import { useState } from "react";
import { Link } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { startForgotPassword } from "../services/accountApi";

export default function ForgotPasswordPage() {
    const [accountUID, setAccountUID] = useState("");
    const [rescueCode, setRescueCode] = useState("");
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);
        setError("");
        setMessage("");
        setRescueCode("");

        try {
            const data = await startForgotPassword(accountUID);
            setRescueCode(data.rescueCode || "");
            setMessage("Rescue code generated successfully.");
        } catch (err) {
            setError("Could not generate rescue code. Check the Account ID.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AuthLayout
            title="Forgot Password"
            subtitle="Generate your rescue code using your Account ID."
        >
            <form onSubmit={handleSubmit} className="auth-form">
                <input
                    type="number"
                    placeholder="Enter your Account ID"
                    value={accountUID}
                    onChange={(e) => setAccountUID(e.target.value)}
                    required
                />

                <button type="submit" className="form-btn" disabled={loading}>
                    {loading ? "Generating..." : "Generate Rescue Code"}
                </button>

                {message && <p className="success-text">{message}</p>}
                {error && <p className="error-text">{error}</p>}

                {rescueCode && (
                    <div className="uid-box">
                        <strong>Rescue Code: {rescueCode}</strong>
                        <p>Use this on the reset password page.</p>
                    </div>
                )}

                <div className="auth-links">
                    <Link to="/reset-password">Go to Reset Password</Link>
                    <Link to="/login">Back to Login</Link>
                </div>
            </form>
        </AuthLayout>
    );
}