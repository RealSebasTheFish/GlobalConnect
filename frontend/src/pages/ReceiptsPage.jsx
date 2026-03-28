import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { authenticate } from "../services/accountApi";
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

                const authResponse = await authenticate(sessionToken, accountUID);
                const secret =
                    authResponse?.authenticatedRequest?.secret || sessionToken;

                const [receiptsData, catalogueData] = await Promise.all([
                    fetchReceipts(secret, accountUID),
                    fetchCatalogue(),
                ]);

                setReceipts(receiptsData?.receipts || []);

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
                                    <div>
                                        <label>Item ID</label>
                                        <p>{receipt.itemId}</p>
                                    </div>

                                    <div>
                                        <label>Item Name</label>
                                        <p>
                                            {itemNamesById[Number(receipt.itemId)] || "Unknown Item"}
                                        </p>
                                    </div>

                                    <div>
                                        <label>Amount</label>
                                        <p>{formatCurrency(receipt.amount)}</p>
                                    </div>

                                    <div>
                                        <label>Payment Method</label>
                                        <p>{receipt.paymentMethod || "N/A"}</p>
                                    </div>

                                    <div>
                                        <label>Shipping Cost</label>
                                        <p>{formatCurrency(receipt.shippingCost)}</p>
                                    </div>

                                    <div>
                                        <label>Expedited Shipping</label>
                                        <p>{receipt.expeditedShipping ? "Yes" : "No"}</p>
                                    </div>

                                    <div>
                                        <label>Expedited Extra Cost</label>
                                        <p>{formatCurrency(receipt.expeditedExtraCost)}</p>
                                    </div>

                                    <div className="full-width">
                                        <label>Date</label>
                                        <p>{formatDate(receipt.date)}</p>
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