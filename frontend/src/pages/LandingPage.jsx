import { Link } from "react-router-dom";
import "../styles/landing.css";

export default function LandingPage() {
    return (
        <div className="landing-page">
            <header className="landing-header">
                <div className="landing-logo">GC</div>
                <nav>
                    <Link to="/login" className="nav-link">
                        Login
                    </Link>
                    <Link to="/signup" className="nav-button">
                        Sign Up
                    </Link>
                </nav>
            </header>

            <main className="landing-main">
                <section className="landing-copy">
                    <span className="landing-tag">Auction Marketplace</span>
                    <h1>
                        Welcome to <span>GlobalConnect</span>
                    </h1>
                    <h3>Bid smarter. Sell faster. Connect globally.</h3>

                    <p>
                        GlobalConnect is a modern auction platform where users can register,
                        sign in, browse active listings, place bids, and get information about current bid.
                    </p>

                    <p>
                        Start by creating your account, and logging in to access the marketplace.
                    </p>

                    <div className="landing-actions">
                        <Link to="/signup" className="primary-btn">
                            Get Started
                        </Link>
                        <Link to="/login" className="secondary-btn">
                            Login
                        </Link>
                    </div>
                </section>

                <section className="landing-visual">
                    <div className="mock-card">
                        <div className="mock-badge">Live Bid</div>
                        <h4>Vintage Watch Auction</h4>
                        <p>Current Bid: $245</p>
                        <p>Time Left: 01:12:43</p>
                        <button>Place Bid</button>
                    </div>

                    <div className="mock-card small">
                        <h4>Seller Dashboard</h4>
                        <p>3 Active Listings</p>
                    </div>

                    <div className="mock-card small">
                        <h4>Winning Offer</h4>
                        <p>Highest Bidder Updated</p>
                    </div>
                </section>
            </main>
        </div>
    );
}