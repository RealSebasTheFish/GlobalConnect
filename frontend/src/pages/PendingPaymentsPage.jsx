import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { authenticate } from "../services/accountApi";
import { fetchCatalogue } from "../services/auctionApi";
import { fetchPendingPayments } from "../services/paymentApi";
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

                const authResponse = await authenticate(sessionToken, accountUID);
                const secret =
                    authResponse?.authenticatedRequest?.secret || sessionToken;

                const [pendingData, catalogueData] = await Promise.all([
                    fetchPendingPayments(secret, accountUID),
                    fetchCatalogue(),
                ]);

                setPendingPayments(pendingData?.pendingPayments || []);

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
                                    <div>
                                        <label>Item Name</label>
                                        <p>
                                            {itemNamesById[Number(payment.itemId)] || "Unknown Item"}
                                        </p>
                                    </div>

                                    <div>
                                        <label>Item ID</label>
                                        <p>{payment.itemId}</p>
                                    </div>

                                    <button
                                        type="button"
                                        className="pending-payment-details-btn"
                                        onClick={() =>
                                            navigate(`/payment?itemId=${payment.itemId}`)
                                        }
                                    >
                                        Pay Now
                                    </button>
                                </article>
                            ))}
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
}