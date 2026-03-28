const SESSION_TOKEN_KEY = "gc_session_token";
const ACCOUNT_UID_KEY = "gc_account_uid";

export function saveSession(sessionToken, accountUID) {
    localStorage.setItem(SESSION_TOKEN_KEY, sessionToken);
    localStorage.setItem(ACCOUNT_UID_KEY, String(accountUID));
}

export function getSessionToken() {
    return localStorage.getItem(SESSION_TOKEN_KEY);
}

export function getAccountUID() {
    return localStorage.getItem(ACCOUNT_UID_KEY);
}

export function clearSession() {
    localStorage.removeItem(SESSION_TOKEN_KEY);
    localStorage.removeItem(ACCOUNT_UID_KEY);
}