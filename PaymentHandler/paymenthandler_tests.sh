#!/usr/bin/env bash
set -euo pipefail

###############################################################################
# Usage:
#   chmod +x paymenthandler_tests.sh
#   ./paymenthandler_tests.sh                # http://localhost:8080
#   ./paymenthandler_tests.sh localhost 8081 # domain + port
###############################################################################

DOMAIN="${1:-http://localhost}"
PORT="${2:-8080}"
if [[ "$DOMAIN" != http://* && "$DOMAIN" != https://* ]]; then DOMAIN="http://$DOMAIN"; fi

BASE="${DOMAIN%/}:$PORT/api/payment"
HDR=(-H "Content-Type: application/json")
SECRET="test-secret"

PASS=0
FAIL=0
WARN=0

say()  { printf "%s\n" "$*"; }
ok()   { PASS=$((PASS+1)); say "✅ PASS: $*"; }
bad()  { FAIL=$((FAIL+1)); say "❌ FAIL: $*"; }
warn() { WARN=$((WARN+1)); say "⚠️  WARN: $*"; }

post_raw () { curl -sS "${HDR[@]}" -X POST "$1" -d "$2"; }
post_status () { curl -sS -o /dev/null -w "%{http_code}" "${HDR[@]}" -X POST "$1" -d "$2" || true; }
contains () { echo "$1" | grep -q "$2"; }

expect_contains () {
  local label="$1" resp="$2" needle="$3"
  if contains "$resp" "$needle"; then ok "$label"; else bad "$label — expected $needle — got: $resp"; fi
}

say "== PaymentHandler Tests =="
say "== Base: $BASE =="

# 0) Reachability
T0="$(post_raw "$BASE/fetchreceipts" "{ \"secret\":\"$SECRET\" }")"
if contains "$T0" "\"errorCode\""; then ok "server reachable (JSON includes errorCode)"; else bad "server reachable (JSON includes errorCode)"; fi

# 1) Fetch pending payments (account 100) -> expect errorCode=0
say
say "1) POST /fetchpendingpayments (accountUID=100)  EXPECT errorCode=0"
PENDING="$(post_raw "$BASE/fetchpendingpayments" "{
  \"secret\":\"$SECRET\",
  \"request\":{ \"requestType\":\"FetchPendingPaymentsRequest\", \"accountUID\":100 }
}")"
say "RESPONSE: $PENDING"
expect_contains "/fetchpendingpayments returns errorCode=0" "$PENDING" "\"errorCode\":0"

HAS_501=false
if contains "$PENDING" "\"itemId\":501"; then HAS_501=true; fi

# 2) Pay (account 100, item 501) -> expect 0 if pending exists else 7
say
if $HAS_501; then
  say "2) POST /pay (accountUID=100,itemId=501)  EXPECT errorCode=0"
else
  say "2) POST /pay (accountUID=100,itemId=501)  EXPECT errorCode=7"
fi

PAY="$(post_raw "$BASE/pay" "{
  \"secret\":\"$SECRET\",
  \"request\":{
    \"requestType\":\"PayRequest\",
    \"accountUID\":100,
    \"itemId\":501,
    \"cardNumber\":\"4111111111111111\",
    \"name\":\"Alex Doe\",
    \"expDate\":\"12/30\",
    \"securityCode\":\"123\",
    \"paymentMethod\":\"VISA\",
    \"amount\":125.50,
    \"shippingCost\":10.00,
    \"expeditedShipping\":false,
    \"expeditedExtraCost\":0.00
  }
}")"
say "RESPONSE: $PAY"
if $HAS_501; then
  expect_contains "/pay returns errorCode=0" "$PAY" "\"errorCode\":0"
else
  expect_contains "/pay returns errorCode=7" "$PAY" "\"errorCode\":7"
fi

# 3) Fetch pending payments (account 100) -> expect errorCode=0; if pay succeeded, 501 removed
say
say "3) POST /fetchpendingpayments (accountUID=100)  EXPECT errorCode=0"
PENDING2="$(post_raw "$BASE/fetchpendingpayments" "{
  \"secret\":\"$SECRET\",
  \"request\":{ \"requestType\":\"FetchPendingPaymentsRequest\", \"accountUID\":100 }
}")"
say "RESPONSE: $PENDING2"
expect_contains "/fetchpendingpayments returns errorCode=0" "$PENDING2" "\"errorCode\":0"

if $HAS_501; then
  if contains "$PENDING2" "\"itemId\":501"; then bad "pendingPayments does not contain itemId 501"; else ok "pendingPayments does not contain itemId 501"; fi
fi

# 4) Fetch receipts (account 100) -> expect errorCode=0; if pay succeeded, receipts include 501
say
say "4) POST /fetchreceipts (accountUID=100)  EXPECT errorCode=0"
RECEIPTS="$(post_raw "$BASE/fetchreceipts" "{
  \"secret\":\"$SECRET\",
  \"request\":{ \"requestType\":\"FetchReceiptsRequest\", \"accountUID\":100 }
}")"
say "RESPONSE: $RECEIPTS"
expect_contains "/fetchreceipts returns errorCode=0" "$RECEIPTS" "\"errorCode\":0"

if $HAS_501; then
  if contains "$RECEIPTS" "\"itemId\":501"; then ok "receipts include itemId 501"; else warn "receipts include itemId 501"; fi
fi

# Robustness: mismatch requests -> expect errorCode=6
say
say "5) POST /fetchreceipts with FetchPendingPaymentsRequest  EXPECT errorCode=6"
R1="$(post_raw "$BASE/fetchreceipts" "{
  \"secret\":\"$SECRET\",
  \"request\":{ \"requestType\":\"FetchPendingPaymentsRequest\", \"accountUID\":100 }
}")"
say "RESPONSE: $R1"
expect_contains "mismatch to /fetchreceipts returns errorCode=6" "$R1" "\"errorCode\":6"

say
say "6) POST /fetchpendingpayments with FetchReceiptsRequest  EXPECT errorCode=6"
R2="$(post_raw "$BASE/fetchpendingpayments" "{
  \"secret\":\"$SECRET\",
  \"request\":{ \"requestType\":\"FetchReceiptsRequest\", \"accountUID\":100 }
}")"
say "RESPONSE: $R2"
expect_contains "mismatch to /fetchpendingpayments returns errorCode=6" "$R2" "\"errorCode\":6"

# Robustness: missing request -> expect errorCode=6
say
say "7) POST /fetchreceipts missing request  EXPECT errorCode=6"
R3="$(post_raw "$BASE/fetchreceipts" "{ \"secret\":\"$SECRET\" }")"
say "RESPONSE: $R3"
expect_contains "missing request returns errorCode=6" "$R3" "\"errorCode\":6"

# Robustness: pay invalid scenarios -> expect errorCode=7
say
say "8) POST /pay (accountUID=999,itemId=999)  EXPECT errorCode=7"
R4="$(post_raw "$BASE/pay" "{
  \"secret\":\"$SECRET\",
  \"request\":{
    \"requestType\":\"PayRequest\",
    \"accountUID\":999,
    \"itemId\":999,
    \"cardNumber\":\"4111111111111111\",
    \"name\":\"Nobody\",
    \"expDate\":\"12/30\",
    \"securityCode\":\"123\",
    \"paymentMethod\":\"VISA\",
    \"amount\":20.00,
    \"shippingCost\":5.00,
    \"expeditedShipping\":false,
    \"expeditedExtraCost\":0.00
  }
}")"
say "RESPONSE: $R4"
expect_contains "pay without pending returns errorCode=7" "$R4" "\"errorCode\":7"

