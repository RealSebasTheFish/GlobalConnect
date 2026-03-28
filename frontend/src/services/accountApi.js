const API_BASE = "http://localhost:8081";

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

    return data;
}

export async function signup(payload) {
    const response = await fetch(`${API_BASE}/signup`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}

export async function login(payload) {
    const response = await fetch(`${API_BASE}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}

export async function authenticate(sessionToken, accountUID) {
    const response = await fetch(`${API_BASE}/authenticate`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            sessionToken,
            request: {
                accountUID: Number(accountUID),
            },
        }),
    });

    return handleResponse(response);
}

export async function fetchAccount(authenticatedRequest) {
    const response = await fetch(`${API_BASE}/fetchaccount`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(authenticatedRequest),
    });

    return handleResponse(response);
}

export async function startForgotPassword(accountUID) {
    const response = await fetch(`${API_BASE}/forgotpassword/request`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            accountUID: Number(accountUID),
        }),
    });

    return handleResponse(response);
}

export async function resetPassword(payload) {
    const response = await fetch(`${API_BASE}/forgotpassword`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            accountUID: Number(payload.accountUID),
            forgotPasswordRescueCode: payload.forgotPasswordRescueCode,
            newPassword: payload.newPassword,
        }),
    });

    return handleResponse(response);
}