import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { login } from "../services/accountApi";
import { saveSession } from "../utils/storage";

export default function LoginPage() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        accountUID: "",
        password: "",
    });
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);
        setError("");
        setMessage("");

        try {
            const data = await login({
                accountUID: Number(form.accountUID),
                password: form.password,
            });

            saveSession(data.sessionToken, form.accountUID);
            setMessage("Login successful.");
            navigate("/dashboard");
        } catch (err) {
            setError("Login failed. Check your Account ID and password.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AuthLayout
            title="Login"
            subtitle="Use your Account ID and password to sign in."
        >
            <form onSubmit={handleSubmit} className="auth-form">
                <input
                    type="number"
                    name="accountUID"
                    placeholder="Enter your Account ID"
                    value={form.accountUID}
                    onChange={handleChange}
                    required
                />

                <input
                    type="password"
                    name="password"
                    placeholder="Enter your password"
                    value={form.password}
                    onChange={handleChange}
                    required
                />

                <button type="submit" className="form-btn" disabled={loading}>
                    {loading ? "Logging in..." : "Login"}
                </button>

                {message && <p className="success-text">{message}</p>}
                {error && <p className="error-text">{error}</p>}

                <div className="auth-links">
                    <Link to="/forgot-password">Forgot password?</Link>
                    <Link to="/signup">Create an account</Link>
                </div>
            </form>
        </AuthLayout>
    );
}