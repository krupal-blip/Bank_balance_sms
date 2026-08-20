# 🛡️ Universal Non-Opt-in SMS Solutions Architecture

When users in the **USA, UK, EU, Canada, or Australia** have **NOT** enabled automatic SMS transaction alerts, or their bank uses push notifications (app-only), the app must provide instant, non-empty, actionable alternatives rather than a blank screen.

---

## 🏗️ 4-Pillar Non-Opt-in Feature Suite

```
                       ┌──────────────────────────────────────────┐
                       │ User Has NOT Opted into Bank SMS Alerts  │
                       └────────────────────┬─────────────────────┘
                                            │
        ┌───────────────────┬───────────────┴───────────────┬───────────────────┐
        ▼                   ▼                               ▼                   ▼
┌───────────────┐   ┌───────────────┐               ┌───────────────┐   ┌───────────────┐
│ 1. 1-Tap SMS  │   │ 2. In-App Opt-│               │ 3. On-Demand  │   │ 4. Official   │
│   Keyword     │   │   in Guide    │               │   IVR Balance │   │   NetBanking  │
│   Request     │   │   (Visual)    │               │   Hotline     │   │   Secure Portal│
├───────────────┤   ├───────────────┤               ├───────────────┤   ├───────────────┤
│ Sends "BAL"   │   │ Interactive   │               │ 1-Tap Dial to │   │ Sandboxed     │
│ to bank short-│   │ click-path to │               │ Bank's toll-  │   │ HTTPS webview │
│ code (e.g.    │   │ enable text   │               │ free automated│   │ for login     │
│ 24273 / CHASE)│   │ alerts in bank│               │ voice system  │   │ balance check │
│ ➔ Instant SMS │   │ mobile app    │               │               │   │               │
└───────────────┘   └───────────────┘               └───────────────┘   └───────────────┘
```

---

## 📱 1. 1-Tap On-Demand SMS Keyword Trigger (`SMS_KEYWORD`)
* **How it works**: Even if transactional SMS alerts are off, almost all US/UK banks support **on-demand text banking**.
* **App Implementation**:
  - The user taps **"Check Chase Balance"**.
  - App launches default SMS intent with pre-filled:
    * **To**: `24273` (Chase)
    * **Body**: `BAL 9384`
  - Within 5 seconds, Chase replies with a formatted SMS balance alert.
  - Our **SMS Parser captures and parses this incoming message in real time**, automatically updating the passbook ledger without manual entry!

---

## 🧭 2. In-App Bank Opt-In Guide (`OPT_IN_PATH`)
* **How it works**: Visual, step-by-step instructions showing the user how to activate free text alerts in their bank app.
* **Per-Bank Configuration in Pure JSON**:
  ```json
  {
    "bank_name": "JPMorgan Chase",
    "opt_in_path": "Chase Mobile App > Profile > Alerts & Messages > Manage Alerts > Text",
    "supported_alerts": ["Real-time debit purchases", "Direct deposits", "Low balance alerts"]
  }
  ```

---

## 📞 3. Direct Automated IVR Voice Line (`IVR`)
* **How it works**: 1-Tap dialer to the bank's automated telephone banking hotline.
* **Bank Registry**:
  - **Chase**: `1-800-935-9935`
  - **Bank of America**: `1-800-432-1000`
  - **Wells Fargo**: `1-800-869-3557`
  - **Citibank**: `1-800-374-9700`

---

## 🔒 4. Sandboxed Secure NetBanking (`NET_BANKING`)
* **How it works**: Opens bank's official portal inside a hardened, sandboxed Android Custom Tab / WebView:
  - Strict HTTPS only.
  - Host allowlisted directly against `region_profile.json`.
  - Zero credential storage, zero cookie leakage.
