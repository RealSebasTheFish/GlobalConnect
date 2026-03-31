import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchCatalogue } from "../services/auctionApi";
import { fetchReceipts } from "../services/paymentApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import "../styles/receipts.css";

function formatCurrency(value) {
    return Number(value || 0).toLocaleString(undefined, {
        style: "currency",
        currency: "CAD",
    });
}

function formatDate(value) {
    if (!value) {
        return "N/A";
    }

    const parsed = new Date(value);
    if (Number.isNaN(parsed.getTime())) {
        return value;
    }

    return parsed.toLocaleString();
}

function getTotalPaid(receipt) {
    return (
        Number(receipt?.amount || 0) +
        Number(receipt?.shippingCost || 0) +
        Number(receipt?.expeditedExtraCost || 0)
    );
}

export default function ReceiptsPage() {
    const navigate = useNavigate();
    const [receipts, setReceipts] = useState([]);
    const [itemNamesById, setItemNamesById] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function loadReceipts() {
            try {
                const sessionToken = getSessionToken();
                const accountUID = getAccountUID();

                if (!sessionToken || !accountUID) {
                    navigate("/login");
                    return;
                }

                const [receiptsResponse, catalogueData] = await Promise.all([
                    fetchReceipts(sessionToken, accountUID),
                    fetchCatalogue(),
                ]);

                setReceipts(receiptsResponse?.receipts || []);

                const names = Object.fromEntries(
                    (catalogueData?.items || []).map((item) => [
                        Number(item.id),
                        item.name,
                    ])
                );
                setItemNamesById(names);
            } catch (err) {
                setError("Could not load receipts. Please try again.");
            } finally {
                setLoading(false);
            }
        }

        loadReceipts();
    }, [navigate]);

    return (
        <div className="receipts-page">
            <Navbar />

            <main className="receipts-wrapper">
                <section className="receipts-card">
                    <div className="receipts-header">
                        <h2>My Receipts</h2>
                    </div>

                    {loading && <p>Loading receipts...</p>}
                    {!loading && error && <p className="error-text">{error}</p>}

                    {!loading && !error && receipts.length === 0 && (
                        <p className="receipts-empty">
                            No receipts found for this account yet.
                        </p>
                    )}

                    {!loading && !error && receipts.length > 0 && (
                        <div className="receipts-grid">
                            {receipts.map((receipt, index) => (
                                <article
                                    className="receipt-item"
                                    key={`${receipt.itemId}-${receipt.date}-${index}`}
                                >
                                    <div className="receipt-top">
                                        <div>
                                            <p className="receipt-small">Receipt for item #{receipt.itemId}</p>
                                            <h3>
                                                {itemNamesById[Number(receipt.itemId)] || "Unknown Item"}
                                            </h3>
                                        </div>

                                        <div className="receipt-total-block">
                                            <label>Total Paid</label>
                                            <p>{formatCurrency(getTotalPaid(receipt))}</p>
                                        </div>
                                    </div>

                                    <div className="receipt-summary">
                                        <div className="receipt-summary-pill">
                                            <label>Item Price</label>
                                            <p>{formatCurrency(receipt.amount)}</p>
                                        </div>

                                        <div className="receipt-summary-pill">
                                            <label>Shipping</label>
                                            <p>{formatCurrency(receipt.shippingCost)}</p>
                                        </div>

                                        <div className="receipt-summary-pill">
                                            <label>Expedited</label>
                                            <p>
                                                {receipt.expeditedShipping
                                                    ? formatCurrency(receipt.expeditedExtraCost)
                                                    : "No"}
                                            </p>
                                        </div>
                                    </div>

                                    <div className="receipt-details">
                                        <div className="receipt-detail">
                                            <label>Payment Method</label>
                                            <p>{receipt.paymentMethod || "N/A"}</p>
                                        </div>

                                        <div className="receipt-detail">
                                            <label>Date</label>
                                            <p>{formatDate(receipt.date)}</p>
                                        </div>
                                    </div>
                                </article>
                            ))}
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
}