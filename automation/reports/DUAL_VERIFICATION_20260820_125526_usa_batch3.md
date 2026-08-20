# Dual-Table Verification & Bank Balance Audit Report

---

## 📊 1. Batch Execution Metadata
- **Input Batch**: `samples/usa_batch3.xml`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Engine**: `OpenCode (Regional SMS Parser)`
- **Total Ingested Messages**: **102**
- **Overall Balance Match Status**: **⚠️ DISCREPANCIES DETECTED**
- **Timestamp**: 2026-08-20T12:55:26.374030

---

## 🔍 2. DUAL-TABLE COMPARISON MATRIX (Claude Expected vs. OpenCode Parsed)
*Exact comparison between Claude's generated truth and OpenCode's extracted numbers:*

| Bank | Account / Card | Metric | Expected (Claude) | Parsed (OpenCode) | Status | Diff |
|---|---|:---:|:---:|:---:|:---:|:---:|
| **Chase** | `...9384` | **Final Balance** | $17,736.98 | $18,469.92 | ❌ MISMATCH | $732.94 |
| **Chase** | `...882` | **Final Balance** | $17,736.98 | $17,644.26 | ❌ MISMATCH | $92.72 |
| **Bank of America** | `...9661` | **Final Balance** | $1,066.50 | $1,066.50 | ✅ MATCH | $0.00 |
| **Bank of America** | `...9111` | **Final Balance** | $3,352.96 | $2,926.74 | ❌ MISMATCH | $426.22 |

---

## 🏦 3. Final Computed Bank Account & Card Passbook Table
| Bank | Account / Card | Type | Total Txns | Total Debits | Total Credits | Final Available Balance |
|---|---|:---:|:---:|:---:|:---:|:---|
| **Bank of America** | `...9661` | BANK | 7 | $381.47 | $400.00 | **$1,066.50** |
| **Bank of America** | `...9111` | CARD | 27 | $10,590.61 | $992.08 | **$2,926.74** |
| **Chase** | `...9384` | BANK | 11 | $596.67 | $6,216.83 | **$18,469.92** |
| **Chase** | `...None` | BANK | 8 | $77.25 | $523.57 | **$446.32** |
| **Chase** | `...882` | CARD | 20 | $3,141.42 | $0.00 | **$17,644.26** |
| **Chase** | `...9384` | CARD | 3 | $0.00 | $830.39 | **$18,555.87** |

---

## 🎯 4. Archival Record
- Batch archived to `automation/processed/20260820_125526_usa_batch3.xml`.
