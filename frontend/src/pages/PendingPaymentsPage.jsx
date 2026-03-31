import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchCatalogue } from "../services/auctionApi";
import { fetchPendingPayments, fetchReceipts } from "../services/paymentApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import "../styles/pending-payments.css";

export default function PendingPaymentsPage() {
    const navigate = useNavigate();
    const [pendingPayments, setPendingPayments] = useState([]);
    const [itemNamesById, setItemNamesById] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function loadPendingPayments() {
            try {
                const sessionToken = getSessionToken();
                const accountUID = getAccountUID();

                if (!sessionToken || !accountUID) {
                    navigate("/login");
                    return;
                }

                const [pendingData, receiptsData, catalogueData] = await Promise.all([
                    fetchPendingPayments(sessionToken, accountUID),
                    fetchReceipts(sessionToken, accountUID),
                    fetchCatalogue(),
                ]);

                const paidItemIds = new Set(
                    (receiptsData?.receipts || []).map((receipt) => Number(receipt?.itemId))
                );

                setPendingPayments(
                    (pendingData?.pendingPayments || []).filter(
                        (payment) => !paidItemIds.has(Number(payment?.itemId))
                    )
                );

                const names = Object.fromEntries(
                    (catalogueData?.items || []).map((item) => [
                        Number(item.id),
                        item.name,
                    ])
                );
                setItemNamesById(names);
            } catch (err) {
                setError("Could not load pending payments. Please try again.");
            } finally {
                setLoading(false);
            }
        }

        loadPendingPayments();
    }, [navigate]);

    return (
        <div className="pending-payments-page">
            <Navbar />

            <main className="pending-payments-wrapper">
                <section className="pending-payments-card">
                    <div className="pending-payments-header">
                        <h2>Pending Payments</h2>
                    </div>

                    {loading && <p>Loading pending payments...</p>}
                    {!loading && error && <p className="error-text">{error}</p>}

                    {!loading && !error && pendingPayments.length === 0 && (
                        <p className="pending-payments-empty">
                            No pending payments found for this account.
                        </p>
                    )}

                    {!loading && !error && pendingPayments.length > 0 && (
                        <div className="pending-payments-grid">
                            {pendingPayments.map((payment, index) => (
                                <article
                                    className="pending-payment-item"
                                    key={`${payment.accountUID}-${payment.itemId}-${index}`}
                                >
                                    <div className="pending-payment-top">
                                        <div>
                                            <h3>
                                                {itemNamesById[Number(payment.itemId)] || "Unknown Item"}
                                            </h3>
                                        </div>
                                    </div>

                                    <div className="pending-payment-details">
                                        <div className="pending-payment-detail">
                                            <label>Item ID</label>
                                            <p>{payment.itemId}</p>
                                        </div>

                                        <div className="pending-payment-detail">
                                            <label>Status</label>
                                            <p>Awaiting payment</p>
                                        </div>
                                    </div>

                                    <div className="pending-payment-actions">
                                        <button
                                            type="button"
                                            className="pending-payment-details-btn"
                                            onClick={() =>
                                                navigate(`/payment?itemId=${payment.itemId}`)
                                            }
                                        >
                                            Pay Now
                                        </button>
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