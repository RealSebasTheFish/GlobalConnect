CREATE TABLE IF NOT EXISTS accounts (
    accountUID INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255),
    hashedPassword VARCHAR(255) NOT NULL,

    firstName VARCHAR(100),
    lastName VARCHAR(100),

    streetName VARCHAR(255),
    streetNumber VARCHAR(50),
    city VARCHAR(100),
    country VARCHAR(100),
    postalCode VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS sessions (
    sessionToken VARCHAR(255) PRIMARY KEY,
    accountUID INTEGER NOT NULL,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY (accountUID) REFERENCES accounts(accountUID)
);

CREATE TABLE IF NOT EXISTS password_resets (
    accountUID INTEGER PRIMARY KEY,
    rescueCode VARCHAR(255) NOT NULL,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY (accountUID) REFERENCES accounts(accountUID)
);