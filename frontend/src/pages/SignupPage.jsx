import { useState } from "react";
import { Link } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { signup } from "../services/accountApi";

const initialForm = {
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    streetName: "",
    streetNumber: "",
    city: "",
    country: "",
    postalCode: "",
};

export default function SignupPage() {
    const [form, setForm] = useState(initialForm);
    const [createdUID, setCreatedUID] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setLoading(true);
        setMessage("");
        setError("");
        setCreatedUID("");

        try {
            const data = await signup(form);
            const uid = data?.accounts?.[0]?.accountUID;

            setCreatedUID(uid || "");
            setMessage("Account created successfully.");
            setForm(initialForm);
        } catch (err) {
            setError("Signup failed. Username may already exist or server error occurred.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AuthLayout
            title="Create Account"
            subtitle="Fill in your details to register on GlobalConnect."
        >
            <form onSubmit={handleSubmit} className="auth-form signup-grid">
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={form.username}
                    onChange={handleChange}
                    required
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={form.email}
                    onChange={handleChange}
                    required
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={form.password}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="firstName"
                    placeholder="First name"
                    value={form.firstName}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="lastName"
                    placeholder="Last name"
                    value={form.lastName}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="streetName"
                    placeholder="Street name"
                    value={form.streetName}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="streetNumber"
                    placeholder="Street number"
                    value={form.streetNumber}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="city"
                    placeholder="City"
                    value={form.city}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="country"
                    placeholder="Country"
                    value={form.country}
                    onChange={handleChange}
                    required
                />
                <input
                    type="text"
                    name="postalCode"
                    placeholder="Postal code"
                    value={form.postalCode}
                    onChange={handleChange}
                    required
                />

                <button type="submit" className="form-btn full-width" disabled={loading}>
                    {loading ? "Creating..." : "Create Account"}
                </button>

                {message && <p className="success-text full-width">{message}</p>}
                {error && <p className="error-text full-width">{error}</p>}

                {createdUID && (
                    <div className="uid-box full-width">
                        <strong>Your Account ID is: {createdUID}</strong>
                        <p>Save this Account ID. You need it for login.</p>
                    </div>
                )}

                <div className="auth-links full-width">
                    <Link to="/login">Already have an account? Login</Link>
                </div>
            </form>
        </AuthLayout>
    );
}