import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchAccount } from "../services/accountApi";
import { fetchCatalogue } from "../services/auctionApi";
import { fetchPendingPayments, fetchReceipts, pay } from "../services/paymentApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import "../styles/payment.css";

const DEFAULT_SHIPPING_COST = 10;
const DEFAULT_SHIPPING_DAYS = 5;
const EXPEDITED_SHIPPING_EXTRA_COST = 25;
const EXPEDITED_SHIPPING_DAYS_REDUCTION = 2;

function formatCurrency(value) {
    return Number(value || 0).toLocaleString(undefined, {
        style: "currency",
        currency: "CAD",
    });
}

function toNumber(value, fallback = 0) {
    const next = Number(value);
    return Number.isFinite(next) ? next : fallback;
}

function getShippingDays(item, itemId) {
    const itemValue =
        item?.shippingDays ??
        item?.shippingTimeDays ??
        item?.estimatedShippingDays ??
        item?.shippingEtaDays;

    if (Number.isFinite(Number(itemValue)) && Number(itemValue) > 0) {
        return Number(itemValue);
    }

    return (Number(itemId) % 5) + DEFAULT_SHIPPING_DAYS;
}

function getExpeditedShippingDays(days) {
    return Math.max(1, days - EXPEDITED_SHIPPING_DAYS_REDUCTION);
}

function getCardDigits(value) {
    return value.replace(/\D/g, "");
}

function formatCardNumber(value) {
    return getCardDigits(value)
        .slice(0, 19)
        .replace(/(\d{4})(?=\d)/g, "$1 ")
        .trim();
}

function isValidCardLength(value) {
    const digits = getCardDigits(value);
    return digits.length >= 13 && digits.length <= 19;
}

function formatExpiryInput(value) {
    const digits = value.replace(/\D/g, "").slice(0, 4);

    if (digits.length <= 2) {
        return digits;
    }

    return `${digits.slice(0, 2)}/${digits.slice(2)}`;
}

function isValidExpiryDate(value) {
    const match = value.match(/^(\d{2})\/(\d{2})$/);
    if (!match) {
        return false;
    }

    const month = Number(match[1]);
    const year = Number(match[2]);

    if (month < 1 || month > 12) {
        return false;
    }

    const fullYear = 2000 + year;
    const expiryDate = new Date(fullYear, month, 0, 23, 59, 59, 999);

    return expiryDate.getTime() >= Date.now();
}

function buildAddress(account) {
    if (!account) {
        return "";
    }

    return [
        `${account.streetNumber || ""} ${account.streetName || ""}`.trim(),
        account.city,
        account.country,
        account.postalCode,
    ]
        .filter(Boolean)
        .join(", ");
}

