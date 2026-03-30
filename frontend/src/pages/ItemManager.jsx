import { useState, useEffect } from "react";
import { fetchUserItems, addItem, modifyItem, removeItem } from "../services/auctionApi";
import { getAccountUID, getSessionToken } from "../utils/storage";
import Navbar from "../components/Navbar";
import "../styles/itemManager.css";

export default function ItemManager() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [isEditing, setIsEditing] = useState(false);

    const accountUID = Number(getAccountUID());
    const sessionToken = getSessionToken();

    const [formData, setFormData] = useState({
        id: null,
        name: "",
        description: "",
        startingPrice: 0,
        duration: 5, // Default auction duration in minutes
        launchImmediately: false // New field for the checkbox
    });

    const loadItems = async () => {
        try {
            setLoading(true);
            const data = await fetchUserItems({ sessionToken, accountUID });
            const allItems = Array.isArray(data) ? data : data.items || [];
            // Assuming fetchUserItems only returns items for this user 
            // (or if it delegates properly, it's filtered)
            setItems(allItems);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadItems();
    }, [accountUID]);

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({ 
            ...formData, 
            [name]: type === "checkbox" ? checked : (name === "startingPrice" || name === "duration" ? Number(value) : value) 
        });
    };

    const handleAddOrEdit = async (e) => {
        e.preventDefault();
        try {
            const timeInMs = formData.launchImmediately ? Date.now() + (formData.duration * 60 * 1000) : 0;
            
            if (isEditing) {
                await modifyItem({
                    sessionToken,
                    accountUID,
                    item: {
                        id: formData.id,
                        name: formData.name,
                        description: formData.description,
                        startingPrice: formData.startingPrice,
                        ownerUid: accountUID,
                        closed: !formData.launchImmediately,
                        auctionEndTime: timeInMs
                    }
                });
            } else {
                await addItem({
                    sessionToken,
                    accountUID,
                    item: { 
                        name: formData.name,
                        description: formData.description,
                        startingPrice: formData.startingPrice,
                        ownerUid: accountUID,
                        closed: !formData.launchImmediately,
                        auctionEndTime: timeInMs
                    }
                });
            }
            setFormData({ id: null, name: "", description: "", startingPrice: 0, duration: 5, launchImmediately: false });
            setIsEditing(false);
            loadItems();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleActivate = async (item) => {
        try {
            const timeInMs = Date.now() + (formData.duration * 60 * 1000);
            
            await modifyItem({
                sessionToken,
                accountUID,
                item: {
                    ...item,
                    closed: false,
                    auctionEndTime: timeInMs
                }
            });
            loadItems();
        } catch (err) {
            setError(err.message);
        }
    };

    const handleEditClick = (item) => {
        setIsEditing(true);
        setFormData({
            id: item.id,
            name: item.name,
            description: item.description,
            startingPrice: item.startingPrice,
            duration: formData.duration, // Preserve the current default or selected duration
            launchImmediately: !item.closed && item.auctionEndTime > Date.now()
        });
    };

    const handleDelete = async (itemId) => {
        try {
            await removeItem({
                sessionToken,
                accountUID,
                itemId
            });
            loadItems();
        } catch (err) {
            setError(err.message);
        }
    };

    const cancelEdit = () => {
        setIsEditing(false);
        setFormData({ id: null, name: "", description: "", startingPrice: 0, duration: 5, launchImmediately: false });
    };

    return (
        <div>
            <Navbar />
            <div className="item-manager-container">
                <h1>Manage Your Items</h1>

                {error && <div className="error-message">{error}</div>}

                <div className="form-container">
                    <h2>{isEditing ? "Edit Item" : "Add New Item"}</h2>
                    <form onSubmit={handleAddOrEdit}>
                        <div className="form-group">
                            <label>Name:</label>
                            <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
                        </div>
                        <div className="form-group">
                            <label>Description:</label>
                            <textarea name="description" value={formData.description} onChange={handleInputChange} required />
                        </div>
                        <div className="form-group">
                            <label>Starting Price ($):</label>
                            <input type="number" name="startingPrice" value={formData.startingPrice} onChange={handleInputChange} min="0" step="0.01" required />
                        </div>
                        <div className="form-group">
                            <label>Auction Duration (Minutes):</label>
                            <select name="duration" value={formData.duration} onChange={handleInputChange}>
                                {[...Array(60).keys()].map(i => (
                                    <option key={i+1} value={i+1}>{i+1} min</option>
                                ))}
                            </select>
                        </div>
                        <div className="form-group checkbox-group">
                            <label>
                                <input 
                                    type="checkbox" 
                                    name="launchImmediately" 
                                    checked={formData.launchImmediately} 
                                    onChange={handleInputChange} 
                                />
                                Launch Auction Immediately
                            </label>
                            <small className="help-text">If unchecked, the item will be saved as "Not Active" to launch later using the selected duration.</small>
                        </div>
                        <div className="form-actions">
                            <button type="submit" className="submit-btn">{isEditing ? "Update Item" : "Save Item"}</button>
                            {isEditing && <button type="button" onClick={cancelEdit} className="cancel-btn">Cancel</button>}
                        </div>
                    </form>
                </div>

                <div className="items-list">
                    <h2>Your Current Items</h2>
                    {loading ? (
                        <p>Loading items...</p>
                    ) : items.length === 0 ? (
                        <p>You have no items in your portfolio.</p>
                    ) : (
                        <table className="items-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Price</th>
                                    <th>Highest Bid</th>
                                    <th>Time Left</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {items.map(item => {
                                    const isActive = !item.closed && item.auctionEndTime > Date.now();
                                    const isEnded = item.closed || (item.auctionEndTime > 0 && item.auctionEndTime <= Date.now() && !item.closed); // Resolver might lag

                                    let statusText = "Active";
                                    let badgeClass = "status-active";
                                    if (item.closed && item.highestBidderUid === -1 && item.currentHighestBid === item.startingPrice) {
                                        // A heuristic to check if it's new and inactive
                                        statusText = "Not Active";
                                        badgeClass = "status-inactive";
                                    } else if (isEnded) {
                                        statusText = "Ended";
                                        badgeClass = "status-closed";
                                    }

                                    let timeLeftText = "N/A";
                                    if (isActive) {
                                        const minsLeft = Math.ceil((item.auctionEndTime - Date.now()) / 60000);
                                        timeLeftText = `${minsLeft} min(s)`;
                                    } else if (isEnded) {
                                        timeLeftText = "0 min(s)";
                                    }

                                    return (
                                        <tr key={item.id}>
                                            <td>{item.id}</td>
                                            <td>{item.name}</td>
                                            <td>${item.startingPrice.toFixed(2)}</td>
                                            <td>${item.currentHighestBid.toFixed(2)}</td>
                                            <td>{timeLeftText}</td>
                                            <td><span className={`status-badge ${badgeClass}`}>{statusText}</span></td>
                                            <td className="actions-cell">
                                                {statusText === "Not Active" && (
                                                    <>
                                                        <button onClick={() => handleActivate(item)} className="activate-btn">Activate</button>
                                                        <button onClick={() => handleEditClick(item)} className="edit-btn">Edit</button>
                                                        <button onClick={() => handleDelete(item.id)} className="delete-btn">Delete</button>
                                                    </>
                                                )}
                                                {isActive && (
                                                    <span style={{color: '#666', fontSize: '0.85rem'}}>Currently in auction</span>
                                                )}
                                                {isEnded && (
                                                    <span style={{color: '#666', fontSize: '0.85rem'}}>Auction finalized</span>
                                                )}
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </div>
    );
}