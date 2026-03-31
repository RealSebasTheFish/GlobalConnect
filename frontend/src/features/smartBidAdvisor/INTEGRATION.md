# Smart Bid Advisor (Isolated Module)

This module is intentionally isolated and does not modify any existing app files.

## Files in this folder

- `SmartBidAdvisorPanel.jsx`: UI panel for budget inputs and strategy display.
- `geminiBidAdvisorClient.js`: Gemini API caller and strategy parser.
- `buildBidAdvisorContext.js`: Helpers to shape item and bid history objects.
- `smartBidAdvisor.css`: Component-local styling.
- `index.js`: Single import surface.

## Expected props

`SmartBidAdvisorPanel` expects:

- `item`: object with item metadata (`id`, `name`, `description`, `startingPrice`, `currentHighestBid`, `closed`, `highestBidderUid`)
- `bidHistory`: optional array of bids
- `geminiApiKey`: optional string (falls back to `VITE_GEMINI_API_KEY`)
- `onStrategyGenerated`: optional callback

## Example usage (for later)

```jsx
import { SmartBidAdvisorPanel, buildBidAdvisorContextFromItem } from "../features/smartBidAdvisor";

const itemContext = buildBidAdvisorContextFromItem(selectedItem);

<SmartBidAdvisorPanel
  item={itemContext}
  bidHistory={selectedItemBidHistory}
/>
```

## Quick sandbox usage

To test the module quickly, temporarily render `SmartBidAdvisorTestPage` from this folder in any route/page shell.

```jsx
import { SmartBidAdvisorTestPage } from "../features/smartBidAdvisor";

export default function Sandbox() {
  return <SmartBidAdvisorTestPage />;
}
```

## CLI-only Gemini key setup (PowerShell + gcloud)

1. Set the project:

```powershell
gcloud config set project YOUR_PROJECT_ID
```

2. Enable Gemini API service:

```powershell
gcloud services enable generativelanguage.googleapis.com
```

3. Create an API key:

```powershell
gcloud services api-keys create --display-name="globalconnect-frontend-gemini" --api-target=service=generativelanguage.googleapis.com
```

4. List keys if needed:

```powershell
gcloud services api-keys list
```

5. For local dev, set the key in your shell before starting Vite:

```powershell
$env:VITE_GEMINI_API_KEY="YOUR_API_KEY_VALUE"
npm run dev
```

## Note

Because this is a frontend direct-call approach, the API key is shipped to the browser at runtime. This is okay for early prototyping, but move Gemini calls to a backend proxy before production.
