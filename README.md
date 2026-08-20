# Bank Balance Checker — Global Multi-Country R&D Hub

---

## 🏛️ Project Directory Structure

```
Bank_balance/
├── README.md                                          ← Master Project Overview & Architecture Hub
├── bank_balance_product_kpi_taxonomy.md               ← Core Product KPI Taxonomy Audit (v4.1.1 baseline)
├── Bank_Balance_Global_Expansion_RD_Report.pdf        ← Executive Multi-Country Strategy & Causality Report
│
├── Templates/                                         ← Production Standards & Raw Code Skeletons
│   ├── sms_trans_tracker_handout/                     ← SMS Parsing Engine, BiGRU+CRF ML Model & Corpus Spec
│   │   └── sms_trans_tracker/
│   │       ├── template/                              ← Region-neutral Kotlin skeleton & RegionProfile interface
│   │       ├── corpus/                                ← Corpus guidelines & DLT template references
│   │       └── src/                                   ← Production reference implementation
│   │
│   └── app_region_templates/                          ← 5 Companion Feature Templates (One Seam)
│       ├── _shared/                                   ← RegionFeatureProfile.kt.template interface
│       ├── 01_bank_holidays/                          ← Bank Holiday schema & parser
│       ├── 02_saving_schemes/                         ← Government schemes schema & calculators
│       ├── 03_bank_code_lookup/                       ← National routing code lookup (IFSC, ABA, Sort Code)
│       ├── 04_balance_channels/                       ← IVR & SMS text banking channel schemas
│       └── 05_net_banking/                            ← Secure HTTPS portal allowlists
│
└── Countries/                                         ← Country-Specific Data & Test Packages
    │
    ├── United_States/                                 ← [ACTIVE] Country #1: USA
    │   ├── README.md                                  ← US Feature Decision Matrix & Index
    │   ├── _shared/
    │   │   └── UsFeatureProfile.kt                    ← Master Seam implementation (RegionFeatureProfile)
    │   ├── sms_parser/
    │   │   ├── UsProfile.kt                           ← RegionProfile implementation (USD, 5-6 digit shortcodes)
    │   │   ├── US_SMS_TEMPLATES.md                    ← Verified real-world US Bank SMS templates
    │   │   ├── us_corpus_format.csv                   ← 11-column evaluation corpus with 40% negative samples
    │   │   └── us_bank_sms_formats.json               ← Production regex patterns with named groups
    │   ├── 01_bank_holidays/
    │   │   └── data/
    │   │       └── holidays_us_2026.json              ← 14 Federal Reserve & NYSE/Nasdaq market closures
    │   ├── 02_saving_schemes/
    │   │   └── data/
    │   │       └── schemes_us.json                    ← 2026 IRS Statues (401k, Roth IRA, HSA, 529 Plan)
    │   ├── 03_bank_code_lookup/
    │   │   └── us_aba_routing_registry.json           ← Top 15 US Banks & 9-digit ABA Routing Checksum
    │   ├── 04_balance_channels/
    │   │   └── data/
    │   │       └── channels_us.json                   ← 24/7 Telephone IVR & SMS Keyword (BAL) numbers
    │   ├── 05_net_banking/
    │   │   └── netbanking_us.json                     ← HTTPS Login URLs with strict host allowlisting
    │   ├── tests/                                     ← US SMS Cognitive Test Suite
    │   │   ├── run_us_sms_tests.py                    ← Dual Cognitive-Validation Test Runner
    │   │   ├── us_sms_test_cases.json                 ← Test cases database with human thought reasoning
    │   │   └── README.md                              ← Test execution guide
    │   ├── us_master_checklist_and_kpi_mapping.md
    │   ├── us_product_architecture_and_business_logic.md
    │   └── us_ui_strings_and_privacy_policy.json
    │
    ├── United_Kingdom/                                ← [PENDING] Country #2 (UK)
    ├── Canada/                                        ← [PENDING] Country #3 (CA)
    ├── Australia/                                     ← [PENDING] Country #4 (AU)
    └── Germany/                                       ← [PENDING] Country #5 (DE)
```

---

## 🎯 The One Architectural Rule

> **"A new market ships DATA (JSON + string resources) behind ONE single seam, not new Kotlin."**

1. **`Templates/`** defines the rigid interfaces (`RegionProfile` & `RegionFeatureProfile`).
2. **`Countries/<Country_Name>/`** delivers the verified country assets, regexes, holidays, schemes, routing algorithms, and automated test cases.
