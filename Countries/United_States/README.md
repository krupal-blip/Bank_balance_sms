# US_DATA Master Redesign & Production Architecture
## Standardized against `sms_trans_tracker` & `app_region_templates`

---

## 📌 Architecture Overview
The `US_DATA/` repository is structured to separate **Data Assets** from **Core Code Logic**.
In accordance with the production standard:
> **"A new market ships DATA (JSON + string resources) behind ONE single seam, not new Kotlin."**

---

## 🗂️ Standardized Directory Layout

```
US_DATA/
├── _shared/
│   └── UsFeatureProfile.kt            ← Master Seam implementing RegionFeatureProfile
│
├── sms_parser/                        ← Companion to sms_trans_tracker/template/
│   ├── UsProfile.kt                   ← RegionProfile implementation (USD, 5-6 digit shortcodes)
│   ├── US_SMS_TEMPLATES.md            ← Verified real-world US Bank SMS templates (Chase, BofA, Wells, Citi, CapOne)
│   ├── us_corpus_format.csv           ← 11-column evaluation corpus with 40% negative samples
│   └── us_bank_sms_formats.json       ← Production Regex patterns with named groups
│
├── 01_bank_holidays/                  ← Companion to app_region_templates/01_bank_holidays/
│   └── data/
│       └── holidays_us_2026.json      ← 14 Federal Reserve & Stock Market closures (NATIONAL scope)
│
├── 02_saving_schemes/                 ← Companion to app_region_templates/02_saving_schemes/
│   └── data/
│       └── schemes_us.json            ← 401(k), Roth IRA, HSA (Triple-Tax), 529 College Savings Plan
│
├── 03_bank_code_lookup/               ← Companion to app_region_templates/03_bank_code_lookup/
│   └── us_aba_routing_registry.json   ← Top 15 US Commercial Banks & 9-digit ABA Routing Modulo 10 Checksum
│
├── 04_balance_channels/               ← Companion to app_region_templates/04_balance_channels/
│   └── data/
│       └── channels_us.json           ← 24/7 Telephone IVR & SMS Keyword (BAL) numbers (USSD/Missed-Call disabled)
│
├── 05_net_banking/                    ← Companion to app_region_templates/05_net_banking/
│   └── netbanking_us.json             ← Secure HTTPS login URLs with host allowlists
│
├── us_master_checklist_and_kpi_mapping.md
├── us_product_architecture_and_business_logic.md
└── us_ui_strings_and_privacy_policy.json
```

---

## 📋 Feature Portability & Decision Matrix (US Market)

| Feature ID | Feature Name | US Market Status | Technical Implementation | File Asset |
|---|---|:---:|---|---|
| **00 (Core)** | **SMS Transaction Passbook** | ✅ **Enabled** | Dual Engine: Opt-in SMS (5–6 digit shortcodes) + Push | `sms_parser/UsProfile.kt` |
| **01** | **Bank Holidays** | ✅ **Enabled** | Flat `NATIONAL` scope (No state picker); ACH delay warnings | `01_bank_holidays/data/holidays_us_2026.json` |
| **02** | **Saving Schemes** | ✅ **Enabled** | 2026 IRS Statues (401k, Roth IRA, HSA, 529); minor-unit limits | `02_saving_schemes/data/schemes_us.json` |
| **03** | **Bank Code Lookup** | ✅ **Enabled** | 9-Digit ABA Routing Transit Number with Modulo 10 algorithm | `03_bank_code_lookup/us_aba_routing_registry.json` |
| **04** | **Balance Channels** | 🟡 **Adapted** | **IVR & SMS Keywords enabled**; USSD & Missed-Call disabled | `04_balance_channels/data/channels_us.json` |
| **05** | **Net Banking** | ✅ **Enabled** | HTTPS only, strict host allowlisting (`chase.com`, `bofa.com`, etc.) | `05_net_banking/netbanking_us.json` |

---

## 🚦 Verification Gate:
**United States (US) data architecture is 100% complete, fully modularized, and ready for production integration.**
