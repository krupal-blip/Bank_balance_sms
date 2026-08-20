# Task Execution Report: `TASK_SAMPLE_20260820_124337_usa_batch3_expected`

---

## 1. Executive Summary
- **Source File**: `samples/usa_batch3_expected.json`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Agent**: `opencode` (via Sample Scooper)
- **Processed Messages**: 60
- **Accuracy**: 5.0%
- **Status**: `COMPLETED`
- **Execution Timestamp**: 2026-08-20T12:43:37.538929

---

## 2. Test Execution Output
```text

=================================================================
       US SMS TEST SUITE — COGNITIVE VS PARSER EVALUATION        
=================================================================

Running 60 Test Cases from: temp_TASK_SAMPLE_20260820_124337_usa_batch3_expected.json

[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L001 (Unknown)
  SMS: "{"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L002 (Unknown)
  SMS: ""batch_id": "usa_batch3","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L003 (Unknown)
  SMS: ""total_messages": 102,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L004 (Unknown)
  SMS: ""positive_transactions": 57,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L005 (Unknown)
  SMS: ""negative_samples": 45,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L006 (Unknown)
  SMS: ""counting_rules": ["
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L007 (Unknown)
  SMS: ""Only EXECUTED money movements count. Pending auths, declines, fraud YES/NO prompts, Zelle requests, AutoPay SETUP notices, scheduled (future) payments, statements, reminders, OTPs and non-bank noise move NO ledger.","
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L008 (Unknown)
  SMS: ""The exact-duplicate BofA $77.30 TARGET SMS appears twice; it must be counted ONCE (dedupe).","
  🧠 Human/Cognitive Thought: Executed financial transaction: $77.30 (DEBIT)
  ⚙️ Parser Extracted: Amount=$77.30, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L009 (Unknown)
  SMS: ""Chase debit card 882 draws from acct 9384: card-882 activity is included in the 9384 ledger; row '882' is the card-level subset (its balance mirrors acct 9384).","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L010 (Unknown)
  SMS: ""Card 9111: expected_final_balance = outstanding owed (limit 4200 - available credit). Purchases=debits, payments/refunds=credits.""
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (CREDIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L011 (Unknown)
  SMS: "],"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L012 (Unknown)
  SMS: ""opening_balances": {"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L013 ("9384")
  SMS: ""9384": 14836.59,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L014 ("9661")
  SMS: ""9661": 1047.97,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L015 (Unknown)
  SMS: ""9111_outstanding": 3191.86,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L016 (Unknown)
  SMS: ""9111_available_credit": 1008.14"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L017 (Unknown)
  SMS: "},"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L018 (Unknown)
  SMS: ""expected_accounts": ["
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L019 (Unknown)
  SMS: "{"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L020 ("bank")
  SMS: ""bank": "Chase","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L021 (Unknown)
  SMS: ""account_or_card": "9384","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L022 ("type")
  SMS: ""type": "BANK","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L023 (Unknown)
  SMS: ""expected_final_balance": 17736.98,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L024 (Unknown)
  SMS: ""expected_total_credits": 5933.43,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L025 (Unknown)
  SMS: ""expected_total_debits": 3033.04,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L026 (Unknown)
  SMS: ""expected_txn_count": 32"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L027 (Unknown)
  SMS: "},"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L028 (Unknown)
  SMS: "{"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L029 ("bank")
  SMS: ""bank": "Chase","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L030 (Unknown)
  SMS: ""account_or_card": "882","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L031 ("type")
  SMS: ""type": "CARD","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L032 (Unknown)
  SMS: ""linked_account": "9384","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L033 (Unknown)
  SMS: ""expected_final_balance": 17736.98,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L034 (Unknown)
  SMS: ""expected_total_credits": 0.0,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L035 (Unknown)
  SMS: ""expected_total_debits": 1605.98,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L036 (Unknown)
  SMS: ""expected_txn_count": 16,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L037 ("note")
  SMS: ""note": "Debit card subset of acct 9384; final balance mirrors 9384.""
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L038 (Unknown)
  SMS: "},"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L039 (Unknown)
  SMS: "{"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L040 ("bank")
  SMS: ""bank": "Bank of America","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L041 (Unknown)
  SMS: ""account_or_card": "9661","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L042 ("type")
  SMS: ""type": "BANK","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L043 (Unknown)
  SMS: ""expected_final_balance": 1066.5,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L044 (Unknown)
  SMS: ""expected_total_credits": 400.0,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L045 (Unknown)
  SMS: ""expected_total_debits": 381.47,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L046 (Unknown)
  SMS: ""expected_txn_count": 7"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L047 (Unknown)
  SMS: "},"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L048 (Unknown)
  SMS: "{"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L049 ("bank")
  SMS: ""bank": "Bank of America","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L050 (Unknown)
  SMS: ""account_or_card": "9111","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L051 ("type")
  SMS: ""type": "CARD","
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L052 (Unknown)
  SMS: ""credit_limit": 4200.0,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L053 (Unknown)
  SMS: ""expected_final_balance": 3352.96,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L054 (Unknown)
  SMS: ""expected_final_available_credit": 847.04,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L055 (Unknown)
  SMS: ""expected_total_credits": 887.08,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L056 (Unknown)
  SMS: ""expected_total_debits": 1048.18,"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L057 (Unknown)
  SMS: ""expected_txn_count": 18"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L058 (Unknown)
  SMS: "}"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L059 (Unknown)
  SMS: "]"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124337_usa_batch3_expected_L060 (Unknown)
  SMS: "}"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------

FINAL RESULT: 3/60 Passed (5.0% Accuracy)
=================================================================


```

---

## 3. Archival Record
- Raw sample moved to `samples/processed/20260820_124337_usa_batch3_expected.json`.
