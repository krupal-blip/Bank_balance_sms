# Project Long-Term Memory & Knowledge Base

---

## 📌 Established Knowledge

### 1. Market Data & GA4 Analytics (Property: `516481394`)
- 93.02% of users are currently in India because version 4.1.1 is hardcoded to TRAI-DLT SMS & Indian banking schemes.
- US users (~2,000) have the highest engagement rate (78.13%), proving intent in high-eCPM markets ($15–$28).
- Emerging/Tier-3 markets (Ethiopia, Nigeria) are organic discovery spillover with low eCPM ($0.30–$0.80) and low retention.

### 2. Technical Architecture & Ingestion
- **India**: 100% mandatory transaction SMS sent by banks.
- **Tier-1 Markets (US, UK, CA, AU, DE)**: 
  - US / Canada: Banks offer Opt-in SMS alerts (5–6 digit shortcodes like Chase `24273`, BofA `73981`).
  * UK / Australia / Germany: Banks have phased out per-transaction SMS in favor of real-time App Push Notifications.
- **Solution**: Dual-Engine capture (`SmsReceiver` + Android `NotificationListenerService`).

### 3. Country Expansion Sequence
- **Country #1**: United States (`Countries/United_States/`) — 100% R&D & Test Complete.
- **Country #2**: United Kingdom (`Countries/United_Kingdom/`) — Next in queue.
- **Country #3**: Canada (`Countries/Canada/`)
- **Country #4**: Australia (`Countries/Australia/`)
- **Country #5**: Germany (`Countries/Germany/`)
