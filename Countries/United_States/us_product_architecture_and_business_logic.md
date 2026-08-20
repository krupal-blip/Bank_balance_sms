# US Market Product Architecture & Business Logic Specification
## Document: `US_DATA/us_product_architecture_and_business_logic.md`

---

## 1. Executive Technical Architecture

The US market requires a **Zero-Login Dual-Engine Capture System**. Because US users manage multiple financial accounts but refuse to compromise privacy by sharing web banking credentials with third parties, Bank Balance operates entirely via local Android device permissions.

```
                  ┌────────────────────────────────────────────────────────┐
                  │                 INCOMING BANK ALERT                    │
                  └──────────────────────────┬─────────────────────────────┘
                                             │
                       ┌─────────────────────┴────────────────────┐
                       ▼                                          ▼
            [Opt-in Bank SMS (24273, 73981)]           [Bank Push Notification]
                       │                                          │
                       ▼                                          ▼
           [SmsReceiver (BroadcastReceiver)]          [NotificationListenerService]
                       │                                          │
                       └─────────────────────┬────────────────────┘
                                             │
                                             ▼
                             [US Bank Detection Engine]
                             • Sender shortcode check (Chase, BofA, Wells, Citi)
                             • Package name check (com.chase.sig.android, etc.)
                                             │
                                             ▼
                                [Regex Parsing Engine]
                                • Extract Amount ($)
                                • Extract Account Suffix (...4321)
                                • Extract Merchant Name
                                • Extract Available Balance
                                • Categorize Transaction
                                             │
                                             ▼
                             [Room Database (Encrypted)]
                             • TransactionEntity
                             • BankAccountEntity
                             • CreditCardEntity
                                             │
                                             ▼
                          [UI Presentation: 10 Native KPIs]
```

---

## 2. The 10 US Product KPIs — Deep Implementation Details

### KPI 1: Transaction Passbook (SMS & Push Dual Engine)
* **Ingestion Channel**: Listens for SMS from registered US shortcodes (`24273` - Chase, `73981` - BofA, `93557` - Wells Fargo, `95686` - Citi, `227898` - Capital One) and system push notifications.
* **Normalization**: Automatically strips currency symbols (`$`, `USD`), commas, and dates, converting into standard `BigDecimal` representation for arithmetic ledger operations.

### KPI 2: Balance Engine
* **Ledger Mechanics**: 
  $$\text{Current Balance} = \text{Initial Balance} + \sum \text{Credits} - \sum \text{Debits}$$
* If an incoming SMS includes `"Available balance: $X,XXX.XX"`, the ledger performs an **auto-reconciliation checkpoint**, aligning computed balance with official bank statements.

### KPI 3: Bank Accounts Management
* Pre-loaded with the **Top 15 US Commercial Banks** (FDIC certified).
* Supports **Checking Accounts, High-Yield Savings Accounts (HYSA), and Money Market Accounts (MMA)**.

### KPI 4: Credit Cards & Utilization (The 30% FICO Rule)
* US consumers monitor card utilization aggressively:
  $$\text{Credit Utilization} = \frac{\sum \text{Current Balance}}{\sum \text{Credit Limit}} \times 100$$
* **App Trigger**: When utilization exceeds **30%**, the app surfaces an automated alert: *"High card balance may temporarily impact your FICO credit score."*

### KPI 5: Transaction Categorization Engine
* Matches merchant tokens against standard US spending categories:
  * **Groceries**: Target, Walmart, Trader Joe's, Kroger, Whole Foods.
  * **Food & Dining**: Starbucks, McDonald's, DoorDash, Uber Eats.
  * **Transportation**: Shell, Chevron, Exxon, Uber, Lyft.
  * **Entertainment/Subs**: Netflix, Spotify, Amazon Prime, Apple.com.

### KPI 6: Statement & PDF Generation
* Formatted specifically for **IRS Schedule B / Schedule C** expense tracking and annual review. Includes transaction date, reference ID, merchant, category, and running balance.

### KPI 7: Banking Utilities
* **ABA Routing Validator**: Implements the official 9-digit Federal Reserve Modulo 10 Checksum Algorithm.
* **Direct Customer Service Dialer**: 1-click dialer for 24/7 automated telephone balance lines.

### KPI 8: Bank & Financial Market Holidays
* Pre-configured with all **14 Federal Reserve & NYSE/Nasdaq 2026 Holidays**, warning users when ACH direct deposits will experience a 24-hour processing delay.

### KPI 9: Government Schemes & Tax Shelter Calculators
* Includes verified 2026 IRS limits and compound interest growth models for:
  * **401(k) / 403(b)**: $24,500 employee limit + $8,000 catch-up.
  * **Roth IRA**: $7,500 limit with MAGI phase-out calculators.
  * **HSA**: $4,400 individual / $8,750 family triple-tax growth simulator.
  * **529 Plan**: College tuition projection and $35,000 Roth IRA rollover estimator.

### KPI 10: Localization & Privacy UX
* Complete `en_US` vocabulary (Checking instead of Current Account, Routing Number instead of IFSC, Credit Limit instead of Card Max).
* Dedicated **"100% On-Device Privacy Guarantee"** onboarding flow.
