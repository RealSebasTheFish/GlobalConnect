# GlobalConnect Backend

EECS 4413 – Group Project  
Milestone 2

## Overview

GlobalConnect is a REST-based backend system that implements the core functionality of an auction-based e-commerce platform.

The system allows users to:

- create accounts and authenticate
- list items for auction
- browse an item catalogue
- place bids on items
- complete payments
- ship purchased items
- reset passwords

The backend is organized as multiple microservices connected through a central Gateway.

All client requests are sent to the Gateway, which routes them to the appropriate microservice.

---

## System Architecture

The system consists of four services:

- **GatewayManager** – API entry point
- **AccountManager** – account management and authentication
- **AuctionManager** – auction and item management
- **PaymentHandler** – payments, receipts, and shipping verification

Architecture diagram:

```text
Client
  |
  v
GatewayManager (Port 8080)
  |
  |--- AccountManager (Port 8081)
  |--- AuctionManager (Port 8082)
  |--- PaymentHandler (Port 8083)
```

---

## Technologies Used

| Component | Technology |
|-----------|------------|
| Backend Framework | Spring Boot |
| Language | Java |
| Database | SQLite |
| Build Tool | Maven |
| API Testing | Postman |

---

## Installation

### Prerequisites

Install the following:

- Java JDK 17 or higher
- Maven
- Postman (for testing)

### Clone the Repository

```bash
git clone <repository-url>
cd GlobalConnect
```

### Build the Project

Each service is built independently using Maven.

Example:

```bash
cd GatewayManager
mvn clean install
```

Repeat for:

- `AccountManager`
- `AuctionManager`
- `PaymentHandler`

---

## Running the System

Each microservice must run in a separate terminal.

### 1. Start AccountManager

```bash
cd AccountManager
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8081
```

### 2. Start AuctionManager

```bash
cd AuctionManager
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8082
```

### 3. Start PaymentHandler

```bash
cd PaymentHandler
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8083
```

### 4. Start GatewayManager

```bash
cd GatewayManager
mvn spring-boot:run
```

Runs on:

```text
http://localhost:8080
```

All API requests must be sent to the Gateway.

---

## API Endpoints

| Endpoint | Description |
|---------|-------------|
| `POST /signup` | Create a new account |
| `POST /login` | Login and generate session token |
| `POST /authenticate` | Verify authentication token |
| `POST /fetchaccount` | Retrieve account information |
| `POST /additem` | Add new item listing |
| `PUT /modifyitem` | Modify an existing item |
| `DELETE /removeitem` | Remove an item |
| `POST /fetchcatalogue` | Retrieve catalogue items |
| `POST /fetchitems` | Retrieve items owned by user |
| `POST /bid` | Place bid on an item |
| `POST /pay` | Complete payment |
| `POST /ship` | Ship item |
| `POST /fetchpendingpayments` | List pending payments |
| `POST /fetchreceipts` | Retrieve payment receipts |
| `POST /resetpassword/request` | Request password reset |
| `POST /resetpassword` | Complete password reset |

---

## Running the Test Suite

The project includes comprehensive Postman test suites that demonstrate the main use cases and robustness tests.

Import the following collections into Postman:

```text
YorkU_E2E_Test_Suite_V3.json
YorkU Gateway E2E Test Suite V3.postman_collection.json
GlobalConnect_Gateway_8080_Postman_Collection_v4_spec_regression.json
GlobalConnect_Gateway_8080_Postman_Collection_v5_strict.json
```

### Running the Tests

1. Open Postman
2. Click **Import**
3. Import the provided Postman collection
4. Run the collection

The test suite automatically:

- creates test accounts
- logs users in
- lists items
- places bids
- processes payments
- performs shipping
- tests authentication
- tests invalid user inputs

The tests are rerunnable and generate unique users for each run.

---

## Example Main Flow

1. Seller creates account
2. Buyer creates account
3. Seller lists an item for auction
4. Buyer browses catalogue
5. Buyer places a bid
6. Buyer wins auction
7. Buyer completes payment
8. Seller ships item
9. Buyer receives receipt

---

## Robustness Testing

The test suite includes validation for incorrect inputs, including:

- invalid authentication tokens
- missing request parameters
- invalid item IDs
- invalid bids
- unauthorized modification attempts
- invalid payment details

These tests verify the system's resilience to incorrect user inputs.

---

## Database

SQLite is used for persistence. Databases are created automatically when services start.

### AccountManager tables

```text
accounts
sessions
password_resets
```

### AuctionManager tables

```text
items
bids
```

### PaymentHandler tables

```text
pending_payments
receipts
```

---

## AI Usage

AI tools were used during development for:

- debugging assistance
- generating Postman test suites
- validating REST API behaviors
- improving documentation

### Advantages

- faster debugging
- improved testing coverage
- better documentation generation

### Disadvantages

- generated solutions required manual validation
- AI suggestions sometimes needed adaptation to match the system architecture

All code was reviewed and integrated manually by the development team.

---

## Repository Structure

```text
GlobalConnect/
├── GatewayManager
├── AccountManager
├── AuctionManager
├── PaymentHandler
├── postman/
│   ├── GlobalConnect_Gateway_8080_Postman_Collection_v4_spec_regression.json
│   └── GlobalConnect_Gateway_8080_Postman_Collection_v5_strict.json
└── README.md
```

---

## Notes for TA / Instructor

To run the system:

1. Start all four services
2. Import the Postman test collection
3. Run the collection

All API requests should be sent through:

```text
http://localhost:8080
```
