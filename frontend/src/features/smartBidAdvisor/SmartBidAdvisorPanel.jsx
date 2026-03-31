import { useMemo, useState } from "react";
import { generateBidStrategy } from "./geminiBidAdvisorClient";
import "./smartBidAdvisor.css";

function toCurrency(value) {
    const number = Number(value);
    if (!Number.isFinite(number)) {
        return "N/A";
    }
    return `$${number.toFixed(2)}`;
}

export default function SmartBidAdvisorPanel({
    item,
    bidHistory = [],
    auctionContext,
    geminiApiKey,
    onStrategyGenerated,
    compact = false,
    className = "",
}) {
    const [targetMaxPrice, setTargetMaxPrice] = useState("");
    const [idealPrice, setIdealPrice] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [strategy, setStrategy] = useState(null);

    const currentHighestBid = useMemo(
        () => Number(item?.currentHighestBid ?? item?.startingPrice ?? 0),
        [item]
    );

    async function handleGenerate() {
        setError("");
        setStrategy(null);

        const target = Number(targetMaxPrice);
        const ideal = Number(idealPrice);

        if (!item) {
            setError("Choose an item first.");
            return;
        }

        if (!Number.isFinite(target) || target <= 0) {
            setError("Target max price must be a positive number.");
            return;
        }

        if (!Number.isFinite(ideal) || ideal <= 0) {
            setError("Ideal price must be a positive number.");
            return;
        }

        if (ideal > target) {
            setError("Ideal price should not exceed target max price.");
            return;
        }

        setLoading(true);

        try {
            const result = await generateBidStrategy({
                item,
                bidHistory,
                auctionContext,
                targetMaxPrice: target,
                idealPrice: ideal,
                apiKey: geminiApiKey,
            });

            setStrategy(result);
            if (typeof onStrategyGenerated === "function") {
                onStrategyGenerated(result);
            }
        } catch (requestError) {
            setError(requestError?.message || "Failed to generate strategy.");
        } finally {
            setLoading(false);
        }
    }

    const panelClass = ["sba-panel", compact ? "sba-compact" : "", className]
        .filter(Boolean)
        .join(" ");

    return (
        <aside className={panelClass} aria-label="Smart Bid Advisor">
            <div className="sba-two-pane">
                <div className="sba-left-pane">
                    <div className="sba-header">
                        <p className="sba-badge">AI Assistant</p>
                        <h2>Smart Bid Advisor</h2>
                        <p>
                            Enter your budget targets and get a practical bidding plan before placing bids.
                        </p>
                    </div>

                    <section className="sba-item">
                        <h3>{item?.name || "No item selected"}</h3>
                        <p>{item?.description || "Select an auction item to analyze."}</p>
                        <div className="sba-chips">
                            <span>Current: {toCurrency(currentHighestBid)}</span>
                            <span>Start: {toCurrency(item?.startingPrice)}</span>
                            <span>Status: {item?.closed ? "Closed" : "Open"}</span>
                            {auctionContext?.timeLeftLabel && <span>Time Left: {auctionContext.timeLeftLabel}</span>}
                            {typeof auctionContext?.totalBids === "number" && <span>Total Bids: {auctionContext.totalBids}</span>}
                        </div>
                    </section>

                    <section className="sba-form">
                        <label htmlFor="sba-target-max">Target max price ($)</label>
                        <input
                            id="sba-target-max"
                            type="number"
                            min="0"
                            step="0.01"
                            value={targetMaxPrice}
                            onChange={(event) => setTargetMaxPrice(event.target.value)}
                            placeholder="e.g. 350"
                        />

                        <label htmlFor="sba-ideal">Ideal winning price ($)</label>
                        <input
                            id="sba-ideal"
                            type="number"
                            min="0"
                            step="0.01"
                            value={idealPrice}
                            onChange={(event) => setIdealPrice(event.target.value)}
                            placeholder="e.g. 290"
                        />

                        <button
                            type="button"
                            className="sba-btn"
                            onClick={handleGenerate}
                            disabled={loading || !item || item?.closed}
                        >
                            {loading ? "Analyzing..." : "Generate Strategy"}
                        </button>

                        {error && <p className="sba-error">{error}</p>}
                    </section>
                </div>

                <section className="sba-result sba-right-pane">
                    <h3>Strategy Breakdown</h3>

                    {!strategy && (
                        <p className="sba-placeholder">
                            Enter your AI bidding agent request to generate your strategy!
                        </p>
                    )}

                    {strategy && (
                        <>
                            <p className="sba-summary">{strategy.summary}</p>

                            <div className="sba-chips">
                                <span>Opening: {toCurrency(strategy.recommendedOpeningBid)}</span>
                                <span>Cap: {toCurrency(strategy.maxBidCap)}</span>
                                <span>Confidence: {strategy.confidence}</span>
                            </div>

                            <h4>Timing Plan</h4>
                            <ul>
                                {strategy.timingPlan.map((step, index) => (
                                    <li key={`timing-${index}`}>{step}</li>
                                ))}
                            </ul>

                            <h4>Risk Warnings</h4>
                            <ul>
                                {strategy.riskWarnings.map((warning, index) => (
                                    <li key={`risk-${index}`}>{warning}</li>
                                ))}
                            </ul>
                        </>
                    )}
                </section>
            </div>
        </aside>
    );
}
