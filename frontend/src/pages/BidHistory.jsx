import { useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import { getAccountUID, getSessionToken } from "../utils/storage";
import { fetchCatalogue, fetchUserItems } from "../services/auctionApi";
import "../styles/bidHistory.css";

export default function BidHistory() {
    const [bids, setBids] = useState([]);
    const [myItemsBids, setMyItemsBids] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const accountUID = Number(getAccountUID());
    const sessionToken = getSessionToken();

    useEffect(() => {
        const loadHistory = async () => {
            try {
                setLoading(true);
                const catalogueData = await fetchCatalogue();
                const allItems = Array.isArray(catalogueData) ? catalogueData : catalogueData.items || [];
                
                const userItemsData = await fetchUserItems({ sessionToken, accountUID });
                const userItems = Array.isArray(userItemsData) ? userItemsData : userItemsData.items || [];

                // 1. Process Bids They Have Done
                const localHistory = JSON.parse(localStorage.getItem(`bid_history_${accountUID}`) || "[]");
                
                const enrichedBids = localHistory.map(localBid => {
                    const currentItemData = allItems.find(i => i.id === localBid.itemId) || 
                                          userItems.find(i => i.id === localBid.itemId); // fallback
                    
                    let status = "Unknown";
                    let statusClass = "status-unknown";
                    
                    if (currentItemData) {
                        const isHighest = currentItemData.highestBidderUid === accountUID;
                        const isEnded = currentItemData.closed || currentItemData.auctionEndTime < Date.now();
                        
                        if (isEnded) {
                            if (isHighest) {
                                status = "Won";
                                statusClass = "status-won";
                            } else {
                                status = "Lost";
                                statusClass = "status-lost";
                            }
                        } else {
                            if (isHighest) {
                                status = "Winning";
                                statusClass = "status-winning";
                            } else {
                                status = "Outbid";
                                statusClass = "status-outbid";
                            }
                        }
                    }

                    return {
                        ...localBid,
                        currentHighestBid: currentItemData ? currentItemData.currentHighestBid : localBid.bidAmount,
                        status,
                        statusClass,
                        latestDate: new Date(localBid.timestamp).toLocaleString()
                    };
                }).sort((a, b) => b.timestamp - a.timestamp);
                
                setBids(enrichedBids);

                // 2. Process Bids on Their Items
                const enrichedMyItems = userItems.filter(item => item.highestBidderUid > 0).map(item => {
                    const isEnded = item.closed || item.auctionEndTime < Date.now();
                    
                    return {
                        ...item,
                        status: isEnded ? "Sold" : "Active Bidding",
                        statusClass: isEnded ? "status-sold" : "status-active-bidding",
                    };
                });
                
                setMyItemsBids(enrichedMyItems);

            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        loadHistory();
    }, [accountUID, sessionToken]);

    return (
        <div className="history-page">
            <Navbar />
            <main className="history-container">
                <h1>Tracking & History</h1>
                
                {error && <p className="history-error">{error}</p>}
                
                {loading ? (
                    <p>Loading history...</p>
                ) : (
                    <div className="history-sections">
                        <section className="history-section">
                            <h2>Your Bids</h2>
                            {bids.length === 0 ? (
                                <p className="empty-state">You have not placed any bids yet.</p>
                            ) : (
                                <div className="history-grid">
                                    {bids.map(bid => (
                                        <div key={bid.itemId} className="history-card">
                                            <div className="history-card-header">
                                                <h4>{bid.itemName}</h4>
                                                <span className={`hist-badge ${bid.statusClass}`}>{bid.status}</span>
                                            </div>
                                            <p>Your Bid: <strong>${bid.bidAmount.toFixed(2)}</strong></p>
                                            <p>Current/Final Price: ${bid.currentHighestBid.toFixed(2)}</p>
                                            <p className="history-date">Last Bid: {bid.latestDate}</p>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </section>

                        <section className="history-section">
                            <h2>Bids on Your Items</h2>
                            {myItemsBids.length === 0 ? (
                                <p className="empty-state">No one has bid on your items yet.</p>
                            ) : (
                                <div className="history-grid">
                                    {myItemsBids.map(item => (
                                        <div key={item.id} className="history-card">
                                            <div className="history-card-header">
                                                <h4>{item.name}</h4>
                                                <span className={`hist-badge ${item.statusClass}`}>{item.status}</span>
                                            </div>
                                            <p>Top Bid: <strong>${item.currentHighestBid.toFixed(2)}</strong></p>
                                            <p>Starting Price: ${item.startingPrice.toFixed(2)}</p>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </section>
                    </div>
                )}
            </main>
        </div>
    );
}
