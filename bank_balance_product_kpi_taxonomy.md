# Bank Balance Checker — Product Feature & KPI Taxonomy

## Scope

This document lists the important **user-purpose/product features** identified from the Product & Business-Logic Audit of Bank Balance Checker v4.1.1.

It intentionally excludes:
- Monetization and advertising
- Subscriptions and affiliate revenue
- Analytics and attribution
- SDKs and SDK initialization
- Crash/ANR infrastructure
- Performance/engineering implementation details
- Security/compliance implementation details
- Other non-user-facing technical components

The source audit covers version 4.1.1 and inventories the product surface/features of the app.

---

## 1. Primary Product KPI Areas

| # | Feature / KPI Area | Main User Purpose | Priority |
|---:|---|---|---|
| 1 | **Transaction Passbook** | Automatically read bank transactions, organize them into accounts/cards, calculate balances, and present a passbook/statement | **Core** |
| 2 | **Balance** | View/check the current account balance | **Core** |
| 3 | **Bank Accounts** | Add/manage accounts and view account-level transaction and balance information | **Core** |
| 4 | **Cards** | Add debit/credit cards and view card-specific transactions and available limits | **Core** |
| 5 | **Transactions** | Browse and track transaction history and credit/debit activity | **Core** |
| 6 | **Statement / PDF** | Filter transactions and export/reopen PDF statements | **Core** |
| 7 | **Bank Services** | Access banking utilities such as balance enquiry, SMS banking, net banking, IFSC/branch search and ATM finding | **Supporting / India-specific** |
| 8 | **Bank Holidays** | View state-wise bank holiday information | **India-specific** |
| 9 | **Government Schemes** | Learn about and use information/calculators for Indian government savings schemes | **India-specific** |
| 10 | **Language / Localization** | Select the app language from supported Indian languages | **Supporting** |

---

## 2. Detailed Feature Inventory

| Feature | User Scenario | Product Function | Market |
|---|---|---|---|
| **Transaction Passbook** | User wants to know what money entered/left an account | Reads transaction SMS, extracts transactions, groups them into accounts/cards, calculates balances and displays a passbook | Core |
| **Balance** | User wants to know current account balance | Shows calculated account balance from processed banking transactions | Core |
| **Bank Accounts** | User wants to see/manage individual accounts | Account-level summary, transaction count and statistics | Core |
| **Cards** | User wants to track debit/credit cards | Manually add cards, identify card transactions and show available limit | Core |
| **Transactions** | User wants to review financial activity | Browse transaction history and distinguish credit/debit activity | Core |
| **Statement / PDF** | User wants a shareable/archivable statement | Filter by account, date and type; generate and reopen PDF statements | Core |
| **Balance Enquiry** | User wants to request bank balance without opening banking | Select bank and use the bank's missed-call/toll-free balance enquiry service | India-only |
| **SMS Banking** | User wants to request bank information by SMS | Shows bank-specific SMS commands and opens a prefilled SMS composer | India-only |
| **USSD Balance Codes** | User wants to use USSD banking | Static `*99*NN#` codes per bank; audit notes the asset is currently unreferenced/dead | India-only / inactive |
| **Net Banking** | User wants to access online banking | Opens the selected bank's web login | Country-pack |
| **IFSC / Branch Finder** | User needs branch/routing information | State → District → Bank → Branch lookup with IFSC, MICR and address | India-specific |
| **Bank Holidays** | User wants to know when banks are closed | State-wise bank holiday calendar | India-specific |
| **Government Schemes** | User wants information about Indian savings/government schemes | Explainers/calculators for PPF, EPF, NPS, SCSS, NSC, Post Office Savings, PMVVY and PMJDY | India-only |
| **Nearby ATM** | User needs to find an ATM | Opens Google Maps with an ATM search | Portable |
| **Account Statistics** | User wants a summary of account activity | Shows computed balance, transaction count and statistics | Supporting |
| **Language Switcher** | User wants to use the app in a preferred language | Supports English plus Indian-language locales | Supporting |
| **Notifications** | User wants relevant app updates/reminders | Notification routing to app screens | Supporting |

---

## 3. Core Financial Product

These are the features that represent the central financial-management experience.

