import { useMemo, useState } from "react";
import SmartBidAdvisorPanel from "./SmartBidAdvisorPanel";
import "./smartBidAdvisorTestPage.css";

const mockItems = [
    {
        id: 101,
        name: "Sony WH-1000XM5 Headphones",
        description: "Used for 3 months, excellent condition, includes case and cables.",
        startingPrice: 180,
        currentHighestBid: 246.5,
        highestBidderUid: 9002,
        closed: false,
    },
    {
        id: 102,
        name: "Lenovo ThinkPad T14",
        description: "16GB RAM, 512GB SSD, lightly used for class projects.",
        startingPrice: 420,
        currentHighestBid: 590,
        highestBidderUid: 9005,
        closed: false,
    },
    {
        id: 103,
        name: "Nintendo Switch OLED",
        description: "Console only, no drift, one pro controller included.",
        startingPrice: 220,
        currentHighestBid: 310,
        highestBidderUid: 9010,
        closed: true,
    },
];

const mockBidHistoryByItem = {
    101: [
        { bidderUID: 9001, bidAmount: 190, bidTime: Date.now() - 1000 * 60 * 28 },
        { bidderUID: 9002, bidAmount: 215, bidTime: Date.now() - 1000 * 60 * 18 },
        { bidderUID: 9004, bidAmount: 235, bidTime: Date.now() - 1000 * 60 * 7 },
        { bidderUID: 9002, bidAmount: 246.5, bidTime: Date.now() - 1000 * 60 * 2 },
    ],
    102: [
        { bidderUID: 9003, bidAmount: 450, bidTime: Date.now() - 1000 * 60 * 40 },
        { bidderUID: 9005, bidAmount: 500, bidTime: Date.now() - 1000 * 60 * 25 },
        { bidderUID: 9007, bidAmount: 560, bidTime: Date.now() - 1000 * 60 * 9 },
        { bidderUID: 9005, bidAmount: 590, bidTime: Date.now() - 1000 * 60 * 4 },
    ],
    103: [
        { bidderUID: 9008, bidAmount: 250, bidTime: Date.now() - 1000 * 60 * 60 },
        { bidderUID: 9010, bidAmount: 280, bidTime: Date.now() - 1000 * 60 * 43 },
        { bidderUID: 9010, bidAmount: 310, bidTime: Date.now() - 1000 * 60 * 30 },
    ],
};

export default function SmartBidAdvisorTestPage() {
    const [selectedItemId, setSelectedItemId] = useState(String(mockItems[0].id));
    const [manualApiKey, setManualApiKey] = useState("");
    const [lastStrategy, setLastStrategy] = useState(null);

    const selectedItem = useMemo(
        () => mockItems.find((item) => String(item.id) === selectedItemId) || null,
        [selectedItemId]
    );

    const selectedBidHistory = useMemo(() => {
        if (!selectedItem) {
            return [];
        }
        return mockBidHistoryByItem[selectedItem.id] || [];
    }, [selectedItem]);

    return (
        <main className="sba-test-page">
            <section className="sba-test-toolbar">
                <h1>Smart Bid Advisor Sandbox</h1>
                <p>
                    Use this standalone page to validate the advisor before wiring it into a real bidding page.
                </p>

                <label htmlFor="sba-test-item">Mock item</label>
                <select
                    id="sba-test-item"
                    value={selectedItemId}
                    onChange={(event) => setSelectedItemId(event.target.value)}
                >
                    {mockItems.map((item) => (
                        <option key={item.id} value={String(item.id)}>
                            #{item.id} {item.name}
                        </option>
                    ))}
                </select>

                <label htmlFor="sba-test-key">Gemini API key (optional override)</label>
                <input
                    id="sba-test-key"
                    type="password"
                    value={manualApiKey}
                    placeholder="Leave empty to use VITE_GEMINI_API_KEY"
                    onChange={(event) => setManualApiKey(event.target.value)}
                />

                <p className="sba-test-note">
                    This test page does not submit bids. It only calls Gemini for strategy generation.
                </p>
            </section>

            <section className="sba-test-panel-wrap">
                <SmartBidAdvisorPanel
                    item={selectedItem}
                    bidHistory={selectedBidHistory}
                    geminiApiKey={manualApiKey || undefined}
                    onStrategyGenerated={(strategy) => setLastStrategy(strategy)}
                />
            </section>

            {lastStrategy && (
                <section className="sba-test-debug">
                    <h2>Last Strategy JSON</h2>
                    <pre>{JSON.stringify(lastStrategy, null, 2)}</pre>
                </section>
            )}
        </main>
    );
}
