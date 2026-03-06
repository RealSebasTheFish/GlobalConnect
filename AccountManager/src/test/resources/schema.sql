CREATE TABLE IF NOT EXISTS accounts (
  accountUID INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT NOT NULL UNIQUE,
  email TEXT,
  hashedPassword TEXT NOT NULL,

  firstName TEXT,
  lastName TEXT,

  streetName TEXT,
  streetNumber TEXT,
  city TEXT,
  country TEXT,
  postalCode TEXT
);

CREATE TABLE IF NOT EXISTS sessions (
  sessionToken TEXT PRIMARY KEY,
  accountUID INTEGER NOT NULL,
  createdAt INTEGER NOT NULL,
  FOREIGN KEY(accountUID) REFERENCES accounts(accountUID)
);

CREATE TABLE IF NOT EXISTS password_resets (
  accountUID INTEGER PRIMARY KEY,
  rescueCode TEXT NOT NULL,
  createdAt INTEGER NOT NULL,
  FOREIGN KEY(accountUID) REFERENCES accounts(accountUID)
);