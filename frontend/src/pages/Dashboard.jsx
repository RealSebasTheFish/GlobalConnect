import Navbar from "../components/Navbar";
import "../styles/dashboard.css";

export default function DashboardPage() {
    return (
        <div className="dashboard-page">
            <Navbar />

            <main className="dashboard-content">
                <div className="dashboard-hero">
                    <span className="dashboard-tag">Auction Marketplace</span>

                    <h1>Welcome to GlobalConnect Bid</h1>


                </div>
            </main>
        </div>
    );
}