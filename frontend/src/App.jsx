import { Routes, Route } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import AccountPage from "./pages/AccountPage";
import Dashboard from "./pages/Dashboard";
import ReceiptsPage from "./pages/ReceiptsPage";
import PendingPaymentsPage from "./pages/PendingPaymentsPage";
import PaymentPage from "./pages/PaymentPage";

export default function App() {
    return (
        <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/forgot-password" element={<ForgotPasswordPage />} />
            <Route path="/reset-password" element={<ResetPasswordPage />} />
            <Route path="/account" element={<AccountPage />} />
            <Route path="/pending-payments" element={<PendingPaymentsPage />} />
            <Route path="/payment" element={<PaymentPage />} />
            <Route path="/receipts" element={<ReceiptsPage />} />
            <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
    );
}