say
say "9) POST /pay blank fields  EXPECT errorCode=7"
R5="$(post_raw "$BASE/pay" "{
  \"secret\":\"$SECRET\",
  \"request\":{
    \"requestType\":\"PayRequest\",
    \"accountUID\":100,
    \"itemId\":502,
    \"cardNumber\":\"\",
    \"name\":\"\",
    \"expDate\":\"\",
    \"securityCode\":\"\",
    \"paymentMethod\":\"\",
    \"amount\":50.00,
    \"shippingCost\":5.00,
    \"expeditedShipping\":false,
    \"expeditedExtraCost\":0.00
  }
}")"
say "RESPONSE: $R5"
expect_contains "pay blank fields returns errorCode=7" "$R5" "\"errorCode\":7"

say
say "10) POST /pay negative amount  EXPECT errorCode=7"
R6="$(post_raw "$BASE/pay" "{
  \"secret\":\"$SECRET\",
  \"request\":{
    \"requestType\":\"PayRequest\",
    \"accountUID\":100,
    \"itemId\":502,
    \"cardNumber\":\"4111111111111111\",
    \"name\":\"Alex Doe\",
    \"expDate\":\"12/30\",
    \"securityCode\":\"123\",
    \"paymentMethod\":\"VISA\",
    \"amount\":-1.00,
    \"shippingCost\":5.00,
    \"expeditedShipping\":false,
    \"expeditedExtraCost\":0.00
  }
}")"
say "RESPONSE: $R6"
expect_contains "pay negative amount returns errorCode=7" "$R6" "\"errorCode\":7"

say
say "11) POST /pay negative expeditedExtraCost  EXPECT errorCode=7"
R7="$(post_raw "$BASE/pay" "{
  \"secret\":\"$SECRET\",
  \"request\":{
    \"requestType\":\"PayRequest\",
    \"accountUID\":100,
    \"itemId\":502,
    \"cardNumber\":\"4111111111111111\",
    \"name\":\"Alex Doe\",
    \"expDate\":\"12/30\",
    \"securityCode\":\"123\",
    \"paymentMethod\":\"VISA\",
    \"amount\":70.00,
    \"shippingCost\":5.00,
    \"expeditedShipping\":true,
    \"expeditedExtraCost\":-10.00
  }
}")"
say "RESPONSE: $R7"
expect_contains "pay invalid expedited cost returns errorCode=7" "$R7" "\"errorCode\":7"

# Robustness: malformed JSON -> HTTP 400
say
say "12) POST /pay malformed JSON  EXPECT HTTP 400"
HTTP400="$(post_status "$BASE/pay" '{ "secret":"x", "request": ')"
say "HTTP_STATUS=$HTTP400"
if [[ "$HTTP400" == "400" ]]; then ok "malformed JSON returns HTTP 400"; else bad "malformed JSON returns HTTP 400"; fi

say
say "======================"
say "Passed:   $PASS"
say "Failed:   $FAIL"
say "Warnings: $WARN"
say "======================"
if [[ "$FAIL" -gt 0 ]]; then exit 1; fi
