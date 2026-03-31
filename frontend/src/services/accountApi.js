const API_BASE = "http://localhost:8080"; // Call the GatewayManager instead

async function handleResponse(response) {
    let data = null;

    try {
        data = await response.json();

        if (data && data.account && data.account.address) {
            data.account = {...data.account, ...data.account.address};
        };
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
    payload.address = {
        "city" : payload.city,
        "country" : payload.country,
        "postalCode" : payload.postalCode,
        "streetName" : payload.streetName,
        "streetNumber" : payload.streetNumber
    };

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
            request: { accountUID: Number(accountUID) },
        }),
    });

    return handleResponse(response);
}

export async function fetchAccount(sessionToken, accountUID) {
    const response = await fetch(`${API_BASE}/fetchaccount`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            sessionToken,
            accountUID: Number(accountUID),
        }),
    });

    return handleResponse(response);
}

export async function startForgotPassword(accountUID) {
    const response = await fetch(`${API_BASE}/resetpassword/request`, {
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
    const response = await fetch(`${API_BASE}/resetpassword`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}