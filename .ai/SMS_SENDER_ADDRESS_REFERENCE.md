# 📱 SMS Sender Address Field — Production Reference
# How each country's banks appear in Android `SmsMessage.getOriginatingAddress()`

## ⚡ Critical Production Difference: India vs USA vs UK vs AU vs DE

---

## 🇮🇳 INDIA — Alphanumeric Sender ID (VI-HDFCBK-I format)

**Carrier:** TRAI mandated DLT (Distributed Ledger Technology) registration.
**Format:** `XX-XXXXXX-X` (2-char telecom operator prefix, 6-char brand, 1-char message type suffix)
**Android address field:** Alphanumeric string like `VI-HDFCBK-I`, `BW-SBIINB`, `TM-ICICIB`, `JO-AXISBK`

| Bank | Android address field |
|---|---|
| HDFC Bank (via Vodafone-Idea) | `VI-HDFCBK-I` |
| SBI (via BSNL/Wireless) | `BW-SBIINB` |
| ICICI Bank | `TM-ICICIB` |
| Axis Bank | `JO-AXISBK` |
| Kotak Mahindra | `AT-KOTAKB` |

**Key property:** One-way only. User CANNOT reply.

---

## 🇺🇸 USA — Numeric Short Code (5–6 digits) — NO alphanumeric Sender IDs

**Regulation:** FCC/CTIA strictly BLOCKS alphanumeric sender IDs in the US.
**Format:** Pure 5–6 digit short codes registered via CTIA/CSCA OR 10DLC (10-digit long codes).
**Android address field:** Pure numeric string like `"24273"`, `"322632"`, `"93557"`

> **CRITICAL:** You will NEVER see "Chase" or "BankOfAmerica" in the address field in the US.
> The app MUST match against numeric shortcodes only.

| Bank | Primary address (shortcode) | Additional shortcodes | Use |
|---|---|---|---|
| **Chase** | `24273` | `72799` | Alerts, fraud, BAL |
| **Bank of America** | `322632` | `73981`, `23618` | Fraud alerts, SafePass 2FA |
| **Wells Fargo** | `93557` | `93733`, `93729`, `20342`, `22981`, `93000` | Alerts, BAL, fraud |
| **Citibank** | `95686` | `692484` | Alerts, fraud |
| **Capital One** | `227898` | `26898` | Alerts, OTPs |
| **U.S. Bank** | `872265` | `86434` | Alerts |
| **PNC** | `762265` | — | Alerts |
| **Truist** | `878478` | — | Alerts |
| **TD Bank (US)** | `832265` | — | Alerts |
| **BMO Bank (US)** | `266226` | — | Alerts |

---

## 🇬🇧 UNITED KINGDOM — Alphanumeric Sender ID (like India, but UK rules)

**Regulation:** Ofcom + mobile network operators. No mandatory registration but restricted branded IDs for banks.
**Format:** Brand name up to 11 chars — appears exactly as the brand name.
**Android address field:** Alphanumeric string like `"NatWest"`, `"Barclays"`, `"HSBC"`, `"Lloyds"`

| Bank | Android address field | Notes |
|---|---|---|
| Lloyds | `Lloyds` | |
| Barclays | `Barclays` | |
| HSBC UK | `HSBC` | |
| NatWest | `NatWest` | Also supports on-demand: text BAL to `60628` → reply from `60628` |
| Santander UK | `Santander` | |
| Nationwide | `Nationwide` | |
| Monzo | `Monzo` | |
| Starling | `Starling` | |

---

## 🇦🇺 AUSTRALIA — Registered Alphanumeric Sender ID (ACMA registered from July 2026)

**Regulation:** ACMA mandatory Sender ID registration as of July 1, 2026. Unregistered IDs labeled "Unverified".
**Format:** Brand name Sender ID — exactly the bank name.
**Android address field:** Alphanumeric string like `"CommBank"`, `"NAB"`, `"ANZ"`, `"Westpac"`

| Bank | Android address field | Notes |
|---|---|---|
| Commonwealth Bank | `CommBank` | ACMA registered |
| NAB | `NAB` | ACMA registered |
| ANZ | `ANZ` | ACMA registered |
| Westpac | `Westpac` | ACMA registered |
| Macquarie | `Macquarie` | ACMA registered |
| ING Australia | `ING` | ACMA registered |
| UBank | `UBank` | ACMA registered |

---

## 🇩🇪 GERMANY — Alphanumeric Sender ID (EU standard, verified by carriers)

**Regulation:** EU Electronic Communications Code. Providers verify sender identity. Max 11 chars.
**Format:** Brand name Sender ID.
**Android address field:** Alphanumeric string like `"DeutscheBank"`, `"Commerzbank"`, `"Sparkasse"`, `"DKB"`

| Bank | Android address field | Notes |
|---|---|---|
| Deutsche Bank | `DeutscheBank` | Max 11 chars truncated |
| Commerzbank | `Commerzbank` | |
| Sparkasse | `Sparkasse` | Kontowecker alerts |
| DKB | `DKB` | |
| ING Germany | `ING-DiBa` | |
| Postbank | `Postbank` | |
| N26 | `N26` | Push-native, SMS rare |

---

## 🇨🇦 CANADA — Same as USA: Numeric Short Codes only (no alphanumeric)

**Regulation:** CRTC/CTIA — alphanumeric sender IDs NOT supported in Canada (same as USA).
**Format:** Pure numeric shortcodes or 10-digit long codes.
**Android address field:** Pure numeric string.

| Bank | Typical address field | Notes |
|---|---|---|
| RBC | Numeric shortcode (not publicly listed) | OTP/fraud alerts from registered shortcodes |
| TD | Numeric shortcode | |
| Scotiabank | Numeric shortcode | InfoAlerts via shortcode |
| BMO | Numeric shortcode | |
| CIBC | Numeric shortcode | Smart Balance Alerts |

> **NOTE:** Canadian banks do NOT publicly document their shortcodes as openly as US banks.
> Shortcodes are registered via CRTC-compliant carrier agreements.
> Parser should fall back to body-keyword matching when address is unknown numeric.

---

## 🏗️ Parser Engineering Implications

| Country | address field type | Parser matching strategy |
|---|---|---|
| 🇮🇳 IN | `VI-HDFCBK-I` alphanumeric | Full string match on known DLT IDs |
| 🇺🇸 US | `24273` numeric shortcode | Numeric whitelist match |
| 🇬🇧 GB | `NatWest` alphanumeric name | String match on bank name |
| 🇦🇺 AU | `CommBank` alphanumeric name | String match on bank name |
| 🇩🇪 DE | `Commerzbank` alphanumeric name | String match on bank name |
| 🇨🇦 CA | Numeric shortcode (unlisted) | Body-first heuristic + numeric fallback |
