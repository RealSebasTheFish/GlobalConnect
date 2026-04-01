import { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { resetPassword } from "../services/accountApi";

function isStrongPassword(password) {
    return (
        password.length >= 8 &&
        /[a-z]/.test(password) &&
        /[A-Z]/.test(password) &&
        /\d/.test(password)
    );
}

export default function ResetPasswordPage() {
    const location = useLocation();
    const prefilledUID = location.state?.accountUID || "";

    const [form, setForm] = useState({
        accountUID: prefilledUID,
        forgotPasswordRescueCode: "",
        newPassword: "",
    });

    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setMessage("");

        if (!isStrongPassword(form.newPassword || "")) {
            setError("Password must be at least 8 characters with uppercase, lowercase, and a number.");
            return;
        }

        setLoading(true);

        try {
            await resetPassword(form);
            setMessage("Password reset successfully.");
            setForm({
                accountUID: prefilledUID,
                forgotPasswordRescueCode: "",
                newPassword: "",
            });
        } catch (err) {
            setError("Reset failed. Rescue code may be invalid.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AuthLayout
            title="Reset Password"
            subtitle="Enter your Account ID, rescue code, and new password."
        >
            <form onSubmit={handleSubmit} className="auth-form">
                <input
                    type="number"
                    name="accountUID"
                    placeholder="Account ID"
                    value={form.accountUID}
                    onChange={handleChange}
                    required
                />

                <input
                    type="text"
                    name="forgotPasswordRescueCode"
                    placeholder="Rescue code"
                    value={form.forgotPasswordRescueCode}
                    onChange={handleChange}
                    required
                />

                <input
                    type="password"
                    name="newPassword"
                    placeholder="New password"
                    value={form.newPassword}
                    onChange={handleChange}
                    minLength={8}
                    title="At least 8 characters, including uppercase, lowercase, and a number"
                    autoComplete="new-password"
                    required
                />

                <button type="submit" className="form-btn" disabled={loading}>
                    {loading ? "Updating..." : "Reset Password"}
                </button>

                {message && <p className="success-text">{message}</p>}
                {error && <p className="error-text">{error}</p>}

                <div className="auth-links">
                    <Link to="/account">Back to Account Info</Link>
                    <Link to="/login">Back to Login</Link>
                </div>
            </form>
        </AuthLayout>
    );
}