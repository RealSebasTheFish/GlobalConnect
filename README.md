# GlobalConnect

GlobalConnect is a microservice-based auction platform with a React frontend, a Spring Boot gateway, and three backend domain services (accounts, auctions, and payments).

This repository version does **not** include startup scripts. The intended workflow is to import and run projects manually (for example, in Eclipse).

## Live Deployment (Docker + Kubernetes)

- Hosted URL: http://34.130.27.225/

Use this URL as the frontend access point for the deployed environment.

## System Architecture

```text
Client (Browser / Postman)
  |
  v
GatewayManager (Port 8080)
  |
  |--- AccountManager (Port 8081)
  |--- AuctionManager (Port 8082)
  |--- PaymentHandler (Port 8083)
```

All client traffic should go through GatewayManager.

## Technologies Used

| Area | Technology |
|------|------------|
| Frontend | React, React Router, Vite |
| Backend | Java 17, Spring Boot 3 |
| Build | Maven (backend), npm (frontend) |
| Database | SQLite |
| API Testing | Postman |
| Deployment | Docker + Kubernetes |

## Current Feature Set

### Frontend (React + Vite)

- Landing page and navigation
- Account flows:
  - sign up
  - login
  - forgot password / reset password
  - account profile view
- Auction flows:
  - live catalogue/dashboard view
  - bid page with near real-time refresh
  - outbid awareness indicators
  - bid history page
- Seller item management:
  - add item
  - edit item
  - remove item
  - launch timing controls
- Payment flows:
  - pending payments page
  - checkout/payment page
  - receipts page
- Smart Bid Advisor integration:
  - AI-assisted bidding guidance via Gemini (optional API key)

### Backend (Spring Boot microservices)

- GatewayManager (port 8080): central API entrypoint and request orchestration
- AccountManager (port 8081):
  - signup
  - login
  - session authentication
  - fetch account
  - forgot password request
  - password reset
- AuctionManager (port 8082):
  - fetch catalogue
  - fetch user items
  - add / modify / remove item
  - bid placement
  - shipping trigger support
- PaymentHandler (port 8083):
  - pay
  - fetch receipts
  - fetch pending payments
  - register pending payment endpoint used by service-to-service flow

### Gateway Routes Exposed to Client

- `GET /health`
- `POST /signup`
- `POST /login`
- `POST /authenticate`
- `POST /resetpassword/request`
- `POST /resetpassword`
- `POST /fetchaccount`
- `POST /fetchcatalogue`
- `POST /fetchitems`
- `POST /additem`
- `PUT /modifyitem`
- `DELETE /removeitem`
- `POST /bid`
- `POST /pay`
- `POST /ship`
- `POST /fetchpendingpayments`
- `POST /fetchreceipts`

## Repository Layout

```text
GlobalConnect/
  backend/
    pom.xml (multi-module parent)
    AccountManager/
    AuctionManager/
    PaymentHandler/
    GatewayManager/
  frontend/
  postman/
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 20+ and npm
- Eclipse IDE for Enterprise Java and Web Developers (recommended)
- Postman
- Git

## Manual Setup and Run (Eclipse-first workflow)

This section is the recommended startup path for this repository snapshot.

### 1. Clone the repository

Use Eclipse import-from-Git or clone first in terminal.

Terminal option:

```bash
git clone <your-repo-url>
cd GlobalConnect
```

### 2. Import backend into Eclipse as Maven projects

1. Open Eclipse.
2. Go to **File > Import > Maven > Existing Maven Projects**.
3. Set root directory to `GlobalConnect/backend`.
4. Ensure these modules are selected:
   - `GatewayManager`
   - `AccountManager`
   - `AuctionManager`
   - `PaymentHandler`
5. Finish import and let Maven dependency resolution complete.

Alternative Eclipse Git path:

1. **File > Import > Git > Projects from Git**
2. Clone URI (or use existing local repository)
3. Import using Maven model.

### 3. Verify backend service ports and startup order

Configured ports:

- GatewayManager: `8080`
- AccountManager: `8081`
- AuctionManager: `8082`
- PaymentHandler: `8083`

Recommended run order:

1. AccountManager
2. AuctionManager
3. PaymentHandler
4. GatewayManager

Run each service from its Spring Boot main class (Run As > Java Application or Spring Boot App).

### 4. Optional: build backend from terminal

From the `backend` directory:

```bash
mvn clean install
```

This builds all backend modules through the parent multi-module Maven project.

If you want to build one service only, run the same command inside that module directory.

### 5. Run frontend manually

From the `frontend` directory:

```bash
npm install
npm run dev
```

Frontend uses the gateway at `http://localhost:8080` in service files.

### 6. Optional Smart Bid Advisor setup

To enable Gemini-backed advisor behavior in frontend, set environment variables before starting Vite:

```powershell
$env:VITE_GEMINI_API_KEY="YOUR_API_KEY"
$env:VITE_GEMINI_MODEL="gemini-2.5-flash"
```

If the API key is not set, Smart Bid Advisor requests will fail gracefully with a missing-key message.

## How to Run Every Facet of the Project

### A. Full local stack (recommended for development)

1. Start backend services in order: AccountManager, AuctionManager, PaymentHandler, GatewayManager.
2. Confirm gateway health at `http://localhost:8080/health`.
3. Start frontend with `npm run dev` from `frontend`.
4. Open frontend at `http://localhost:5173`.
5. Run Postman collection against `http://localhost:8080`.

### B. Backend-only API run

