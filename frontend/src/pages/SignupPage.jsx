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

const usernamePattern = /^[a-zA-Z0-9_.-]{3,30}$/;
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const namePattern = /^[a-zA-Z][a-zA-Z' -]{0,49}$/;
const cityCountryPattern = /^[a-zA-Z][a-zA-Z' -]{1,59}$/;
const streetNumberPattern = /^\d+[a-zA-Z]?(?:[\-/ ]\d*[a-zA-Z0-9]+)?$/;
const genericPostalCodePattern = /^(?=.{3,12}$)(?=.*\d)[a-zA-Z0-9][a-zA-Z0-9 -]*[a-zA-Z0-9]$/;

function isValidPostalCode(postalCode, country) {
    const normalizedPostalCode = String(postalCode || "").trim();
    const normalizedCountry = String(country || "").trim().toLowerCase();

    if (normalizedCountry === "canada") {
        return /^[abceghj-nprstvxy]\d[abceghj-nprstv-z][ -]?\d[abceghj-nprstv-z]\d$/i.test(normalizedPostalCode);
    }

    if (normalizedCountry === "usa" || normalizedCountry === "us" || normalizedCountry === "united states") {
        return /^\d{5}(?:-\d{4})?$/.test(normalizedPostalCode);
    }

    return genericPostalCodePattern.test(normalizedPostalCode);
}

function isStrongPassword(password) {
    return (
        password.length >= 8 &&
        /[a-z]/.test(password) &&
        /[A-Z]/.test(password) &&
        /\d/.test(password)
    );
}

function validateSignupForm(values) {
    const normalized = Object.fromEntries(
        Object.entries(values).map(([key, value]) => [key, String(value || "").trim()])
    );

    if (!usernamePattern.test(normalized.username)) {
        return "Username must be 3-30 characters and use letters, numbers, ., _, or -.";
    }
    if (!emailPattern.test(normalized.email)) {
        return "Please enter a valid email address.";
    }
    if (!isStrongPassword(values.password || "")) {
        return "Password must be at least 8 characters with uppercase, lowercase, and a number.";
    }
    if (!namePattern.test(normalized.firstName) || !namePattern.test(normalized.lastName)) {
        return "First and last names must use letters and be 1-50 characters.";
    }
    if (normalized.streetName.length < 2 || normalized.streetName.length > 80) {
        return "Street name must be between 2 and 80 characters.";
    }
    if (!streetNumberPattern.test(normalized.streetNumber) || normalized.streetNumber.length > 20) {
        return "Street number format is invalid (examples: 12, 12B, 221B, 12-4).";
    }
    if (!cityCountryPattern.test(normalized.city) || !cityCountryPattern.test(normalized.country)) {
        return "City and country must be 2-60 letters (spaces, apostrophes, and - allowed).";
    }
    if (!isValidPostalCode(normalized.postalCode, normalized.country)) {
        return "Postal code format is invalid for the selected country.";
    }

    return "";
}

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
        setMessage("");
        setError("");
        setCreatedUID("");

        const validationError = validateSignupForm(form);
        if (validationError) {
            setError(validationError);
            return;
        }

        setLoading(true);

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
                    minLength={3}
                    maxLength={30}
                    pattern="[A-Za-z0-9_.-]{3,30}"
                    title="3-30 characters using letters, numbers, ., _, or -"
                    autoComplete="username"
                    required
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={form.email}
                    onChange={handleChange}
                    autoComplete="email"
                    required
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={form.password}
                    onChange={handleChange}
                    minLength={8}
                    title="At least 8 characters, including uppercase, lowercase, and a number"
                    autoComplete="new-password"
                    required
                />
                <input
                    type="text"
                    name="firstName"
                    placeholder="First name"
                    value={form.firstName}
                    onChange={handleChange}
                    maxLength={50}
                    pattern="[A-Za-z][A-Za-z' -]{0,49}"
                    title="Use letters only (apostrophes, spaces, and - allowed), up to 50 characters"
                    autoComplete="given-name"
                    required
                />
                <input
                    type="text"
                    name="lastName"
                    placeholder="Last name"
                    value={form.lastName}
                    onChange={handleChange}
                    maxLength={50}
                    pattern="[A-Za-z][A-Za-z' -]{0,49}"
                    title="Use letters only (apostrophes, spaces, and - allowed), up to 50 characters"
                    autoComplete="family-name"
                    required
                />
                <input
                    type="text"
                    name="streetName"
                    placeholder="Street name"
                    value={form.streetName}
                    onChange={handleChange}
                    minLength={2}
                    maxLength={80}
                    autoComplete="address-line1"
                    required
                />
                <input
                    type="text"
                    name="streetNumber"
                    placeholder="Street number"
                    value={form.streetNumber}
                    onChange={handleChange}
                    maxLength={20}
                    pattern="\\d+[A-Za-z]?([\\-/ ]\\d*[A-Za-z0-9]+)?"
                    title="Use formats like 12, 12B, 221B, or 12-4 (must include digits)"
                    autoComplete="address-line2"
                    required
                />
                <input
                    type="text"
                    name="city"
                    placeholder="City"
                    value={form.city}
                    onChange={handleChange}
                    maxLength={60}
                    pattern="[A-Za-z][A-Za-z' -]{1,59}"
                    title="Use letters only (spaces, apostrophes, and - allowed)"
                    autoComplete="address-level2"
                    required
                />
                <input
                    type="text"
                    name="country"
                    placeholder="Country"
                    value={form.country}
                    onChange={handleChange}
                    maxLength={60}
                    pattern="[A-Za-z][A-Za-z' -]{1,59}"
                    title="Use letters only (spaces, apostrophes, and - allowed)"
                    autoComplete="country-name"
                    required
                />
                <input
                    type="text"
                    name="postalCode"
                    placeholder="Postal code"
                    value={form.postalCode}
                    onChange={handleChange}
                    maxLength={12}
                    pattern="(?=.{3,12}$)(?=.*\\d)[A-Za-z0-9][A-Za-z0-9 -]*[A-Za-z0-9]"
                    title="Use a valid postal code format for your country (must include a number)"
                    autoComplete="postal-code"
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
                        <p>Save this Account ID. It may be required for account support flows.</p>
                    </div>
                )}

                <div className="auth-links full-width">
                    <Link to="/login">Already have an account? Login</Link>
                </div>
            </form>
        </AuthLayout>
    );
}