import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { authenticate, fetchAccount, startForgotPassword } from "../services/accountApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import "../styles/account.css";

export default function AccountPage() {
    const navigate = useNavigate();
    const [account, setAccount] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);

    const [rescueCode, setRescueCode] = useState("");
    const [rescueMessage, setRescueMessage] = useState("");
    const [rescueError, setRescueError] = useState("");
    const [rescueLoading, setRescueLoading] = useState(false);

    useEffect(() => {
        async function loadAccount() {
            try {
                const sessionToken = getSessionToken();
                const accountUID = getAccountUID();

                if (!sessionToken || !accountUID) {
                    navigate("/login");
                    return;
                }

                const authResponse = await authenticate(sessionToken, accountUID);
                const accountResponse = await fetchAccount(authResponse.authenticatedRequest);

                const fetchedAccount = accountResponse?.accounts?.[0];
                setAccount(fetchedAccount || null);
            } catch (err) {
                setError("Session invalid or account could not be loaded.");
            } finally {
                setLoading(false);
            }
        }

        loadAccount();
    }, [navigate]);

    async function handleGenerateRescueCode() {
        try {
            setRescueLoading(true);
            setRescueError("");
            setRescueMessage("");
            setRescueCode("");

            const accountUID = getAccountUID();
            const data = await startForgotPassword(accountUID);

            setRescueCode(data.rescueCode || "");
            setRescueMessage("Password reset code generated successfully.");
        } catch (err) {
            setRescueError("Could not generate rescue code.");
        } finally {
            setRescueLoading(false);
        }
    }

    function handleGoToReset() {
        if (account?.accountUID) {
            navigate("/reset-password", {
                state: { accountUID: account.accountUID },
            });
        } else {
            navigate("/reset-password");
        }
    }

    if (loading) {
        return <div className="account-wrapper">Loading account...</div>;
    }

    if (error) {
        return (
            <div className="account-wrapper">
                <div className="account-card">
                    <h2>Account</h2>
                    <p className="error-text">{error}</p>
                </div>
            </div>
        );
    }

    return (
        <div className="account-page">
            <Navbar />

            <div className="account-wrapper">
                <div className="account-card">
                    <div className="account-top">
                        <h2>My Account</h2>

                        <button
                            className="change-password-btn"
                            onClick={handleGenerateRescueCode}
                            disabled={rescueLoading}
                        >
                            {rescueLoading ? "Generating..." : "Generate Password Reset Code"}
                        </button>
                    </div>

                    {rescueMessage && <p className="success-text">{rescueMessage}</p>}
                    {rescueError && <p className="error-text">{rescueError}</p>}

                    {rescueCode && (
                        <div className="uid-box account-action-box">
                            <strong>Rescue Code: {rescueCode}</strong>
                            <p>Use this code on the reset password page.</p>
                            <button className="change-password-btn" onClick={handleGoToReset}>
                                Continue to Change Password
                            </button>
                        </div>
                    )}

                    {account && (
                        <div className="account-grid">
                            <div>
                                <label>Account ID</label>
                                <p>{account.accountUID}</p>
                            </div>
                            <div>
                                <label>Username</label>
                                <p>{account.username}</p>
                            </div>
                            <div>
                                <label>Email</label>
                                <p>{account.email}</p>
                            </div>
                            <div>
                                <label>First Name</label>
                                <p>{account.firstName}</p>
                            </div>
                            <div>
                                <label>Last Name</label>
                                <p>{account.lastName}</p>
                            </div>
                            <div>
                                <label>Street Name</label>
                                <p>{account.streetName}</p>
                            </div>
                            <div>
                                <label>Street Number</label>
                                <p>{account.streetNumber}</p>
                            </div>
                            <div>
                                <label>City</label>
                                <p>{account.city}</p>
                            </div>
                            <div>
                                <label>Country</label>
                                <p>{account.country}</p>
                            </div>
                            <div>
                                <label>Postal Code</label>
                                <p>{account.postalCode}</p>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}