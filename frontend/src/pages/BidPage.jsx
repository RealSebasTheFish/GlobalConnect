import { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchCatalogue, placeBid } from "../services/auctionApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import { SmartBidAdvisorPanel } from "../features/smartBidAdvisor";
import "../styles/bidPage.css";

export default function BidPage() {
    const { itemId } = useParams();
    const navigate = useNavigate();
    const accountUID = Number(getAccountUID());
    const sessionToken = getSessionToken();

    const [item, setItem] = useState(null);
    const [bidAmount, setBidAmount] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [timeLeft, setTimeLeft] = useState("");
    
    // We strictly use this ref to know if it's the very first load
    const isFirstLoadRef = useRef(true);

    const loadItem = async () => {
        try {
            // Only show full loading screen on initial load to prevent flickering
            if (isFirstLoadRef.current) setLoading(true);
            
            const data = await fetchCatalogue();
            const allItems = Array.isArray(data) ? data : data.items || [];
            const foundItem = allItems.find(i => i.id === Number(itemId));

            if (!foundItem) {
                setError("Item not found or auction closed.");
            } else {
                setItem(foundItem);
                
                // Only setup initial bid value on the very FIRST successful load
                if (isFirstLoadRef.current) {
                    setBidAmount((foundItem.currentHighestBid + 5).toFixed(2));
                    isFirstLoadRef.current = false;
                }
            }
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadItem();
        // Set up interval to refresh the catalog every 2 seconds to get newest bids
        const intervalId = setInterval(loadItem, 2000);
        return () => clearInterval(intervalId);
    }, [itemId]);

    // Track the previous highest bid to detect changes and animations
    const prevHighestRef = useRef(null);
    const [justOutbid, setJustOutbid] = useState(false);
    
    useEffect(() => {
        if (!item) return;

        // If the item just finished loading for the first time or updated
        if (prevHighestRef.current !== null && 
            prevHighestRef.current.highestBidderUid === accountUID && 
            item.highestBidderUid !== accountUID && 
            item.highestBidderUid > 0) {
            // We were winning and now we are not
            setJustOutbid(true);
            setTimeout(() => setJustOutbid(false), 3000);
        }
        
        prevHighestRef.current = {
            highestBidderUid: item.highestBidderUid,
            currentHighestBid: item.currentHighestBid
        };
        
        // Remove the bid logic from forcing value to always update on refresh
        // This stops overriding the user's manual keyboard input!
    }, [item, accountUID]);

    useEffect(() => {
        if (!item || !item.auctionEndTime) return;

        const updateTimer = () => {
            const now = Date.now();
            const diff = item.auctionEndTime - now;

            if (diff <= 0) {
                setTimeLeft("Auction Ended");
            } else {
                const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
                const seconds = Math.floor((diff % (1000 * 60)) / 1000);
                setTimeLeft(`${minutes}m ${seconds}s`);
            }
        };

        updateTimer();
        const timerId = setInterval(updateTimer, 1000);
        return () => clearInterval(timerId);
    }, [item]);

    const handleBidChange = (e) => {
        setBidAmount(e.target.value);
    };

    const backendBidHistory = Array.isArray(item?.bidHistory) ? item.bidHistory : [];
    const localBidHistory = (() => {
        const history = JSON.parse(localStorage.getItem(`bid_history_${accountUID}`) || "[]");
        return Array.isArray(history)
            ? history
                .filter((entry) => Number(entry?.itemId) === Number(item?.id))
                .map((entry) => ({
                    bidderUID: accountUID,
                    bidAmount: Number(entry?.bidAmount ?? 0),
                    bidTime: entry?.timestamp,
                }))
            : [];
    })();
    const advisorBidHistory = [...backendBidHistory, ...localBidHistory].sort(
        (a, b) => Number(a?.bidTime ?? 0) - Number(b?.bidTime ?? 0)
    );

    const timeLeftMs = (() => {
        if (!item?.auctionEndTime) {
            return 0;
        }
        const endTime = Number(item.auctionEndTime);
        return Math.max(endTime - Date.now(), 0);
    })();

    const advisorContext = {
        timeLeftLabel: timeLeft,
        timeLeftMs,
        totalBids: advisorBidHistory.length,
        myLatestBidAmount: localBidHistory[localBidHistory.length - 1]?.bidAmount ?? 0,
        myIsCurrentHighestBidder: item?.highestBidderUid === accountUID,
    };

    const handlePlaceBid = async (e) => {
        e.preventDefault();
        setError("");
        setSuccessMessage("");

        const amount = parseFloat(bidAmount);
        if (isNaN(amount) || amount <= item.currentHighestBid) {
            setError(`Bid must be greater than $${item.currentHighestBid}`);
            return;
        }

        try {
            await placeBid({
                sessionToken,
                accountUID,
                targetItemId: item.id,
                bidAmount: amount
            });
            
            // Save to local storage for Bid History
            const bidRecord = {
                itemId: item.id,
                itemName: item.name,
                bidAmount: amount,
                timestamp: Date.now()
            };
            const history = JSON.parse(localStorage.getItem(`bid_history_${accountUID}`) || "[]");
            
            // Remove previous bid for the same item if it exists, so we only track our latest bid per item
            const filteredHistory = history.filter(b => b.itemId !== item.id);
            filteredHistory.push(bidRecord);
            localStorage.setItem(`bid_history_${accountUID}`, JSON.stringify(filteredHistory));

            setSuccessMessage("Bid placed successfully!");
            // Automatically queue up the next suggested bid + 5 after winning submission
            setBidAmount((amount + 5).toFixed(2));
            loadItem(); // Reload the item to show the new highest bid
        } catch (err) {
            setError(err.message || "Failed to place bid.");
        }
    };

    if (loading && !item) {
        return (
            <div className="bid-page">
                <Navbar />
                <div className="bid-container loading-container">
                    <p>Loading item details...</p>
                </div>
            </div>
        );
    }

    if (error && !item) {
        return (
            <div className="bid-page">
                <Navbar />
                <div className="bid-container error-container">
                    <h2>Error</h2>
                    <p>{error}</p>
                    <button className="back-btn" onClick={() => navigate("/dashboard")}>Back to Catalogue</button>
                </div>
            </div>
        );
    }

    return (
        <div className="bid-page">
            <Navbar />
            
            <main className="bid-main">
                <button className="back-link" onClick={() => navigate("/dashboard")}>
                    &larr; Back to Catalogue
                </button>

                {item && (() => {
                    const isWinning = item.highestBidderUid === accountUID;
                    const isEnded = item.closed || timeLeft === "Auction Ended";
                    
                    let cardClass = "bid-action-section";
                    if (justOutbid) cardClass += " outbid";
                    else if (isWinning && !isEnded) cardClass += " winning-bid";
                    else if (isEnded && isWinning) cardClass += " winning-bid";
                    else if (isEnded && !isWinning) cardClass += " lost-bid";

                    return (
                        <div className="bid-content">
                            <section className="bid-info-section">
                                {isEnded ? (
                                    <span className={`bid-badge ${isWinning ? 'won' : 'lost'}`}>
                                        {isWinning ? 'You Won!' : 'Auction Ended'}
                                    </span>
                                ) : (
                                    <span className="bid-badge">Live Auction</span>
                                )}
                                <h1>{item.name}</h1>
                                <p className="item-desc">{item.description}</p>
                                
                                <div className="auction-stats">
                                    <div className="stat-box">
                                        <span className="stat-label">Current Highest Bid</span>
                                        <span className="stat-value">${item.currentHighestBid.toFixed(2)}</span>
                                    </div>
                                    <div className="stat-box">
                                        <span className="stat-label">Time Remaining</span>
                                        <span className={`stat-value ${isEnded ? "ended" : "active-time"}`}>
                                            {timeLeft}
                                        </span>
                                    </div>
                                </div>
                                
                                {/* Winner Announcement */}
                                {isEnded && (
                                    <div className={`end-announcement ${isWinning ? 'success' : 'failure'}`}>
                                        {isWinning ? 
                                            `Congratulations! You won this item for $${item.currentHighestBid.toFixed(2)}.` : 
                                            `This item was sold to another bidder for $${item.currentHighestBid.toFixed(2)}.`
                                        }
                                    </div>
                                )}
                            </section>

                            <section className={cardClass}>
                                <div className="bid-card">
                                    <h3>{isEnded ? "Final Status" : "Place Your Bid"}</h3>
                                    
                                    {error && <div className="form-error">{error}</div>}
                                    {successMessage && <div className="form-success">{successMessage}</div>}
                                    
                                    {isWinning && !isEnded && (
                                        <div className="winning-status">
                                            You are currently the highest bidder!
                                        </div>
                                    )}

                                    <form onSubmit={handlePlaceBid}>
                                        <div className="bid-input-group">
                                            <label htmlFor="bid-amount">Bid Amount ($)</label>
                                            <div className="input-wrapper">
                                                <span className="currency-symbol">$</span>
                                                <input 
                                                    id="bid-amount"
                                                    type="number" 
                                                    step="0.01"
                                                    min={(item.currentHighestBid + 0.01).toFixed(2)}
                                                    value={bidAmount}
                                                    onChange={handleBidChange}
                                                    disabled={item.closed || timeLeft === "Auction Ended"}
                                                    placeholder={(item.currentHighestBid + 5).toFixed(2)}
                                                    required
                                                />
                                            </div>
                                            <small>Must be higher than ${item.currentHighestBid.toFixed(2)}</small>
                                        </div>
                                        
                                        <button 
                                            type="submit" 
                                            className="place-bid-btn"
                                            disabled={item.closed || timeLeft === "Auction Ended"}
                                        >
                                            {item.closed || timeLeft === "Auction Ended" ? "Auction Closed" : "Submit Bid"}
                                        </button>
                                    </form>
                                </div>
                            </section>
                        </div>
                    );
                })()}

                {item && (
                    <section className="bid-advisor-section" aria-label="AI bidding advisor section">
                        <SmartBidAdvisorPanel
                            className="sba-layout-wide"
                            item={item}
                            bidHistory={advisorBidHistory}
                            auctionContext={advisorContext}
                        />
                    </section>
                )}
            </main>
        </div>
    );
}