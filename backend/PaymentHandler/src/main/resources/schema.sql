CREATE TABLE IF NOT EXISTS pending_payments (
    account_uid INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    PRIMARY KEY (account_uid, item_id)
);

CREATE TABLE IF NOT EXISTS receipts (
    account_uid INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    payment_method TEXT NOT NULL,
    date TEXT NOT NULL,
    shipping_cost REAL NOT NULL,
    expedited_shipping INTEGER NOT NULL,
    expedited_extra_cost REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_methods (
    account_uid INTEGER NOT NULL,
    payment_method TEXT NOT NULL,
    cardholder_name TEXT,
    last4 TEXT,
    exp_date TEXT,
    PRIMARY KEY (account_uid, payment_method)
);