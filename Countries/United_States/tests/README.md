# US SMS Cognitive Test Harness
## Location: `US_DATA/tests/`

---

## 📌 Testing Philosophy & Strategy
In accordance with your directive:
1. **Human/Cognitive Thought First**: When you provide external real-world SMS samples, we first analyze and formulate what the financial event *truly means* in reality (e.g., Was money actually moved? Is it an executed debit, a credit, or a future mandate setup? What account/card was touched?).
2. **Template Execution**: The raw SMS is processed through the **US Region Profile (`UsProfile.kt`)** and extraction patterns (`us_bank_sms_formats.json`).
3. **Automated Verification**: The test engine matches the template's output against our cognitive ground truth across 7 dimensions (`is_transaction`, `bank`, `account`, `amount`, `balance`, `txn_type`, `source`).

---

## 📁 Test Directory Structure

```
US_DATA/tests/
├── run_us_sms_tests.py        ← Test execution engine with dual validation
├── us_sms_test_cases.json     ← Master test cases database with human thought reasoning
└── README.md                  ← This instruction guide
```

---

## 🚀 How to Add & Test External US SMS Messages

Whenever you have raw US SMS messages to test:

### Step 1: Add your SMS to `us_sms_test_cases.json`
```json
{
  "test_id": "US_TEST_009_YOUR_BANK",
  "raw_sender": "24273",
  "raw_body": "Paste your external raw SMS text here",
  "thought_process": {
    "scenario": "Brief description of the event",
    "reasoning": "Why this is or isn't a transaction, amount spent, etc.",
    "financial_impact": "Debit/Credit/None"
  },
  "expected_result": {
    "is_transaction": true,
    "bank": "Chase",
    "account": "4321",
    "amount": "45.00",
    "balance": "1,200.00",
    "txn_type": "DEBIT",
    "source": "CARD",
    "merchant": "UBER"
  }
}
```

### Step 2: Run the Test Engine
```bash
python3 /Volumes/Extra/backup/R&D/Bank_balance/US_DATA/tests/run_us_sms_tests.py
```

---

## 📊 Benchmark Baseline
* Current Test Cases: **8/8 Passed (100.0% Accuracy)**
* Covers: **POS Debit, ATM Withdrawal, ACH Payroll Direct Deposit, Card Subscription, SafePass OTP, AutoPay Mandate Creation, and Declined Transactions.**