| KPI | Key User Action | Example KPI |
|---|---|---|
| **Transaction Passbook** | Opens/views passbook | Passbook active users |
| **Transaction Parsing** | Gets transactions successfully recognized | Parsed transactions per active user |
| **Balance** | Views account balance | Balance views / active account |
| **Bank Accounts** | Adds or opens an account | Accounts added per user |
| **Transactions** | Opens transaction history | Transaction-screen users |
| **Cards** | Adds/opens a card | Cards added per user |
| **Statement / PDF** | Exports a statement | Statements exported per active account |

The audit identifies the **SMS Transaction Passbook as the strategic core** because it reads banking SMS, extracts transactions, groups them into accounts/cards, computes balances and renders a passbook/statement.

---

## 4. Banking Utility Features

| Feature | User Purpose | Classification |
|---|---|---|
| **Balance Enquiry** | Request bank balance through missed-call/toll-free banking | India-only |
| **SMS Banking** | Send predefined bank commands such as balance/mini-statement requests | India-only |
| **Net Banking** | Open a bank's online banking login | Country-pack |
| **IFSC / Branch Finder** | Find branch, IFSC, MICR and address | India-specific |
| **Nearby ATM** | Find nearby ATM | Portable |
| **Bank Holidays** | Check bank closure/holiday dates | Country-pack / India-specific |

---

## 5. Indian Financial Information

### Government Saving Schemes

The audit identifies eight government savings products:

| Scheme | Type |
|---|---|
| **PPF** | Government savings scheme |
| **EPF** | Provident fund |
| **NPS** | Pension scheme |
| **SCSS** | Senior citizen savings scheme |
| **NSC** | National savings certificate |
| **Post Office Savings** | Government-backed savings |
| **PMVVY** | Government pension/savings scheme |
| **PMJDY** | Financial inclusion scheme |

This should remain a separate **Government Schemes** product area rather than being grouped into banking transactions.

---

## 6. Supporting Product Features

| Feature | Purpose | KPI Importance |
|---|---|---|
| **Account Statistics** | Summarize account balance, transaction count and statistics | Supporting |
| **Language Switcher** | Localize the user experience | Supporting / market readiness |
| **Notifications** | Route users back to relevant features | Supporting |
| **Nearby ATM** | Provide a quick physical banking utility | Supporting |

---

## 7. Features NOT to Treat as Product KPIs

The following were intentionally excluded from this taxonomy:

| Excluded Area | Reason |
|---|---|
| **Advertising / AdMob** | Monetization |
| **Mediation / Waterfalls / eCPM** | Monetization |
| **Subscription / Paywall** | Monetization |
| **Affiliate / Offerwall** | Monetization |
| **Revenue / ARPDAU / LTV** | Monetization |
| **Firebase / Adjust / Analytics implementation** | Measurement infrastructure |
| **SDKs** | Technical implementation |
| **Crash / ANR handling** | Technical quality |
| **Performance optimization** | Engineering |
| **Remote Config implementation** | Technical infrastructure |
| **Parser model implementation** | Technical implementation; its product outcome is Transaction Passbook |
| **Room / database implementation** | Technical implementation |
| **API implementation** | Technical implementation |
| **Security credentials / encryption implementation** | Security/compliance |
| **ProGuard / R8 / Gradle** | Build/engineering |

---

## 8. Recommended Final KPI Structure

For product reporting, the cleanest hierarchy is:

### A. Core Financial Activity
- Transaction Passbook
- Balance
- Bank Accounts
- Transactions
- Cards
- Statement / PDF

### B. Banking Utilities
- Balance Enquiry
- SMS Banking
- Net Banking
- IFSC / Branch Finder
- Nearby ATM
- Bank Holidays

### C. Financial Information
- Government Schemes

### D. Supporting Experience
- Account Statistics
- Language / Localization
- Notifications

This structure avoids turning every individual screen or technical component into a separate KPI while retaining the important user-facing capabilities identified by the audit.

---

## Source Basis

Based on the Product & Business-Logic Audit — Bank Balance Checker, audited build **v4.1.1 / versionCode 412**, covering 177 Kotlin sources, manifest, Gradle configuration, assets, Room schema and Remote Config keys.