export default function PaymentPage() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const itemId = Number(searchParams.get("itemId"));

    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const [account, setAccount] = useState(null);
    const [secret, setSecret] = useState("");
    const [item, setItem] = useState(null);
    const [receipt, setReceipt] = useState(null);
    const [isAuthorizedForItem, setIsAuthorizedForItem] = useState(false);

    const [cardNumber, setCardNumber] = useState("");
    const [nameOnCard, setNameOnCard] = useState("");
    const [expDate, setExpDate] = useState("");
    const [securityCode, setSecurityCode] = useState("");
    const [useExpeditedShipping, setUseExpeditedShipping] = useState(false);

    useEffect(() => {
        async function loadPaymentPageData() {
            try {
                const sessionToken = getSessionToken();
                const accountUID = getAccountUID();

                if (!sessionToken || !accountUID || !itemId) {
                    throw new Error("Missing required session, account, or item info.");
                }

                const [pendingResponse, receiptsResponse, catalogueData, accountData] = await Promise.all([
                    fetchPendingPayments(sessionToken, accountUID),
                    fetchReceipts(sessionToken, accountUID),
                    fetchCatalogue(),
                    fetchAccount(sessionToken, accountUID)
                ]);

                const hasPendingPaymentForItem = (pendingResponse?.pendingPayments || []).some(
                    (payment) => Number(payment?.itemId) === Number(itemId)
                );

                const existingReceipt =
                    (receiptsResponse?.receipts || []).find(
                        (entry) => Number(entry?.itemId) === Number(itemId)
                    ) || null;

                const matchedItem = catalogueData?.items?.find((i) => i.id === itemId) || null;
                const ownsItem = matchedItem && Number(matchedItem.highestBidderUid) === Number(accountUID);

                if (existingReceipt) {
                    setReceipt(existingReceipt);
                    setSuccessMessage("This item has been paid for.");
                }

                if ((!hasPendingPaymentForItem && !existingReceipt) || !ownsItem) {
                    navigate("/pending-payments", { replace: true });
                    return;
                }

                setIsAuthorizedForItem(!existingReceipt);
                setItem(matchedItem);

                setAccount(accountData?.account || null);
                setSecret(sessionToken);

            } catch (err) {
                setError("Could not load payment details.");
            } finally {
                setLoading(false);
            }
        }

        loadPaymentPageData();
    }, [itemId, navigate]);

    const itemPrice = useMemo(() => {
        const highestBid = toNumber(item?.currentHighestBid);
        if (highestBid > 0) {
            return highestBid;
        }

        return toNumber(item?.startingPrice, 0);
    }, [item]);

    const shippingCost = useMemo(() => {
        const apiShippingCost = toNumber(item?.shippingCost, NaN);
        if (Number.isFinite(apiShippingCost) && apiShippingCost >= 0) {
            return apiShippingCost;
        }
        return DEFAULT_SHIPPING_COST;
    }, [item]);

    const shippingDays = useMemo(() => getShippingDays(item, itemId), [item, itemId]);

    const expeditedExtraCost = useMemo(
        () => (useExpeditedShipping ? EXPEDITED_SHIPPING_EXTRA_COST : 0),
        [useExpeditedShipping]
    );

    const estimatedShippingDays = useMemo(
        () =>
            useExpeditedShipping
                ? getExpeditedShippingDays(shippingDays)
                : shippingDays,
        [shippingDays, useExpeditedShipping]
    );

    const totalAmount = useMemo(
        () => itemPrice + shippingCost + expeditedExtraCost,
        [itemPrice, shippingCost, expeditedExtraCost]
    );

    const receiptShippingCost = useMemo(
        () => toNumber(receipt?.shippingCost, shippingCost),
        [receipt, shippingCost]
    );

    const receiptExpeditedExtraCost = useMemo(
        () => toNumber(receipt?.expeditedExtraCost, 0),
        [receipt]
    );

    const receiptTotalAmount = useMemo(
        () => toNumber(receipt?.amount, itemPrice) + receiptShippingCost + receiptExpeditedExtraCost,
        [receipt, itemPrice, receiptShippingCost, receiptExpeditedExtraCost]
    );

    const receiptShippingDays = useMemo(
        () =>
            receipt?.expeditedShipping
                ? getExpeditedShippingDays(shippingDays)
                : shippingDays,
        [receipt, shippingDays]
    );

    async function handleSubmit(event) {
        event.preventDefault();

        setError("");
        setSuccessMessage("");

        if (!cardNumber || !nameOnCard || !expDate || !securityCode) {
            setError("All payment details are required before submitting.");
            return;
        }

        if (!isValidCardLength(cardNumber)) {
            setError("Enter a valid credit card number with 13 to 19 digits.");
            return;
        }

        if (!/^\d{3}$/.test(securityCode)) {
            setError("CVV must be exactly 3 numbers.");
            return;
        }

        if (!isValidExpiryDate(expDate)) {
            setError("Enter a valid expiration date in MM/YY format.");
            return;
        }

        if (!isAuthorizedForItem) {
            setError("This item has already been paid for and cannot be paid again.");
            return;
        }

        const accountUID = Number(getAccountUID());
        if (!accountUID || !itemId) {
            setError("Missing account or item details.");
            return;
        }

        try {
            setSubmitting(true);
            const payResponse = await pay(secret, {
                accountUID,
                itemId,
                cardNumber: getCardDigits(cardNumber),
                name: nameOnCard.trim(),
                expDate,
                securityCode,
                paymentMethod: "Credit Card",
                amount: itemPrice,
                shippingCost,
                expeditedShipping: useExpeditedShipping,
                expeditedExtraCost,
            });

            setReceipt(payResponse?.receipt || null);
            setSuccessMessage("Payment cleared successfully.");
            setIsAuthorizedForItem(false);
        } catch (err) {
            setError(err?.message || "Payment could not be completed.");
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <div className="payment-page">
            <Navbar />

            <main className="payment-wrapper">
                <section className="payment-card">
                    <div className="payment-header">
                        <h2>Complete Payment</h2>
                    </div>

                    {loading && <p>Loading payment details...</p>}
                    {!loading && error && <p className="error-text">{error}</p>}
                    {!loading && successMessage && (
                        <p className="success-text">{successMessage}</p>
                    )}

                    {!loading && (
                        <>
                            <div className="payment-grid">
                                <div className="payment-box">
                                    <div className="payment-box-header">
                                        <p className="payment-box-small">Delivery details</p>
                                        <h3>Shipping Address</h3>
                                    </div>
                                    <p className="payment-address">
                                        {buildAddress(account) || "Address unavailable"}
                                    </p>
                                </div>

                                <div className="payment-box">
                                    <div className="payment-box-header">
                                        <p className="payment-box-small">Order summary</p>
                                        <h3>{item?.name || "Unknown Item"}</h3>
                                    </div>

                                    <div className="payment-summary-list">
                                        <div className="payment-summary-row">
                                            <label>Item Price</label>
                                            <p>{formatCurrency(itemPrice)}</p>
                                        </div>

                                        <div className="payment-summary-row">
                                            <label>Shipping Cost</label>
                                            <p>{formatCurrency(receipt ? receiptShippingCost : shippingCost)}</p>
                                        </div>

                                        <div className="payment-summary-row">
                                            <label>Expedited Shipping</label>
                                            <p>
                                                {receipt
                                                    ? receipt.expeditedShipping
                                                        ? formatCurrency(receiptExpeditedExtraCost)
                                                        : "No"
                                                    : useExpeditedShipping
                                                        ? formatCurrency(expeditedExtraCost)
                                                        : "No"}
                                            </p>
                                        </div>
                                    </div>

                                    <div className="payment-total-block">
                                        <label>Total</label>
                                        <p>{formatCurrency(receipt ? receiptTotalAmount : totalAmount)}</p>
                                    </div>
                                </div>
                            </div>

                            {!receipt && (
                                <form className="payment-form" onSubmit={handleSubmit}>
                                    <div className="payment-form-header">
                                        <p className="payment-box-small">Payment details</p>
                                        <h3>Card Information</h3>
                                    </div>

                                    <div className="payment-form-grid">
                                        <label className="payment-field">
                                            <span>Credit Card Number</span>
                                            <input
                                                type="text"
                                                placeholder="1234 5678 9012 3456"
                                                value={cardNumber}
                                                inputMode="numeric"
                                                maxLength={23}
                                                onChange={(e) =>
                                                    setCardNumber(formatCardNumber(e.target.value))
                                                }
                                                required
                                            />
                                        </label>

                                        <label className="payment-field">
                                            <span>Name On Card</span>
                                            <input
                                                type="text"
                                                placeholder="Name on card"
                                                value={nameOnCard}
                                                onChange={(e) =>
                                                    setNameOnCard(e.target.value)
                                                }
                                                required
                                            />
                                        </label>

                                        <label className="payment-field">
                                            <span>Expiration Date</span>
                                            <input
                                                type="text"
                                                placeholder="MM/YY"
                                                value={expDate}
                                                inputMode="numeric"
                                                maxLength={5}
                                                onChange={(e) => setExpDate(formatExpiryInput(e.target.value))}
                                                required
                                            />
                                        </label>

                                        <label className="payment-field">
                                            <span>Security Code</span>
                                            <input
                                                type="password"
                                                placeholder="CVV"
                                                value={securityCode}
                                                inputMode="numeric"
                                                maxLength={3}
                                                onChange={(e) =>
                                                    setSecurityCode(e.target.value.replace(/\D/g, "").slice(0, 3))
                                                }
                                                required
                                            />
                                        </label>
                                    </div>

                                    <label className="payment-checkbox">
                                        <input
                                            type="checkbox"
                                            checked={useExpeditedShipping}
                                            onChange={(e) =>
                                                setUseExpeditedShipping(e.target.checked)
                                            }
                                        />
                                        <span>
                                            Add expedited shipping for{" "}
                                            {formatCurrency(EXPEDITED_SHIPPING_EXTRA_COST)} and
                                            get estimated delivery in {estimatedShippingDays} days.
                                        </span>
                                    </label>

                                    <button
                                        type="submit"
                                        className="form-btn"
                                        disabled={submitting}
                                    >
                                        {submitting ? "Submitting..." : "Submit"}
                                    </button>
                                </form>
                            )}

                            {receipt && (
                                <div className="receipt-result">
                                    <div className="payment-box-header">
                                        <p className="payment-box-small">Payment complete</p>
                                        <h3>Receipt</h3>
                                    </div>
                                    <div className="payment-summary-list">
                                        <div className="payment-summary-row">
                                            <label>Payment Method</label>
                                            <p>{receipt.paymentMethod}</p>
                                        </div>
                                        <div className="payment-summary-row">
                                            <label>Total Amount Paid</label>
                                            <p>{formatCurrency(receiptTotalAmount)}</p>
                                        </div>
                                        <div className="payment-summary-row">
                                            <label>Shipping Details</label>
                                            <p>The item will be shipped in {receiptShippingDays} days.</p>
                                        </div>
                                    </div>
                                    <br/>
                                    <button
                                        type="button"
                                        className="form-btn"
                                        onClick={() => navigate("/receipts")}
                                    >
                                        View All Receipts
                                    </button>
                                </div>
                            )}
                        </>
                    )}
                </section>
            </main>
        </div>
    );
}