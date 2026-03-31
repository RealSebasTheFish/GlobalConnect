const DEFAULT_GEMINI_MODEL = import.meta.env.VITE_GEMINI_MODEL || "gemini-2.5-flash";

function cleanJsonText(text) {
    return text
        .trim()
        .replace(/^```json\s*/i, "")
        .replace(/^```\s*/i, "")
        .replace(/```$/i, "")
        .trim();
}

function summarizeBidHistory(bidHistory) {
    if (!Array.isArray(bidHistory) || bidHistory.length === 0) {
        return "No bid history was provided.";
    }

    return bidHistory.slice(-12).map((entry) => ({
        bidderUID: entry?.bidderUID ?? entry?.bidderUid ?? null,
        bidAmount: Number(entry?.bidAmount ?? entry?.amount ?? 0),
        bidTime: entry?.bidTime ?? null,
    }));
}

function toMillis(value) {
    const num = Number(value);
    if (!Number.isFinite(num)) {
        return null;
    }
    return num < 1000000000000 ? num * 1000 : num;
}

function buildBidSignals(bidHistory) {
    if (!Array.isArray(bidHistory) || bidHistory.length === 0) {
        return {
            totalBids: 0,
            uniqueBidders: 0,
            recentBidCount: 0,
            latestBidAmount: null,
            averageIncrement: null,
        };
    }

    const cleaned = bidHistory
        .map((entry) => ({
            bidderUID: entry?.bidderUID ?? entry?.bidderUid ?? null,
            bidAmount: Number(entry?.bidAmount ?? entry?.amount ?? 0),
            bidTime: toMillis(entry?.bidTime),
        }))
        .filter((entry) => Number.isFinite(entry.bidAmount) && entry.bidAmount > 0)
        .sort((a, b) => (a.bidTime ?? 0) - (b.bidTime ?? 0));

    const increments = [];
    for (let i = 1; i < cleaned.length; i += 1) {
        increments.push(cleaned[i].bidAmount - cleaned[i - 1].bidAmount);
    }

    const lastTs = cleaned[cleaned.length - 1]?.bidTime;
    const recentWindowMs = 15 * 60 * 1000;
    const recentBidCount = lastTs
        ? cleaned.filter((entry) => (entry.bidTime ?? 0) >= lastTs - recentWindowMs).length
        : 0;

    return {
        totalBids: cleaned.length,
        uniqueBidders: new Set(cleaned.map((entry) => entry.bidderUID).filter((id) => id !== null)).size,
        recentBidCount,
        latestBidAmount: cleaned[cleaned.length - 1]?.bidAmount ?? null,
        averageIncrement: increments.length
            ? increments.reduce((sum, inc) => sum + inc, 0) / increments.length
            : null,
    };
}

function buildPrompt({ item, bidHistory, targetMaxPrice, idealPrice, auctionContext }) {
    const normalizedBidHistory = summarizeBidHistory(bidHistory);
    const promptPayload = {
        userBudget: {
            targetMaxPrice: Number(targetMaxPrice),
            idealPrice: Number(idealPrice),
        },
        item: {
            id: item?.id ?? null,
            name: item?.name ?? "Unknown Item",
            description: item?.description ?? "",
            startingPrice: Number(item?.startingPrice ?? 0),
            currentHighestBid: Number(item?.currentHighestBid ?? 0),
            closed: Boolean(item?.closed),
            highestBidderUid: item?.highestBidderUid ?? null,
            auctionEndTime: toMillis(item?.auctionEndTime),
        },
        liveAuctionContext: {
            timeLeftLabel: auctionContext?.timeLeftLabel ?? null,
            timeLeftMs: Number(auctionContext?.timeLeftMs ?? 0),
            myLatestBidAmount: Number(auctionContext?.myLatestBidAmount ?? 0),
            myIsCurrentHighestBidder: Boolean(auctionContext?.myIsCurrentHighestBidder),
        },
        bidHistorySignals: buildBidSignals(normalizedBidHistory),
        bidHistory: normalizedBidHistory,
    };

    return [
        "You are an auction bidding strategy assistant.",
        "Return strict JSON only with no markdown.",
        "Use exactly this schema:",
        "{",
        '  "summary": "string",',
        '  "recommendedOpeningBid": number,',
        '  "maxBidCap": number,',
        '  "timingPlan": ["string"],',
        '  "riskWarnings": ["string"],',
        '  "confidence": "low|medium|high"',
        "}",
        "Rules:",
        "- Never exceed userBudget.targetMaxPrice.",
        "- recommendedOpeningBid must be <= maxBidCap.",
        "- Include at least 4 timingPlan steps when enough data exists.",
        "- If data is missing, mention that in riskWarnings.",
        "- Keep timing steps concrete and short.",
        "- Use liveAuctionContext.timeLeftMs and bidHistorySignals.recentBidCount to tune urgency.",
        "Context:",
        JSON.stringify(promptPayload),
    ].join("\n");
}

function extractCandidateText(data) {
    return (
        data?.candidates?.[0]?.content?.parts
            ?.map((part) => part?.text || "")
            .join("\n") || ""
    );
}

function normalizeConfidence(value) {
    const normalized = String(value || "").toLowerCase();
    if (["low", "medium", "high"].includes(normalized)) {
        return normalized;
    }
    return "medium";
}

function normalizeStrategy(raw, targetMaxPrice) {
    const cap = Number(targetMaxPrice);
    const maxBidCap = Math.min(Number(raw?.maxBidCap ?? cap), cap);
    const recommendedOpeningBid = Math.min(Number(raw?.recommendedOpeningBid ?? maxBidCap), maxBidCap);

    return {
        summary: String(raw?.summary || "No summary provided."),
        recommendedOpeningBid: Number.isFinite(recommendedOpeningBid) ? recommendedOpeningBid : 0,
        maxBidCap: Number.isFinite(maxBidCap) ? maxBidCap : 0,
        timingPlan: Array.isArray(raw?.timingPlan) ? raw.timingPlan.map(String) : [],
        riskWarnings: Array.isArray(raw?.riskWarnings) ? raw.riskWarnings.map(String) : [],
        confidence: normalizeConfidence(raw?.confidence),
    };
}

export async function generateBidStrategy({
    item,
    bidHistory = [],
    auctionContext = {},
    targetMaxPrice,
    idealPrice,
    apiKey = import.meta.env.VITE_GEMINI_API_KEY,
    model = DEFAULT_GEMINI_MODEL,
}) {
    if (!apiKey) {
        throw new Error("Missing Gemini API key. Set VITE_GEMINI_API_KEY or pass apiKey.");
    }

    if (!item) {
        throw new Error("Item is required to generate a strategy.");
    }

    const endpoint = `https://generativelanguage.googleapis.com/v1beta/models/${model}:generateContent?key=${apiKey}`;

    const response = await fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            contents: [
                {
                    role: "user",
                    parts: [{ text: buildPrompt({ item, bidHistory, targetMaxPrice, idealPrice, auctionContext }) }],
                },
            ],
            generationConfig: {
                temperature: 0.35,
                responseMimeType: "application/json",
            },
        }),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data?.error?.message || "Gemini request failed.");
    }

    const text = extractCandidateText(data);
    if (!text) {
        throw new Error("Gemini returned an empty response.");
    }

    let parsed;
    try {
        parsed = JSON.parse(cleanJsonText(text));
    } catch {
        throw new Error("Gemini did not return valid JSON.");
    }

    return normalizeStrategy(parsed, targetMaxPrice);
}

export { DEFAULT_GEMINI_MODEL };