1. Start all backend services (same order as above).
2. Use Postman against gateway routes on port 8080.
3. Skip frontend if you are validating API behavior only.

### C. Hosted deployment run

1. Open http://34.130.27.225/ in browser.
2. If API tests are exposed on the same host, set Postman `baseUrl` to `http://34.130.27.225`.
3. Keep `strictSpecMode=false` for regression-style validation of this iteration.

### D. Eclipse manual workflow (no startup scripts)

1. Import backend via Maven project import.
2. Run each Spring Boot service manually from Eclipse.
3. Start frontend manually from terminal.
4. Validate with Postman suite.

This repository intentionally expects manual startup and orchestration.

## Local Verification Checklist

1. Open `http://localhost:5173` for frontend (default Vite dev port).
2. Verify gateway health at `http://localhost:8080/health`.
3. Create seller and buyer accounts.
4. Add an item via Item Manager.
5. Place bids via dashboard and bid page.
6. Validate pending payments and complete payment.
7. Check receipt visibility.

## Postman: Latest Test Suite Breakdown

Primary latest collection in this repo:

- `postman/GlobalConnect_TestSuitev4.json`

Additional historical files:

- `postman/YorkU Gateway E2E Test Suite V3.postman_collection.json`
- `postman/YorkU_E2E_Test_Suite_V3.json`
- `postman/GlobalConnect Gateway 8080 Tests v4 - Spec and Regression Suite.postman_test_run.json` (sample run output)

### Import the latest collection

1. Open Postman.
2. Click **Import**.
3. Select `postman/GlobalConnect_TestSuitev4.json`.
4. Open the imported collection and inspect collection variables.

### Important collection variables

- `baseUrl` (default: `http://localhost:8080`)
  - For local testing, keep as localhost.
  - For deployed testing, you may set to `http://34.130.27.225` if gateway routes are exposed there.
- `strictSpecMode` (default: `false`)
  - `false`: tolerates known in-progress teammate-dependent behaviors.
  - `true`: enforces stricter expected/spec-complete behavior.
- Run-generated variables:
  - `runId`, `sellerUsername`, `buyerUsername`, `itemId`, session/account variables, etc.

### Collection folder-by-folder intent

- `00 - Initialize unique run`
  - Health checks and unique variable generation for reruns.
- `01 - Account lifecycle`
  - Multi-user signup/login/fetch account coverage.
- `02 - Authentication and session semantics`
  - Valid + invalid auth and login edge behavior.
- `03 - Catalogue and search behavior`
  - Add searchable items and validate catalogue retrieval/search expectations.
- `04 - Ownership, fetchitems, and item CRUD`
  - Owner vs non-owner permissions and CRUD robustness.
- `05 - Bidding behavior`
  - Valid bidding, outbidding, and rejection paths.
- `06 - Pending payments, payment, and shipping`
  - Pending/receipt checks, payment validation, and shipping flow checks.
- `07 - Password reset flow`
  - Forgot-password request through new-password login.
- `08 - Robustness and validation matrix`
  - Negative and malformed request tests.
- `09 - Optional cleanup`
  - Cleanup items to reduce test data buildup.

### How to run the collection (recommended)

1. Start all backend services locally and verify `GET /health` on gateway.
2. In Postman, open the collection runner.
3. Confirm `baseUrl` and `strictSpecMode` values.
4. Run all folders in order (default top-to-bottom).
5. Review failed assertions:
   - If failures happen only in strict mode, compare with known in-progress areas.
   - If failures happen in default mode, investigate service health/logs first.

### Interpreting results in this iteration

- The v4 suite is designed to be rerunnable without DB resets by generating unique users/items.
- Some assertions are intentionally future-facing when strict mode is enabled.
- This makes the same collection useful for both current regression checks and final spec-complete validation.

## Notes on Databases

SQLite files are created automatically during local runs:

- `accountmanager.db`
- `auctionmanager.db`
- `payments.db`

Schema initialization is configured in each microservice via `schema.sql`.

Typical tables include:

- AccountManager: `accounts`, `sessions`, `password_resets`
- AuctionManager: `items`, `bids`
- PaymentHandler: `pending_payments`, `receipts`

## Example End-to-End Business Flow

1. Seller signs up and logs in.
2. Buyer signs up and logs in.
3. Seller adds an auction item.
4. Buyer browses catalogue and bids.
5. Buyer wins and pays.
6. Shipping flow is triggered.
7. Buyer reviews receipts.

## Robustness and Validation Coverage

The latest Postman suite validates:

- invalid authentication/session inputs
- missing required fields on account and payment calls
- invalid item and bid operations
- ownership and authorization boundaries on item modification/removal
- password reset negative and positive paths

This is designed for both regression confidence and ongoing spec-compliance checks.

## AI Usage Note

AI tooling was used during development assistance for debugging, test authoring support, and documentation drafting. All integrated code and test behavior should still be manually verified in your environment.

## Troubleshooting

- `Connection refused` from frontend/Postman:
  - confirm all 4 backend services are running
  - check ports 8080-8083 are free and correct
- Authentication failures:
  - verify session token/account UID pairing
  - rerun login requests to refresh session tokens
- Payment/shipping flow mismatches:
  - re-run from folder 00 to regenerate a clean test run context

## Team Workflow Reminder

Because startup scripts are not part of this version, onboarding should assume:

- manual project import into IDE (Eclipse recommended)
- manual service startup per microservice
- manual frontend startup via npm
- Postman collection execution for integration verification
