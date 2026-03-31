export function buildBidAdvisorContextFromItem(item) {
    if (!item) {
        return null;
    }

    return {
        id: Number(item.id),
        name: item.name || "",
        description: item.description || "",
        startingPrice: Number(item.startingPrice ?? 0),
        currentHighestBid: Number(item.currentHighestBid ?? item.startingPrice ?? 0),
        highestBidderUid: item.highestBidderUid ?? null,
        closed: Boolean(item.closed),
    };
}

export function buildBidHistoryContext(bidHistory = []) {
    if (!Array.isArray(bidHistory)) {
        return [];
    }

    return bidHistory.map((entry) => ({
        bidAmount: Number(entry?.bidAmount ?? entry?.amount ?? 0),
        bidderUID: entry?.bidderUID ?? entry?.bidderUid ?? null,
        bidTime: entry?.bidTime ?? null,
    }));
}
