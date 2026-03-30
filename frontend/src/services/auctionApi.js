const API_BASE = "http://localhost:8080"; // The gateway runs on port 8080

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
        throw new Error(data?.message || "Catalogue request failed.");
    }

    return data;
}

export async function fetchCatalogue() {
    const response = await fetch(`${API_BASE}/fetchcatalogue`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
    });

    return handleResponse(response);
}

export async function fetchUserItems(payload) {
    const response = await fetch(`${API_BASE}/fetchitems`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}

export async function addItem(payload) {
    const response = await fetch(`${API_BASE}/additem`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}

export async function modifyItem(payload) {
    const response = await fetch(`${API_BASE}/modifyitem`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}

export async function removeItem(payload) {
    const response = await fetch(`${API_BASE}/removeitem`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}

export async function placeBid(payload) {
    const response = await fetch(`${API_BASE}/bid`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    return handleResponse(response);
}