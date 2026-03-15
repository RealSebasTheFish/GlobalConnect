CREATE TABLE IF NOT EXISTS items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    owner_uid INTEGER NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    starting_price REAL NOT NULL,
    current_highest_bid REAL NOT NULL,
    highest_bidder_uid INTEGER,
    is_closed INTEGER DEFAULT 0
);