# Ground Truth Specification for Test Batches
## Dual-Table Verification Standard

When generating a batch (e.g. `samples/usa/usa_batch3.xml`), Claude must also generate a matching expected truth file:
📁 **`samples/usa/usa_batch3_expected.json`** (or embed `<expected_summary>` in the XML).

---

### 📋 JSON Format: `samples/usa/usa_batch<N>_expected.json`

```json
{
  "batch_id": "usa_batch3",
  "total_messages": 50,
  "expected_accounts": [
    {
      "bank": "Chase",
      "account_or_card": "9384",
      "type": "BANK",
      "expected_final_balance": 14836.59,
      "expected_total_credits": 9619.32,
      "expected_total_debits": 914.48,
      "expected_txn_count": 21
    },
    {
      "bank": "Bank of America",
      "account_or_card": "9111",
      "type": "CARD",
      "expected_final_balance": 2927.26,
      "expected_total_credits": 255.00,
      "expected_total_debits": 11910.93,
      "expected_txn_count": 25
    }
  ]
}
```

---

### 🔍 How Automation Verifies Both Tables:

1. **Table A (Expected from Claude)**: Ground truth numbers created during dataset generation.
2. **Table B (Computed by OpenCode/Parser)**: Actual numbers extracted by regex/pipeline after replaying SMS messages.
3. **Table C (Verification Diff Table)**:
   ```
   | Bank | Account | Metric | Expected (Claude) | Parsed (OpenCode) | Status | Diff |
   | Chase | ...9384 | Final Bal | $14,836.59 | $14,836.59 | ✅ MATCH | $0.00 |
   | BofA  | ...9111 | Debits    | $11,910.93 | $11,910.93 | ✅ MATCH | $0.00 |
   ```
