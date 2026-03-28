import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Navbar from "../components/Navbar";
import { authenticate, fetchAccount } from "../services/accountApi";
import { fetchCatalogue } from "../services/auctionApi";
import { fetchPendingPayments, pay } from "../services/paymentApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import "../styles/payment.css";

const DEFAULT_SHIPPING_COST = 10;
const DEFAULT_SHIPPING_DAYS = 5;

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

    useEffect(() => {
        async function loadPaymentPageData() {
            try {
                const sessionToken = getSessionToken();
                const accountUID = getAccountUID();

                if (!sessionToken || !accountUID) {
                    navigate("/login");
                    return;
                }

                if (!Number.isFinite(itemId) || itemId <= 0) {
                    setError("Missing or invalid item id for payment.");
                    return;
                }

                const authResponse = await authenticate(sessionToken, accountUID);
                const authSecret = authResponse?.authenticatedRequest?.secret || sessionToken;
                setSecret(authSecret);

                const numericAccountUID = Number(accountUID);
                const pendingResponse = await fetchPendingPayments(
                    authSecret,
                    numericAccountUID
                );
                const hasPendingPaymentForItem = (pendingResponse?.pendingPayments || []).some(
                    (payment) => Number(payment?.itemId) === Number(itemId)
                );

                const catalogueResponse = await fetchCatalogue();
                const matchedItem = catalogueResponse?.items?.find((i) => i.id === itemId) || null;
                const ownsItem = matchedItem && Number(matchedItem.highestBidderUid) === numericAccountUID;

                if (!hasPendingPaymentForItem || !ownsItem) {
                    navigate("/pending-payments", { replace: true });
                    return;
                }

                setIsAuthorizedForItem(true);
                setItem(matchedItem);

                const accountResponse = await fetchAccount(
                    authResponse?.authenticatedRequest
                );
                const fetchedAccount = accountResponse?.accounts?.[0] || null;
                setAccount(fetchedAccount);
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
    const totalAmount = useMemo(() => itemPrice + shippingCost, [itemPrice, shippingCost]);

    async function handleSubmit(event) {
        event.preventDefault();

        setError("");
        setSuccessMessage("");

        if (!cardNumber || !nameOnCard || !expDate || !securityCode) {
            setError("All payment details are required before submitting.");
            return;
        }

        if (!isAuthorizedForItem) {
            setError("You are not authorized to pay for this item.");
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
                cardNumber,
                name: nameOnCard,
                expDate,
                securityCode,
                paymentMethod: "Credit Card",
                amount: itemPrice,
                shippingCost,
                expeditedShipping: false,
                expeditedExtraCost: 0,
            });

            setReceipt(payResponse?.receipt || null);
            setSuccessMessage("Payment cleared successfully.");
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
                        <p>Item #{itemId}</p>
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
                                    <h3>Shipping Address</h3>
                                    <p>{buildAddress(account) || "Address unavailable"}</p>
                                </div>

                                <div className="payment-box">
                                    <h3>Order Summary</h3>
                                    <p className="payment-item-name">
                                        Item: {item?.name || "Unknown Item"}
                                    </p>
                                    <p>Item Price: {formatCurrency(itemPrice)}</p>
                                    <p>Shipping Cost: {formatCurrency(shippingCost)}</p>
                                    <p className="payment-total">
                                        Total: {formatCurrency(totalAmount)}
                                    </p>
                                </div>
                            </div>

                            {!receipt && (
                                <form className="payment-form" onSubmit={handleSubmit}>
                                    <div className="payment-form-grid">
                                        <input
                                            type="text"
                                            placeholder="Credit Card Number"
                                            value={cardNumber}
                                            onChange={(e) =>
                                                setCardNumber(e.target.value)
                                            }
                                            required
                                        />
                                        <input
                                            type="text"
                                            placeholder="Name On Card"
                                            value={nameOnCard}
                                            onChange={(e) =>
                                                setNameOnCard(e.target.value)
                                            }
                                            required
                                        />
                                        <input
                                            type="text"
                                            placeholder="Expiration Date (MM/YY)"
                                            value={expDate}
                                            onChange={(e) => setExpDate(e.target.value)}
                                            required
                                        />
                                        <input
                                            type="password"
                                            placeholder="Security Code"
                                            value={securityCode}
                                            onChange={(e) =>
                                                setSecurityCode(e.target.value)
                                            }
                                            required
                                        />
                                    </div>

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
                                    <h3>Receipt</h3>
                                    <p>Payment Method: {receipt.paymentMethod}</p>
                                    <p>Total Amount Paid: {formatCurrency(totalAmount)}</p>
                                    <p>
                                        Shipping Details: The Item will be shipped in{" "}
                                        {shippingDays} days.
                                    </p>
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