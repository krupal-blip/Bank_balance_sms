# Multi-Agent Verification & Account Balance Passbook Report

---

## 📊 1. Batch Execution Overview
- **Source Batch**: `samples/usa_batch2.xml`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Engine**: `OpenCode / Regional Parser`
- **Total Ingested Messages**: **105**
- **Valid Financial Transactions**: **75**
- **Filtered Negatives / Alerts (OTPs, Declines, Mandates)**: **30**
- **System Ingestion Health**: **100% Processed**
- **Timestamp**: 2026-08-20T12:35:11.472440

---

## 🏦 2. Final Bank & Account Balance Verification Table
*The table below simulates the user's live device state after chronological replay of all SMS & notification events:*

| Bank | Account / Card | Type | Total Txns | Total Debits | Total Credits | Final Available Balance |
|---|---|:---:|:---:|:---:|:---:|:---|
| **Bank of America** | `...9661` | BANK | 8 | $1,950.85 | $500.00 | **$1,047.97** |
| **Bank of America** | `...9111` | CARD | 25 | $11,910.93 | $255.00 | **$2,927.26** |
| **Chase** | `...9384` | BANK | 21 | $914.48 | $9,619.32 | **$14,836.59** |
| **Chase** | `...None` | BANK | 1 | $64.07 | $0.00 | **$-64.07** |
| **Chase** | `...882` | CARD | 18 | $17,428.66 | $23.45 | **$0.00** |
| **Chase** | `...9384` | CARD | 1 | $76.40 | $0.00 | **$15,477.11** |
| **Chase** | `...None` | CARD | 1 | $412.77 | $0.00 | **$15,025.52** |

---

## 📝 3. Detailed Transaction Log Sample (First 15 Transactions)
| # | Bank | Account/Card | Type | Amount | Final Balance | Message Excerpt |
|---|---|---|:---:|:---:|:---:|---|
| 1 | Bank of America | `...9111` | DEBIT | $242.58 | $-242.58 | Bank of America: A purchase of $242.58 at KROGER #771 was ch... |
| 2 | Bank of America | `...9111` | DEBIT | $198.89 | $-441.47 | Bank of America: A purchase of $198.89 at KROGER #771 was ch... |
| 3 | Bank of America | `...9661` | DEBIT | $71.87 | $1,270.50 | Bank of America: A withdrawal/debit of $71.87 (ATM WITHDRAWA... |
| 4 | Chase | `...882` | DEBIT | $13.26 | $8,996.86 | Chase: You made a $13.26 debit card purchase with card endin... |
| 5 | Chase | `...882` | CREDIT | $23.45 | $9,020.31 | Chase: A refund of $23.45 from AMAZON MKTPLACE was credited ... |
| 6 | Bank of America | `...9111` | DEBIT | $206.12 | $-647.59 | Bank of America: A purchase of $206.12 at KROGER #771 was ch... |
| 7 | Bank of America | `...9111` | CREDIT | $35.00 | $-612.59 | BofA Reminder: Payment of $35.00 minimum is due on credit ca... |
| 8 | Chase | `...882` | DEBIT | $116.49 | $8,903.82 | Chase: You made a $116.49 debit card purchase with card endi... |
| 9 | Chase | `...9384` | CREDIT | $190.41 | $190.41 | Chase: You scheduled a payment of $190.41 to COMCAST CABLE f... |
| 10 | Bank of America | `...9111` | DEBIT | $64.10 | $-676.69 | BofA: A pending authorization of $64.10 at DELTA AIR 0062341... |
| 11 | Bank of America | `...9111` | DEBIT | $64.10 | $-740.79 | Bank of America: A purchase of $64.10 at DELTA AIR 0062341 w... |
| 12 | Chase | `...882` | DEBIT | $110.07 | $8,793.75 | Chase: You made a $110.07 debit card purchase with card endi... |
| 13 | Bank of America | `...9111` | DEBIT | $716.11 | $716.11 | Bank of America: Your credit card ending in 9111 statement i... |
| 14 | Chase | `...882` | DEBIT | $70.14 | $8,723.61 | Chase: You made a $70.14 debit card purchase with card endin... |
| 15 | Chase | `...9384` | CREDIT | $76.13 | $8,799.74 | Chase: SARAH LOPEZ sent you $76.13 with Zelle(R). Deposited ... |

---

## 🎯 4. Archival & Next Batch Signal
- Raw batch archived to `automation/processed/20260820_123511_usa_batch2.xml`.
- `samples/` folder cleaned for next test run.
