const API_BASE = "http://localhost:8082/api/auction";

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
    const response = await fetch(`${API_BASE}/catalogue`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });

    return handleResponse(response);
}