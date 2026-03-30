import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchCatalogue } from "../services/auctionApi";
import { getAccountUID } from "../utils/storage";
import "../styles/dashboard.css";

export default function DashboardPage() {
    const [items, setItems] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [outbidItems, setOutbidItems] = useState(new Set());
    const navigate = useNavigate();
    
    const accountUID = Number(getAccountUID());
    const prevItemsRef = useRef([]);

    useEffect(() => {
        let isMounted = true;
        const loadCatalogue = async () => {
            try {
                const data = await fetchCatalogue();
                if (!isMounted) return;
                
                // Assuming data is an array or has an items array
                const newItems = Array.isArray(data) ? data : data.items || [];
                
                if (prevItemsRef.current.length > 0) {
                    let hasNewOutbids = false;
                    const newlyOutbidIds = [];

                    prevItemsRef.current.forEach(prevItem => {
                        if (prevItem.highestBidderUid === accountUID) {
                            const currentItem = newItems.find(i => i.id === prevItem.id);
                            if (currentItem && currentItem.highestBidderUid !== accountUID && currentItem.highestBidderUid > 0) {
                                newlyOutbidIds.push(currentItem.id);
                                hasNewOutbids = true;
                            }
                        }
                    });
                    
                    if (hasNewOutbids) {
                        setOutbidItems(curr => {
                            const updated = new Set(curr);
                            newlyOutbidIds.forEach(id => updated.add(id));
                            return updated;
                        });
                        
                        newlyOutbidIds.forEach(id => {
                            setTimeout(() => {
                                setOutbidItems(curr => {
                                    const updated = new Set(curr);
                                    updated.delete(id);
                                    return updated;
                                });
                            }, 3000);
                        });
                    }
                }
                
                prevItemsRef.current = newItems;
                setItems(newItems);
            } catch (err) {
                if (isMounted) setError(err.message);
            } finally {
                if (isMounted) setLoading(false);
            }
        };

        loadCatalogue();
        const intervalId = setInterval(loadCatalogue, 3000); // Poll every 3s to get real-time outbid updates
        
        return () => {
            isMounted = false;
            clearInterval(intervalId);
        };
    }, [accountUID]);

    const handleSearch = (e) => {
        setSearchQuery(e.target.value);
    };

    const handleBidClick = (itemId) => {
        navigate(`/bid/${itemId}`);
    };

    const filteredItems = items.filter(item => {
        const query = searchQuery.toLowerCase();
        const nameMatch = item.name && item.name.toLowerCase().includes(query);
        const descMatch = item.description && item.description.toLowerCase().includes(query);
        return nameMatch || descMatch;
    });

    return (
        <div className="dashboard-page">
            <Navbar />

            <main className="dashboard-content">
                <div className="dashboard-hero">
                    <span className="dashboard-tag">Auction Marketplace</span>
                    <h1>Welcome to GlobalConnect Bid</h1>
                </div>

                <div className="catalogue-section">
                    <div className="search-bar">
                        <input 
                            type="text" 
                            placeholder="Search by name or description..." 
                            value={searchQuery}
                            onChange={handleSearch}
                            className="search-input"
                        />
                    </div>

                    {loading && <p>Loading catalogue...</p>}
                    {error && <p className="error-text">Error: {error}</p>}

                    {!loading && !error && (
                        <div className="items-grid">
                            {filteredItems.length > 0 ? (
                                filteredItems.map(item => {
                                    const isWinning = item.highestBidderUid === accountUID;
                                    const isOutbid = outbidItems.has(item.id);
                                    let cardClass = "item-card";
                                    if (isOutbid) {
                                        cardClass += " outbid";
                                    } else if (isWinning) {
                                        cardClass += " winning-bid";
                                    }
                                    
                                    return (
                                        <div key={item.id} className={cardClass}>
                                            <h3>{item.name}</h3>
                                            <p>{item.description}</p>
                                            <div className="item-details">
                                                <span>Current Bid: ${item.currentHighestBid}</span>
                                                {item.closed ? (
                                                    <span className="status closed"> Auction Closed</span>
                                                ) : (
                                                    <span className="status active"> Active</span>
                                                )}
                                            </div>
                                            {isWinning && !item.closed && (
                                                <div className="winning-badge">You are winning!</div>
                                            )}
                                            <button 
                                                onClick={() => handleBidClick(item.id)}
                                                disabled={item.closed || item.ownerUid === accountUID}
                                                className="bid-button"
                                                title={item.ownerUid === accountUID ? "You cannot bid on your own item" : ""}
                                            >
                                                {item.ownerUid === accountUID ? "Your Item" : "Place Bid"}
                                            </button>
                                        </div>
                                    );
                                })
                            ) : (
                                <p>No items found matching your search.</p>
                            )}
                        </div>
                    )}
                </div>
            </main>
        </div>
    );
}