const API_BASE = "http://localhost:8083/api/payment";

const PAYMENT_ERROR_MESSAGES = {
    1: "Some required payment information is missing.",
    2: "Payment service is temporarily unavailable. Please try again.",
    4: "Your session is no longer valid. Please log in again.",
    5: "Payment authorization failed. Please check your payment method.",
    6: "Invalid payment request. Please refresh and try again.",
    7: "Payment details are invalid or incomplete. Please review all fields and try again.",
};

function getPaymentErrorMessage(errorCode, fallbackMessage) {
    return (
        PAYMENT_ERROR_MESSAGES[errorCode] ||
        fallbackMessage ||
        "Something went wrong while processing your payment request."
    );
}

async function handleResponse(response) {
    let data = null;

    try {
        data = await response.json();
    } catch {
        data = null;
    }

    if (!response.ok) {
        const message =
            data?.message ||
            data?.error ||
            `Request failed with status ${response.status}`;
        throw new Error(message);
    }

    if (typeof data?.errorCode === "number" && data.errorCode !== 0) {
        throw new Error(getPaymentErrorMessage(data.errorCode, data?.message));
    }

    return data;
}

async function post(path, body) {
    const response = await fetch(`${API_BASE}${path}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
    });

    return handleResponse(response);
}

function buildAuthenticatedRequest(secret, request) {
    return {
        secret,
        request,
    };
}

export async function pay(secret, payload) {
    return post(
        "/pay",
        buildAuthenticatedRequest(secret, {
            requestType: "PayRequest",
            accountUID: Number(payload.accountUID),
            itemId: Number(payload.itemId),
            cardNumber: payload.cardNumber,
            name: payload.name,
            expDate: payload.expDate,
            securityCode: payload.securityCode,
            paymentMethod: payload.paymentMethod,
            amount: Number(payload.amount),
            shippingCost: Number(payload.shippingCost),
            expeditedShipping: Boolean(payload.expeditedShipping),
            expeditedExtraCost: Number(payload.expeditedExtraCost),
        })
    );
}

export async function fetchReceipts(secret, accountUID) {
    return post(
        "/fetchreceipts",
        buildAuthenticatedRequest(secret, {
            requestType: "FetchReceiptsRequest",
            accountUID: Number(accountUID),
        })
    );
}

export async function fetchPendingPayments(secret, accountUID) {
    return post(
        "/fetchpendingpayments",
        buildAuthenticatedRequest(secret, {
            requestType: "FetchPendingPaymentsRequest",
            accountUID: Number(accountUID),
        })
    );
}

export async function registerPending(secret, accountUID, itemId) {
    return post(
        "/register-pending",
        buildAuthenticatedRequest(secret, {
            requestType: "PayRequest",
            accountUID: Number(accountUID),
            itemId: Number(itemId),
        })
    );
}