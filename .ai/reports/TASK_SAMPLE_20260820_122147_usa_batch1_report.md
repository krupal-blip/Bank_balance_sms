# Task Execution Report: `TASK_SAMPLE_20260820_122147_usa_batch1`

---

## 1. Executive Summary
- **Source File**: `samples/usa_batch1.xml`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Agent**: `opencode` (via Sample Scooper)
- **Processed Messages**: 115
- **Status**: `COMPLETED`
- **Execution Timestamp**: 2026-08-20T12:21:47.913449

---

## 2. Test Execution Output
```text

=================================================================
       US SMS TEST SUITE — COGNITIVE VS PARSER EVALUATION        
=================================================================

Running 115 Test Cases from: temp_TASK_SAMPLE_20260820_122147_usa_batch1.json

[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L001 (24273)
  SMS: "Chase Acct Alert: Your account ending in 9384 has an available balance of $3,250.00. Reply STOP to end alerts."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,250.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,250.00, Acc=9384, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L002 (322632)
  SMS: "Bank of America: Your available balance for account ending in 9661 is $1,875.40 as of 06/01. Not you? Call 800.432.1000."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,875.40 (DEBIT)
  ⚙️ Parser Extracted: Amount=$1,875.40, Acc=9661, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L003 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='BANK'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L004 (322632)
  SMS: "BofA: Payment of $491.69 received on credit card ending 9111. Thank you. Avail credit: $4,200.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $491.69 (CREDIT)
  ⚙️ Parser Extracted: Amount=$491.69, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,200.00', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L005 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $305.39 at APPLE.COM/BILL on 06/04. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L006 (24273)
  SMS: "Chase Alert: A $21.90 card purchase was made at CVS/PHARMACY #883 with your debit card ending in 882 on 06/05. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $21.90 (DEBIT)
  ⚙️ Parser Extracted: Amount=$21.90, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L007 (322632)
  SMS: "BofA eBill Reminder: Your T-MOBILE eBill of $94.35 is due on 06/14. AutoPay is OFF for this payee."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L008 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $333.33 at WHOLE FOODS MKT on 06/06. Avail credit: $3,866.67. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $333.33 (DEBIT)
  ⚙️ Parser Extracted: Amount=$333.33, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,866.67', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L009 (22000)
  SMS: "G-194020 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L010 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $159.87 at KROGER #771 on 06/08. Avail credit: $3,706.80. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $159.87 (DEBIT)
  ⚙️ Parser Extracted: Amount=$159.87, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,706.80', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L011 (24273)
  SMS: "Chase: You made a $157.69 debit card purchase with card ending 882 at WHOLE FOODS MKT on 06/09. Avail bal: $2,578.72."
  🧠 Human/Cognitive Thought: Executed financial transaction: $157.69 (DEBIT)
  ⚙️ Parser Extracted: Amount=$157.69, Acc=None, Type=DEBIT, Bal=$2,578.72
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L012 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 06/14. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L013 (24273)
  SMS: "Chase: RAJ PATEL sent you $190.04 with Zelle(R). Deposited to acct ...9384. Avail bal: $2,768.76."
  🧠 Human/Cognitive Thought: Executed financial transaction: $190.04 (CREDIT)
  ⚙️ Parser Extracted: Amount=$190.04, Acc=9384, Type=CREDIT, Bal=$2,768.76
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L014 (24273)
  SMS: "Chase: RAJ PATEL sent you $212.67 with Zelle(R). Deposited to acct ...9384. Avail bal: $2,981.43."
  🧠 Human/Cognitive Thought: Executed financial transaction: $212.67 (CREDIT)
  ⚙️ Parser Extracted: Amount=$212.67, Acc=9384, Type=CREDIT, Bal=$2,981.43
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L015 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $16.09 with Zelle(R). Deposited to acct ...9384. Avail bal: $2,997.52."
  🧠 Human/Cognitive Thought: Executed financial transaction: $16.09 (CREDIT)
  ⚙️ Parser Extracted: Amount=$16.09, Acc=9384, Type=CREDIT, Bal=$2,997.52
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L016 (24273)
  SMS: "Chase: You sent $170.92 to RAJ PATEL with Zelle(R) from acct ending 9384 on 06/11. Avail bal: $2,826.60."
  🧠 Human/Cognitive Thought: Executed financial transaction: $170.92 (DEBIT)
  ⚙️ Parser Extracted: Amount=$170.92, Acc=None, Type=DEBIT, Bal=$2,826.60
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L017 (24273)
  SMS: "Chase: Your requested balance for acct ...9384 is $2,826.60 as of 06/11."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,826.60 (DEBIT)
  ⚙️ Parser Extracted: Amount=$2,826.60, Acc=9384, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L018 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $291.52 at BEST BUY #442 on 06/11. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L019 (24273)
  SMS: "Chase: Direct deposit of $2,596.08 from INTEREST PAYMENT posted to acct ...9384 on 06/12. Avail bal: $5,422.68."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,596.08 (CREDIT)
  ⚙️ Parser Extracted: Amount=$2,596.08, Acc=9384, Type=CREDIT, Bal=$5,422.68
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L020 (322632)
  SMS: "Bank of America: A purchase of $284.17 at STARBUCKS #2214 was charged to your credit card ending in 9111 on 06/12."
  🧠 Human/Cognitive Thought: Executed financial transaction: $284.17 (DEBIT)
  ⚙️ Parser Extracted: Amount=$284.17, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L021 (322632)
  SMS: "BofA: Payment of $329.11 received on credit card ending 9111. Thank you. Avail credit: $3,751.74."
  🧠 Human/Cognitive Thought: Executed financial transaction: $329.11 (CREDIT)
  ⚙️ Parser Extracted: Amount=$329.11, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,751.74', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L022 (24273)
  SMS: "Chase: MIKE CHEN sent you $231.16 with Zelle(R). Deposited to acct ...9384. Avail bal: $5,324.73."
  🧠 Human/Cognitive Thought: Executed financial transaction: $231.16 (CREDIT)
  ⚙️ Parser Extracted: Amount=$231.16, Acc=9384, Type=CREDIT, Bal=$5,324.73
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L023 (24273)
  SMS: "Chase: You scheduled a payment of $193.88 to COMCAST CABLE for 06/24 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $193.88 (CREDIT)
  ⚙️ Parser Extracted: Amount=$193.88, Acc=9384, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='CREDIT', Parsed='DEBIT'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L024 (322632)
  SMS: "Bank of America: A purchase of $159.94 at AMAZON MKTPLACE was charged to your credit card ending in 9111 on 06/15."
  🧠 Human/Cognitive Thought: Executed financial transaction: $159.94 (DEBIT)
  ⚙️ Parser Extracted: Amount=$159.94, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L025 (37777)
  SMS: "USPS: Your package 9469052253928720220 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L026 (322632)
  SMS: "Bank of America: A purchase of $173.48 at AMAZON MKTPLACE was charged to your credit card ending in 9111 on 06/16."
  🧠 Human/Cognitive Thought: Executed financial transaction: $173.48 (DEBIT)
  ⚙️ Parser Extracted: Amount=$173.48, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L027 (24273)
  SMS: "Chase: Direct deposit of $430.87 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 06/17. Avail bal: $5,755.60."
  🧠 Human/Cognitive Thought: Executed financial transaction: $430.87 (CREDIT)
  ⚙️ Parser Extracted: Amount=$430.87, Acc=9384, Type=CREDIT, Bal=$5,755.60
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L028 (322632)
  SMS: "BofA: Payment of $518.18 received on credit card ending 9111. Thank you. Avail credit: $3,936.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $518.18 (CREDIT)
  ⚙️ Parser Extracted: Amount=$518.18, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,936.50', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L029 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $48.11 at WALMART SUPERCENTER on 06/19. Avail credit: $3,888.39. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $48.11 (DEBIT)
  ⚙️ Parser Extracted: Amount=$48.11, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,888.39', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L030 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $54.36 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$54.36, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'amount': Expected='None', Parsed='54.36'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L031 (322632)
  SMS: "BofA: Payment of $563.10 received on credit card ending 9111. Thank you. Avail credit: $4,200.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $563.10 (CREDIT)
  ⚙️ Parser Extracted: Amount=$563.10, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,200.00', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L032 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $239.49 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$239.49, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'amount': Expected='None', Parsed='239.49'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L033 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='BANK'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L034 (24273)
  SMS: "Chase Alert: A $148.57 card purchase was made at TARGET T-1043 with your debit card ending in 882 on 06/23. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $148.57 (DEBIT)
  ⚙️ Parser Extracted: Amount=$148.57, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L035 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $103.70 with Zelle(R). Deposited to acct ...9384. Avail bal: $4,629.45."
  🧠 Human/Cognitive Thought: Executed financial transaction: $103.70 (CREDIT)
  ⚙️ Parser Extracted: Amount=$103.70, Acc=9384, Type=CREDIT, Bal=$4,629.45
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L036 (24273)
  SMS: "Chase: You made a $27.37 debit card purchase with card ending 882 at SHELL OIL 5744 on 06/24. Avail bal: $4,602.08."
  🧠 Human/Cognitive Thought: Executed financial transaction: $27.37 (DEBIT)
  ⚙️ Parser Extracted: Amount=$27.37, Acc=None, Type=DEBIT, Bal=$4,602.08
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L037 (322632)
  SMS: "Bank of America: A withdrawal/debit of $143.13 (COMCAST CABLE) posted to account ending 9661 on 06/25. Available balance: $1,732.27."
  🧠 Human/Cognitive Thought: Executed financial transaction: $143.13 (DEBIT)
  ⚙️ Parser Extracted: Amount=$143.13, Acc=None, Type=DEBIT, Bal=$1,732.27
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9661', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L038 (24273)
  SMS: "Chase: You sent $173.87 to EMILY DAVIS with Zelle(R) from acct ending 9384 on 06/25. Avail bal: $4,428.21."
  🧠 Human/Cognitive Thought: Executed financial transaction: $173.87 (DEBIT)
  ⚙️ Parser Extracted: Amount=$173.87, Acc=None, Type=DEBIT, Bal=$4,428.21
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L039 (+14155550132)
  SMS: "Hey are we still on for dinner tonight?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L040 (24273)
  SMS: "Chase: You sent $114.07 to MIKE CHEN with Zelle(R) from acct ending 9384 on 06/25. Avail bal: $4,314.14."
  🧠 Human/Cognitive Thought: Executed financial transaction: $114.07 (DEBIT)
  ⚙️ Parser Extracted: Amount=$114.07, Acc=None, Type=DEBIT, Bal=$4,314.14
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L041 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $32.98 at KROGER #771 on 06/26. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L042 (22000)
  SMS: "G-150528 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L043 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $654.30 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$654.30, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'amount': Expected='None', Parsed='654.30'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L044 (24273)
  SMS: "Chase: Your one-time code is 641959. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L045 (24273)
  SMS: "Chase: Your one-time code is 386058. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L046 (24273)
  SMS: "Chase: Direct deposit of $573.05 from VENMO CASHOUT posted to acct ...9384 on 06/28. Avail bal: $4,887.19."
  🧠 Human/Cognitive Thought: Executed financial transaction: $573.05 (CREDIT)
  ⚙️ Parser Extracted: Amount=$573.05, Acc=9384, Type=CREDIT, Bal=$4,887.19
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L047 (322632)
  SMS: "Bank of America: A purchase of $162.02 at DOORDASH*SUBWAY was charged to your credit card ending in 9111 on 06/28."
  🧠 Human/Cognitive Thought: Executed financial transaction: $162.02 (DEBIT)
  ⚙️ Parser Extracted: Amount=$162.02, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L048 (24273)
  SMS: "Chase: RAJ PATEL sent you $116.70 with Zelle(R). Deposited to acct ...9384. Avail bal: $5,003.89."
  🧠 Human/Cognitive Thought: Executed financial transaction: $116.70 (CREDIT)
  ⚙️ Parser Extracted: Amount=$116.70, Acc=9384, Type=CREDIT, Bal=$5,003.89
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L049 (24273)
  SMS: "Chase Alert: A $50.69 card purchase was made at STARBUCKS #2214 with your debit card ending in 882 on 06/29. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $50.69 (DEBIT)
  ⚙️ Parser Extracted: Amount=$50.69, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L050 (322632)
  SMS: "Bank of America: A withdrawal/debit of $107.21 (CHECK #1042) posted to account ending 9661 on 06/30. Available balance: $1,625.06."
  🧠 Human/Cognitive Thought: Executed financial transaction: $107.21 (DEBIT)
  ⚙️ Parser Extracted: Amount=$107.21, Acc=None, Type=DEBIT, Bal=$1,625.06
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9661', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L051 (322632)
  SMS: "Bank of America: A withdrawal/debit of $45.08 (ATM WITHDRAWAL #A5521) posted to account ending 9661 on 06/30. Available balance: $1,579.98."
  🧠 Human/Cognitive Thought: Executed financial transaction: $45.08 (DEBIT)
  ⚙️ Parser Extracted: Amount=$45.08, Acc=None, Type=DEBIT, Bal=$1,579.98
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9661', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L052 (322632)
  SMS: "Bank of America: AutoPay is now set up for your credit card ending in 9111. $150.00 (minimum due) will be drafted from account 9661 each cycle. No payment was made today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $150.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='True', Parsed='False'
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'amount': Expected='150.00', Parsed='None'
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L053 (322632)
  SMS: "BofA: Payment of $207.22 received on credit card ending 9111. Thank you. Avail credit: $4,200.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $207.22 (CREDIT)
  ⚙️ Parser Extracted: Amount=$207.22, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,200.00', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L054 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $192.50 at AMAZON MKTPLACE on 07/03. Avail credit: $4,007.50. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $192.50 (DEBIT)
  ⚙️ Parser Extracted: Amount=$192.50, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,007.50', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L055 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $235.27 at WHOLE FOODS MKT on 07/03. Avail credit: $3,772.23. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $235.27 (DEBIT)
  ⚙️ Parser Extracted: Amount=$235.27, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,772.23', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L056 (262966)
  SMS: "390231 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L057 (24273)
  SMS: "Chase: You made a $205.31 debit card purchase with card ending 882 at UBER TRIP on 07/04. Avail bal: $4,540.67."
  🧠 Human/Cognitive Thought: Executed financial transaction: $205.31 (DEBIT)
  ⚙️ Parser Extracted: Amount=$205.31, Acc=None, Type=DEBIT, Bal=$4,540.67
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L058 (24273)
  SMS: "Chase: Direct deposit of $2,375.72 from VENMO CASHOUT posted to acct ...9384 on 07/05. Avail bal: $6,916.39."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,375.72 (CREDIT)
  ⚙️ Parser Extracted: Amount=$2,375.72, Acc=9384, Type=CREDIT, Bal=$6,916.39
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L059 (24273)
  SMS: "Chase: You made a $55.19 debit card purchase with card ending 882 at CVS/PHARMACY #883 on 07/05. Avail bal: $6,861.20."
  🧠 Human/Cognitive Thought: Executed financial transaction: $55.19 (DEBIT)
  ⚙️ Parser Extracted: Amount=$55.19, Acc=None, Type=DEBIT, Bal=$6,861.20
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L060 (322632)
  SMS: "BofA: Payment of $568.06 received on credit card ending 9111. Thank you. Avail credit: $4,200.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $568.06 (CREDIT)
  ⚙️ Parser Extracted: Amount=$568.06, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,200.00', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L061 (24273)
  SMS: "Chase: You scheduled a payment of $69.12 to COMCAST CABLE for 07/19 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $69.12 (CREDIT)
  ⚙️ Parser Extracted: Amount=$69.12, Acc=9384, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='CREDIT', Parsed='DEBIT'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L062 (262966)
  SMS: "546823 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L063 (24273)
  SMS: "Chase Alert: A $85.13 card purchase was made at TRADER JOE'S #552 with your debit card ending in 882 on 07/08. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $85.13 (DEBIT)
  ⚙️ Parser Extracted: Amount=$85.13, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L064 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $279.18 at WALMART SUPERCENTER on 07/09. Avail credit: $3,920.82. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $279.18 (DEBIT)
  ⚙️ Parser Extracted: Amount=$279.18, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,920.82', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L065 (24273)
  SMS: "Chase Alert: A $154.34 card purchase was made at CVS/PHARMACY #883 with your debit card ending in 882 on 07/10. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $154.34 (DEBIT)
  ⚙️ Parser Extracted: Amount=$154.34, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L066 (322632)
  SMS: "Bank of America: AutoPay is now set up for your credit card ending in 9111. $35.00 (minimum due) will be drafted from account 9661 each cycle. No payment was made today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $35.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='True', Parsed='False'
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'amount': Expected='35.00', Parsed='None'
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L067 (322632)
  SMS: "Bank of America: A purchase of $26.64 at AMAZON MKTPLACE was charged to your credit card ending in 9111 on 07/11."
  🧠 Human/Cognitive Thought: Executed financial transaction: $26.64 (DEBIT)
  ⚙️ Parser Extracted: Amount=$26.64, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L068 (24273)
  SMS: "Chase Alert: A $117.12 card purchase was made at LYFT RIDE with your debit card ending in 882 on 07/11. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $117.12 (DEBIT)
  ⚙️ Parser Extracted: Amount=$117.12, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L069 (24273)
  SMS: "Chase: You made a $129.09 debit card purchase with card ending 882 at WHOLE FOODS MKT on 07/12. Avail bal: $5,807.46."
  🧠 Human/Cognitive Thought: Executed financial transaction: $129.09 (DEBIT)
  ⚙️ Parser Extracted: Amount=$129.09, Acc=None, Type=DEBIT, Bal=$5,807.46
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L070 (24273)
  SMS: "Chase: You scheduled a payment of $104.03 to COMCAST CABLE for 07/18 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $104.03 (CREDIT)
  ⚙️ Parser Extracted: Amount=$104.03, Acc=9384, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='CREDIT', Parsed='DEBIT'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L071 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $230.86 at NETFLIX.COM on 07/13. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L072 (24273)
  SMS: "Chase: You made a $64.16 debit card purchase with card ending 882 at EXXONMOBIL 9921 on 07/13. Avail bal: $5,743.30."
  🧠 Human/Cognitive Thought: Executed financial transaction: $64.16 (DEBIT)
  ⚙️ Parser Extracted: Amount=$64.16, Acc=None, Type=DEBIT, Bal=$5,743.30
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L073 (+14155550132)
  SMS: "Hey are we still on for dinner tonight?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L074 (22000)
  SMS: "G-639017 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L075 (22000)
  SMS: "G-432920 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L076 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 07/20. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L077 (24273)
  SMS: "Chase Alert: A $52.31 card purchase was made at TRADER JOE'S #552 with your debit card ending in 882 on 07/16. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $52.31 (DEBIT)
  ⚙️ Parser Extracted: Amount=$52.31, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L078 (24273)
  SMS: "Chase: Your requested balance for acct ...9384 is $5,690.99 as of 07/17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $5,690.99 (DEBIT)
  ⚙️ Parser Extracted: Amount=$5,690.99, Acc=9384, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L079 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $283.25 at CHIPOTLE 1187 on 07/17. Avail credit: $3,610.93. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $283.25 (DEBIT)
  ⚙️ Parser Extracted: Amount=$283.25, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,610.93', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L080 (24273)
  SMS: "Chase: EMILY DAVIS sent you $119.81 with Zelle(R). Deposited to acct ...9384. Avail bal: $5,810.80."
  🧠 Human/Cognitive Thought: Executed financial transaction: $119.81 (CREDIT)
  ⚙️ Parser Extracted: Amount=$119.81, Acc=9384, Type=CREDIT, Bal=$5,810.80
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L081 (322632)
  SMS: "Bank of America: A withdrawal/debit of $45.33 (T-MOBILE) posted to account ending 9661 on 07/19. Available balance: $1,534.65."
  🧠 Human/Cognitive Thought: Executed financial transaction: $45.33 (DEBIT)
  ⚙️ Parser Extracted: Amount=$45.33, Acc=None, Type=DEBIT, Bal=$1,534.65
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9661', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L082 (24273)
  SMS: "Chase: Your requested balance for acct ...9384 is $5,810.80 as of 07/19."
  🧠 Human/Cognitive Thought: Executed financial transaction: $5,810.80 (DEBIT)
  ⚙️ Parser Extracted: Amount=$5,810.80, Acc=9384, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L083 (24273)
  SMS: "Chase: Direct deposit of $482.51 from IRS TREAS 310 TAX REF posted to acct ...9384 on 07/20. Avail bal: $6,293.31."
  🧠 Human/Cognitive Thought: Executed financial transaction: $482.51 (CREDIT)
  ⚙️ Parser Extracted: Amount=$482.51, Acc=9384, Type=CREDIT, Bal=$6,293.31
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L084 (24273)
  SMS: "Chase: Direct deposit of $2,008.22 from IRS TREAS 310 TAX REF posted to acct ...9384 on 07/20. Avail bal: $8,301.53."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,008.22 (CREDIT)
  ⚙️ Parser Extracted: Amount=$2,008.22, Acc=9384, Type=CREDIT, Bal=$8,301.53
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L085 (24273)
  SMS: "Chase: You made a $199.21 debit card purchase with card ending 882 at BEST BUY #442 on 07/21. Avail bal: $8,102.32."
  🧠 Human/Cognitive Thought: Executed financial transaction: $199.21 (DEBIT)
  ⚙️ Parser Extracted: Amount=$199.21, Acc=None, Type=DEBIT, Bal=$8,102.32
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L086 (322632)
  SMS: "Bank of America: A withdrawal/debit of $27.14 (T-MOBILE) posted to account ending 9661 on 07/22. Available balance: $1,507.51."
  🧠 Human/Cognitive Thought: Executed financial transaction: $27.14 (DEBIT)
  ⚙️ Parser Extracted: Amount=$27.14, Acc=None, Type=DEBIT, Bal=$1,507.51
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9661', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L087 (+14155550132)
  SMS: "Sent you the pics, check WhatsApp"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L088 (89887)
  SMS: "DOMINO'S: 50% OFF all pizzas this weekend only! Order at dominos.com. Txt STOP to opt out."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L089 (24273)
  SMS: "Chase: Direct deposit of $1,604.48 from VENMO CASHOUT posted to acct ...9384 on 07/24. Avail bal: $9,706.80."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,604.48 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,604.48, Acc=9384, Type=CREDIT, Bal=$9,706.80
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L090 (322632)
  SMS: "BofA: Payment of $493.62 received on credit card ending 9111. Thank you. Avail credit: $4,104.55."
  🧠 Human/Cognitive Thought: Executed financial transaction: $493.62 (CREDIT)
  ⚙️ Parser Extracted: Amount=$493.62, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,104.55', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L091 (322632)
  SMS: "Bank of America: A purchase of $187.67 at WHOLE FOODS MKT was charged to your credit card ending in 9111 on 07/25."
  🧠 Human/Cognitive Thought: Executed financial transaction: $187.67 (DEBIT)
  ⚙️ Parser Extracted: Amount=$187.67, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L092 (+14155550132)
  SMS: "Sent you the pics, check WhatsApp"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L093 (37777)
  SMS: "USPS: Your package 9424627155804702688 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L094 (24273)
  SMS: "Chase: You made a $72.31 debit card purchase with card ending 882 at TARGET T-1043 on 07/28. Avail bal: $9,140.87."
  🧠 Human/Cognitive Thought: Executed financial transaction: $72.31 (DEBIT)
  ⚙️ Parser Extracted: Amount=$72.31, Acc=None, Type=DEBIT, Bal=$9,140.87
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L095 (22000)
  SMS: "G-904062 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L096 (24273)
  SMS: "Chase: Direct deposit of $1,209.59 from IRS TREAS 310 TAX REF posted to acct ...9384 on 07/28. Avail bal: $10,350.46."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,209.59 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,209.59, Acc=9384, Type=CREDIT, Bal=$10,350.46
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L097 (322632)
  SMS: "BofA: Payment of $224.08 received on credit card ending 9111. Thank you. Avail credit: $4,140.96."
  🧠 Human/Cognitive Thought: Executed financial transaction: $224.08 (CREDIT)
  ⚙️ Parser Extracted: Amount=$224.08, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,140.96', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L098 (24273)
  SMS: "Chase: You sent $219.75 to JOHN MILLER with Zelle(R) from acct ending 9384 on 07/29. Avail bal: $9,906.63."
  🧠 Human/Cognitive Thought: Executed financial transaction: $219.75 (DEBIT)
  ⚙️ Parser Extracted: Amount=$219.75, Acc=None, Type=DEBIT, Bal=$9,906.63
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L099 (262966)
  SMS: "622976 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L100 (24273)
  SMS: "Chase: EMILY DAVIS sent you $208.87 with Zelle(R). Deposited to acct ...9384. Avail bal: $10,115.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $208.87 (CREDIT)
  ⚙️ Parser Extracted: Amount=$208.87, Acc=9384, Type=CREDIT, Bal=$10,115.50
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L101 (24273)
  SMS: "Chase Alert: A $214.71 card purchase was made at EXXONMOBIL 9921 with your debit card ending in 882 on 07/31. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $214.71 (DEBIT)
  ⚙️ Parser Extracted: Amount=$214.71, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L102 (24273)
  SMS: "Chase: Your one-time code is 553610. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L103 (322632)
  SMS: "Bank of America: A withdrawal/debit of $165.14 (CHECK #1042) posted to account ending 9661 on 08/01. Available balance: $1,342.37."
  🧠 Human/Cognitive Thought: Executed financial transaction: $165.14 (DEBIT)
  ⚙️ Parser Extracted: Amount=$165.14, Acc=None, Type=DEBIT, Bal=$1,342.37
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9661', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L104 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $85.28 with Zelle(R). Deposited to acct ...9384. Avail bal: $9,986.07."
  🧠 Human/Cognitive Thought: Executed financial transaction: $85.28 (CREDIT)
  ⚙️ Parser Extracted: Amount=$85.28, Acc=9384, Type=CREDIT, Bal=$9,986.07
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L105 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $719.11 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$719.11, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'amount': Expected='None', Parsed='719.11'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L106 (24273)
  SMS: "Chase: You scheduled a payment of $166.00 to COMCAST CABLE for 08/09 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $166.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$166.00, Acc=9384, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='CREDIT', Parsed='DEBIT'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L107 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $329.63 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$329.63, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'amount': Expected='None', Parsed='329.63'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L108 (322632)
  SMS: "Bank of America: A purchase of $295.77 at NETFLIX.COM was charged to your credit card ending in 9111 on 08/05."
  🧠 Human/Cognitive Thought: Executed financial transaction: $295.77 (DEBIT)
  ⚙️ Parser Extracted: Amount=$295.77, Acc=9111, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Chase', Parsed='Unknown'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L109 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for $330.79 at BEST BUY #442 on 08/06. Avail credit: $3,514.40. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $330.79 (DEBIT)
  ⚙️ Parser Extracted: Amount=$330.79, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,514.40', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L110 (322632)
  SMS: "BofA: Payment of $416.60 received on credit card ending 9111. Thank you. Avail credit: $3,931.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $416.60 (CREDIT)
  ⚙️ Parser Extracted: Amount=$416.60, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='3,931.00', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L111 (322632)
  SMS: "BofA: Payment of $264.58 received on credit card ending 9111. Thank you. Avail credit: $4,195.58."
  🧠 Human/Cognitive Thought: Executed financial transaction: $264.58 (CREDIT)
  ⚙️ Parser Extracted: Amount=$264.58, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Unknown'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'balance': Expected='4,195.58', Parsed='None'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L112 (24273)
  SMS: "Chase Alert: A $169.82 card purchase was made at EXXONMOBIL 9921 with your debit card ending in 882 on 08/07. If this wasn't you, call 1-800-935-9935."
  🧠 Human/Cognitive Thought: Executed financial transaction: $169.82 (DEBIT)
  ⚙️ Parser Extracted: Amount=$169.82, Acc=None, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_122147_usa_batch1_L113 (262966)
  SMS: "320109 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L114 (24273)
  SMS: "Chase: Your one-time code is 519182. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_122147_usa_batch1_L115 (24273)
  SMS: "Chase: You made a $124.95 debit card purchase with card ending 882 at NETFLIX.COM on 08/10. Avail bal: $9,010.12."
  🧠 Human/Cognitive Thought: Executed financial transaction: $124.95 (DEBIT)
  ⚙️ Parser Extracted: Amount=$124.95, Acc=None, Type=DEBIT, Bal=$9,010.12
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='882', Parsed='None'
-----------------------------------------------------------------

FINAL RESULT: 37/115 Passed (32.2% Accuracy)
=================================================================


```

---

## 3. Archival Record
- Raw sample moved to `samples/processed/20260820_122147_usa_batch1.xml`.
