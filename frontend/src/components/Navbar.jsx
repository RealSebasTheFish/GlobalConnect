import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { clearSession } from "../utils/storage";
import "../styles/navbar.css";

export default function Navbar() {
    const [open, setOpen] = useState(false);
    const navigate = useNavigate();

    function handleLogout() {
        clearSession();
        navigate("/login");
    }

    return (
        <header className="top-navbar">
            <div className="nav-left">
                <Link to="/dashboard" className="brand-logo">
                    GC
                </Link>
                <span className="brand-text">GlobalConnect</span>
            </div>

            <div className="nav-right">
                <div className="menu-wrapper">
                    <button
                        className="menu-button"
                        onClick={() => setOpen(!open)}
                    >
                        My Menu ▾
                    </button>

                    {open && (
                        <div className="dropdown-menu">
                            <Link to="/account" onClick={() => setOpen(false)}>
                                Account Info
                            </Link>
                            <Link to="/pending-payments" onClick={() => setOpen(false)}>
                                Pending Payments
                            </Link>
                            <Link to="/receipts" onClick={() => setOpen(false)}>
                                Receipts
                            </Link>
                            <button onClick={handleLogout}>Logout</button>
                        </div>
                    )}
                </div>
            </div>
        </header>
    );
}