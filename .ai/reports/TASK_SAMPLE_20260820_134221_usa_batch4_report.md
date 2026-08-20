# Task Execution Report: `TASK_SAMPLE_20260820_134221_usa_batch4`

---

## 1. Executive Summary
- **Source File**: `samples/usa_batch4.xml`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Agent**: `opencode` (via Sample Scooper)
- **Processed Messages**: 102
- **Accuracy**: 71.6%
- **Status**: `COMPLETED`
- **Execution Timestamp**: 2026-08-20T13:42:21.966833

---

## 2. Test Execution Output
```text

=================================================================
       US SMS TEST SUITE — COGNITIVE VS PARSER EVALUATION        
=================================================================

Running 102 Test Cases from: temp_TASK_SAMPLE_20260820_134221_usa_batch4.json

[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L001 (322632)
  SMS: "Bank of America: A purchase of $96.87 at TARGET T-1043 was charged to your credit card ending in 9111 on 11/20."
  🧠 Human/Cognitive Thought: Executed financial transaction: $96.87 (DEBIT)
  ⚙️ Parser Extracted: Amount=$96.87, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L002 (24273)
  SMS: "Chase: You sent $87.79 to MIKE CHEN with Zelle(R) from acct ending 9384 on 11/20. Avail bal: $17,649.19."
  🧠 Human/Cognitive Thought: Executed financial transaction: $87.79 (DEBIT)
  ⚙️ Parser Extracted: Amount=$87.79, Acc=9384, Type=DEBIT, Bal=$17,649.19
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L003 (24273)
  SMS: "Chase: You made a $96.69 debit card purchase with card ending 882 at BEST BUY #442 on 11/20. Avail bal: $17,552.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $96.69 (DEBIT)
  ⚙️ Parser Extracted: Amount=$96.69, Acc=882, Type=DEBIT, Bal=$17,552.50
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L004 (93557)
  SMS: "Wells Fargo: 212098 is your access code. It expires in 10 minutes. Don't share it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L005 (24273)
  SMS: "Chase: You made a $91.74 debit card purchase with card ending 882 at TRADER JOE'S #552 on 11/21. Avail bal: $17,460.76."
  🧠 Human/Cognitive Thought: Executed financial transaction: $91.74 (DEBIT)
  ⚙️ Parser Extracted: Amount=$91.74, Acc=882, Type=DEBIT, Bal=$17,460.76
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L006 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L007 (322632)
  SMS: "Bank of America: A purchase of $103.93 at STARBUCKS #2214 was charged to your credit card ending in 9111 on 11/22."
  🧠 Human/Cognitive Thought: Executed financial transaction: $103.93 (DEBIT)
  ⚙️ Parser Extracted: Amount=$103.93, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L008 (93557)
  SMS: "Wells Fargo: 156689 is your access code. It expires in 10 minutes. Don't share it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L009 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $414.89 at SHELL OIL 5744 on 11/23. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L010 (24273)
  SMS: "Chase: You scheduled a payment of $144.31 to COMCAST CABLE for 12/01 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $144.31 (CREDIT)
  ⚙️ Parser Extracted: Amount=$144.31, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L011 (24273)
  SMS: "Chase: You scheduled a payment of $103.69 to COMCAST CABLE for 12/02 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $103.69 (CREDIT)
  ⚙️ Parser Extracted: Amount=$103.69, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L012 (55123)
  SMS: "QuickCash Loans: Get up to $5,000 TODAY! No credit check. Apply now: qcloans.example/apply. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L013 (24273)
  SMS: "Chase: You made a $113.69 debit card purchase with card ending 882 at STARBUCKS #2214 on 11/25. Avail bal: $17,347.07."
  🧠 Human/Cognitive Thought: Executed financial transaction: $113.69 (DEBIT)
  ⚙️ Parser Extracted: Amount=$113.69, Acc=882, Type=DEBIT, Bal=$17,347.07
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L014 (24273)
  SMS: "Chase: Direct deposit of $1,339.22 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 11/25. Avail bal: $18,686.29."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,339.22 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,339.22, Acc=9384, Type=CREDIT, Bal=$18,686.29
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L015 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $114.43 at STARBUCKS #2214 on 11/25. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L016 (322632)
  SMS: "Bank of America: A purchase of $84.80 at STARBUCKS #2214 was charged to your credit card ending in 9111 on 11/26."
  🧠 Human/Cognitive Thought: Executed financial transaction: $84.80 (DEBIT)
  ⚙️ Parser Extracted: Amount=$84.80, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L017 (24273)
  SMS: "Chase: Your one-time code is 145287. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L018 (24273)
  SMS: "Chase: Direct deposit of $501.36 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 11/26. Avail bal: $19,187.65."
  🧠 Human/Cognitive Thought: Executed financial transaction: $501.36 (CREDIT)
  ⚙️ Parser Extracted: Amount=$501.36, Acc=9384, Type=CREDIT, Bal=$19,187.65
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L019 (24273)
  SMS: "Chase: You made a $69.30 debit card purchase with card ending 882 at SHELL OIL 5744 on 11/27. Avail bal: $19,118.35."
  🧠 Human/Cognitive Thought: Executed financial transaction: $69.30 (DEBIT)
  ⚙️ Parser Extracted: Amount=$69.30, Acc=882, Type=DEBIT, Bal=$19,118.35
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L020 (322632)
  SMS: "Bank of America: A purchase of $22.02 at TRADER JOE'S #552 was charged to your credit card ending in 9111 on 11/27."
  🧠 Human/Cognitive Thought: Executed financial transaction: $22.02 (DEBIT)
  ⚙️ Parser Extracted: Amount=$22.02, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L021 (24273)
  SMS: "Chase: You made a $57.71 debit card purchase with card ending 882 at SHELL OIL 5744 on 11/28. Avail bal: $19,060.64."
  🧠 Human/Cognitive Thought: Executed financial transaction: $57.71 (DEBIT)
  ⚙️ Parser Extracted: Amount=$57.71, Acc=882, Type=DEBIT, Bal=$19,060.64
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L022 (322632)
  SMS: "Bank of America: A purchase of $145.23 at UBER TRIP was charged to your credit card ending in 9111 on 11/28."
  🧠 Human/Cognitive Thought: Executed financial transaction: $145.23 (DEBIT)
  ⚙️ Parser Extracted: Amount=$145.23, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L023 (55123)
  SMS: "QuickCash Loans: Get up to $5,000 TODAY! No credit check. Apply now: qcloans.example/apply. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L024 (24273)
  SMS: "Chase: JOHN MILLER sent you $45.79 with Zelle(R). Deposited to acct ...9384. Avail bal: $19,106.43."
  🧠 Human/Cognitive Thought: Executed financial transaction: $45.79 (CREDIT)
  ⚙️ Parser Extracted: Amount=$45.79, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
     • Mismatch in 'balance': Expected='19,106.43', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L025 (322632)
  SMS: "Bank of America: A withdrawal/debit of $29.00 (GEICO AUTO INS) posted to account ending 9661 on 11/29. Available balance: $1,037.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $29.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$29.00, Acc=9661, Type=DEBIT, Bal=$1,037.50
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L026 (24273)
  SMS: "Chase: RAJ PATEL sent you $119.36 with Zelle(R). Deposited to acct ...9384. Avail bal: $19,225.79."
  🧠 Human/Cognitive Thought: Executed financial transaction: $119.36 (CREDIT)
  ⚙️ Parser Extracted: Amount=$119.36, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
     • Mismatch in 'balance': Expected='19,225.79', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L027 (24273)
  SMS: "Chase: You scheduled a payment of $174.59 to COMCAST CABLE for 12/08 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $174.59 (CREDIT)
  ⚙️ Parser Extracted: Amount=$174.59, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L028 (322632)
  SMS: "Bank of America: A withdrawal/debit of $27.18 (PG&E UTILITY) posted to account ending 9661 on 12/01. Available balance: $1,010.32."
  🧠 Human/Cognitive Thought: Executed financial transaction: $27.18 (DEBIT)
  ⚙️ Parser Extracted: Amount=$27.18, Acc=9661, Type=DEBIT, Bal=$1,010.32
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L029 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L030 (24273)
  SMS: "Chase: You made a $136.67 debit card purchase with card ending 882 at WALMART SUPERCENTER on 12/02. Avail bal: $19,089.12."
  🧠 Human/Cognitive Thought: Executed financial transaction: $136.67 (DEBIT)
  ⚙️ Parser Extracted: Amount=$136.67, Acc=882, Type=DEBIT, Bal=$19,089.12
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L031 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $845.00 at ELECTRONICS OUTLET ONLINE with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$845.00, Acc=882, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='882'
     • Mismatch in 'amount': Expected='None', Parsed='845.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L032 (24273)
  SMS: "Chase: Your debit card ending 882 has been CLOSED due to suspected fraud. A replacement card ending 7761 has been mailed to your address on file. No transaction was posted."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=882, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L033 (24273)
  SMS: "Chase: Your new debit card ending 7761 is now ACTIVE and linked to acct ...9384. Your old card ending 882 can no longer be used."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=7761, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L034 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $105.98 at ONLINE MERCHANT with debit card ending 7761? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$105.98, Acc=7761, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='7761'
     • Mismatch in 'amount': Expected='None', Parsed='105.98'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L035 (322632)
  SMS: "Bank of America: A withdrawal/debit of $30.52 (ATM WITHDRAWAL #A5521) posted to account ending 9661 on 12/05. Available balance: $979.80."
  🧠 Human/Cognitive Thought: Executed financial transaction: $30.52 (DEBIT)
  ⚙️ Parser Extracted: Amount=$30.52, Acc=9661, Type=DEBIT, Bal=$979.80
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L036 (322632)
  SMS: "Bank of America: A purchase of $23.99 at WHOLE FOODS MKT was charged to your credit card ending in 9111 on 12/05."
  🧠 Human/Cognitive Thought: Executed financial transaction: $23.99 (DEBIT)
  ⚙️ Parser Extracted: Amount=$23.99, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L037 (322632)
  SMS: "BofA: A pending authorization of $70.96 at UBER TRIP is on your credit card ending 9111. Final amount may vary."
  🧠 Human/Cognitive Thought: Executed financial transaction: $70.96 (DEBIT)
  ⚙️ Parser Extracted: Amount=$70.96, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L038 (322632)
  SMS: "Bank of America: AutoPay is now set up for your credit card ending in 9111. $35.00 (minimum due) will be drafted from account 9661 each cycle. No payment was made today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $35.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='True', Parsed='False'
     • Mismatch in 'account': Expected='9111', Parsed='None'
     • Mismatch in 'amount': Expected='35.00', Parsed='None'
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L039 (93557)
  SMS: "Welcome to Wells Fargo Alerts! You are now enrolled for account ending 4417. Msg&data rates may apply. Reply HELP for help."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=4417, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L040 (24273)
  SMS: "Chase: You transferred $1,000.00 from acct ...9384 to external account ending 4417 on 12/06. Avail bal: $18,089.12."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,000.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$1,000.00, Acc=9384, Type=DEBIT, Bal=$18,089.12
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L041 (93557)
  SMS: "Wells Fargo: A transfer of $1,000.00 was received into account ending 4417 on 12/07. Avail Bal: $1,000.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,000.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,000.00, Acc=4417, Type=CREDIT, Bal=$1,000.00
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L042 (24273)
  SMS: "Chase: You made a $92.64 debit card purchase with card ending 7761 at CHIPOTLE 1187 on 12/07. Avail bal: $17,996.48."
  🧠 Human/Cognitive Thought: Executed financial transaction: $92.64 (DEBIT)
  ⚙️ Parser Extracted: Amount=$92.64, Acc=7761, Type=DEBIT, Bal=$17,996.48
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L043 (24273)
  SMS: "Chase: You made a $67.26 debit card purchase with card ending 7761 at SHELL OIL 5744 on 12/07. Avail bal: $17,929.22."
  🧠 Human/Cognitive Thought: Executed financial transaction: $67.26 (DEBIT)
  ⚙️ Parser Extracted: Amount=$67.26, Acc=7761, Type=DEBIT, Bal=$17,929.22
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L044 (322632)
  SMS: "Bank of America: A withdrawal/debit of $55.19 (PG&E UTILITY) posted to account ending 9661 on 12/07. Available balance: $924.61."
  🧠 Human/Cognitive Thought: Executed financial transaction: $55.19 (DEBIT)
  ⚙️ Parser Extracted: Amount=$55.19, Acc=9661, Type=DEBIT, Bal=$924.61
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L045 (55123)
  SMS: "QuickCash Loans: Get up to $5,000 TODAY! No credit check. Apply now: qcloans.example/apply. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L046 (24273)
  SMS: "Chase: You scheduled a payment of $212.70 to COMCAST CABLE for 12/16 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $212.70 (CREDIT)
  ⚙️ Parser Extracted: Amount=$212.70, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L047 (24273)
  SMS: "Chase: You made a $60.00 debit card purchase (includes $20.00 cash back) with card ending 7761 at KROGER #771 on 12/09. Avail bal: $17,869.22."
  🧠 Human/Cognitive Thought: Executed financial transaction: $60.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$60.00, Acc=7761, Type=DEBIT, Bal=$17,869.22
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L048 (322632)
  SMS: "Bank of America: A withdrawal/debit of $56.25 (T-MOBILE) posted to account ending 9661 on 12/10. Available balance: $868.36."
  🧠 Human/Cognitive Thought: Executed financial transaction: $56.25 (DEBIT)
  ⚙️ Parser Extracted: Amount=$56.25, Acc=9661, Type=DEBIT, Bal=$868.36
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L049 (24273)
  SMS: "Chase: You made a $20.35 debit card purchase with card ending 7761 at CVS/PHARMACY #883 on 12/10. Avail bal: $17,848.87."
  🧠 Human/Cognitive Thought: Executed financial transaction: $20.35 (DEBIT)
  ⚙️ Parser Extracted: Amount=$20.35, Acc=7761, Type=DEBIT, Bal=$17,848.87
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L050 (24273)
  SMS: "Chase: Your online payment of $389.57 to BANK OF AMERICA CARD 9111 from acct ...9384 is complete on 12/10. Avail bal: $17,459.30."
  🧠 Human/Cognitive Thought: Executed financial transaction: $389.57 (CREDIT)
  ⚙️ Parser Extracted: Amount=$389.57, Acc=9384, Type=CREDIT, Bal=$17,459.30
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Chase'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L051 (322632)
  SMS: "BofA: Payment of $389.57 received on credit card ending 9111. Thank you. Avail credit: $759.77."
  🧠 Human/Cognitive Thought: Executed financial transaction: $389.57 (CREDIT)
  ⚙️ Parser Extracted: Amount=$389.57, Acc=9111, Type=CREDIT, Bal=$759.77
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L052 (24273)
  SMS: "Chase: Direct deposit of $1,610.22 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 12/11. Avail bal: $19,069.52."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,610.22 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,610.22, Acc=9384, Type=CREDIT, Bal=$19,069.52
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L053 (24273)
  SMS: "Chase: You made a $141.54 debit card purchase with card ending 7761 at UBER TRIP on 12/11. Avail bal: $18,927.98."
  🧠 Human/Cognitive Thought: Executed financial transaction: $141.54 (DEBIT)
  ⚙️ Parser Extracted: Amount=$141.54, Acc=7761, Type=DEBIT, Bal=$18,927.98
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L054 (322632)
  SMS: "Bank of America: An international purchase of $49.12 (EUR 45.00) at CAFE DE PARIS was charged to your credit card ending in 9111 on 12/12. Avail credit: $710.65."
  🧠 Human/Cognitive Thought: Executed financial transaction: $49.12 (DEBIT)
  ⚙️ Parser Extracted: Amount=$49.12, Acc=9111, Type=DEBIT, Bal=$710.65
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L055 (322632)
  SMS: "Bank of America: A purchase of $113.96 at UBER TRIP was charged to your credit card ending in 9111 on 12/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $113.96 (DEBIT)
  ⚙️ Parser Extracted: Amount=$113.96, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L056 (+14155550132)
  SMS: "Movie at 8?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L057 (322632)
  SMS: "BofA: A pending authorization of $100.41 at SHELL OIL 5744 is on your credit card ending 9111. Final amount may vary."
  🧠 Human/Cognitive Thought: Executed financial transaction: $100.41 (DEBIT)
  ⚙️ Parser Extracted: Amount=$100.41, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L058 (24273)
  SMS: "Chase: You sent $33.95 to RAJ PATEL with Zelle(R) from acct ending 9384 on 12/14. Avail bal: $18,894.03."
  🧠 Human/Cognitive Thought: Executed financial transaction: $33.95 (DEBIT)
  ⚙️ Parser Extracted: Amount=$33.95, Acc=9384, Type=DEBIT, Bal=$18,894.03
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L059 (24273)
  SMS: "Chase: You made a $121.23 debit card purchase with card ending 7761 at CVS/PHARMACY #883 on 12/14. Avail bal: $18,772.80."
  🧠 Human/Cognitive Thought: Executed financial transaction: $121.23 (DEBIT)
  ⚙️ Parser Extracted: Amount=$121.23, Acc=7761, Type=DEBIT, Bal=$18,772.80
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L060 (322632)
  SMS: "BofA: A Late Payment Fee of $29.00 was charged to your credit card ending 9111 on 12/15."
  🧠 Human/Cognitive Thought: Executed financial transaction: $29.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$29.00, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L061 (322632)
  SMS: "BofA: Interest Charged of $41.23 was posted to your credit card ending 9111 on 12/15."
  🧠 Human/Cognitive Thought: Executed financial transaction: $41.23 (DEBIT)
  ⚙️ Parser Extracted: Amount=$41.23, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L062 (24273)
  SMS: "Chase: Your online payment of $357.86 to BANK OF AMERICA CARD 9111 from acct ...9384 is complete on 12/15. Avail bal: $18,414.94."
  🧠 Human/Cognitive Thought: Executed financial transaction: $357.86 (CREDIT)
  ⚙️ Parser Extracted: Amount=$357.86, Acc=9384, Type=CREDIT, Bal=$18,414.94
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Chase'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L063 (322632)
  SMS: "BofA: Payment of $357.86 received on credit card ending 9111. Thank you. Avail credit: $884.32."
  🧠 Human/Cognitive Thought: Executed financial transaction: $357.86 (CREDIT)
  ⚙️ Parser Extracted: Amount=$357.86, Acc=9111, Type=CREDIT, Bal=$884.32
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L064 (24273)
  SMS: "Chase: You sent $10.04 to RAJ PATEL with Zelle(R) from acct ending 9384 on 12/16. Avail bal: $18,404.90."
  🧠 Human/Cognitive Thought: Executed financial transaction: $10.04 (DEBIT)
  ⚙️ Parser Extracted: Amount=$10.04, Acc=9384, Type=DEBIT, Bal=$18,404.90
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L065 (24273)
  SMS: "Chase: JOHN MILLER requested $22.44 from you with Zelle(R). Review in the Chase Mobile app. No money has moved."
  🧠 Human/Cognitive Thought: Executed financial transaction: $22.44 (DEBIT)
  ⚙️ Parser Extracted: Amount=$22.44, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L066 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 12/23. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L067 (24273)
  SMS: "Chase: Your one-time code is 765514. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L068 (322632)
  SMS: "Bank of America: A payment of $200.00 to BofA CARD 9111 was debited from account ending 9661 on 12/17. Available balance: $668.36."
  🧠 Human/Cognitive Thought: Executed financial transaction: $200.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$200.00, Acc=9661, Type=CREDIT, Bal=$668.36
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L069 (322632)
  SMS: "BofA: Payment of $200.00 received on credit card ending 9111. Thank you. Avail credit: $1,084.32."
  🧠 Human/Cognitive Thought: Executed financial transaction: $200.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$200.00, Acc=9111, Type=CREDIT, Bal=$1,084.32
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L070 (322632)
  SMS: "BofA: Your $200.00 payment on credit card ending 9111 was RETURNED (insufficient funds) and reversed on 12/19. Avail credit: $884.32."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L071 (322632)
  SMS: "Bank of America: $200.00 was credited back to account ending 9661 (returned payment) on 12/19. Available balance: $868.36."
  🧠 Human/Cognitive Thought: Executed financial transaction: $200.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$200.00, Acc=9661, Type=CREDIT, Bal=$868.36
  ⚠️ DISCREPANCIES:
     • Mismatch in 'source': Expected='CARD', Parsed='BANK'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L072 (322632)
  SMS: "Bank of America: A purchase of $106.20 at STARBUCKS #2214 was charged to your credit card ending in 9111 on 12/19."
  🧠 Human/Cognitive Thought: Executed financial transaction: $106.20 (DEBIT)
  ⚙️ Parser Extracted: Amount=$106.20, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L073 (24273)
  SMS: "Chase: You made a $101.19 debit card purchase with card ending 7761 at BEST BUY #442 on 12/20. Avail bal: $18,303.71."
  🧠 Human/Cognitive Thought: Executed financial transaction: $101.19 (DEBIT)
  ⚙️ Parser Extracted: Amount=$101.19, Acc=7761, Type=DEBIT, Bal=$18,303.71
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L074 (55123)
  SMS: "QuickCash Loans: Get up to $5,000 TODAY! No credit check. Apply now: qcloans.example/apply. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='CARD', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L075 (24273)
  SMS: "Chase: Direct deposit of $2,082.11 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 12/20. Avail bal: $20,385.82."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,082.11 (CREDIT)
  ⚙️ Parser Extracted: Amount=$2,082.11, Acc=9384, Type=CREDIT, Bal=$20,385.82
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L076 (24273)
  SMS: "Chase: You scheduled a payment of $83.03 to COMCAST CABLE for 12/29 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $83.03 (CREDIT)
  ⚙️ Parser Extracted: Amount=$83.03, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L077 (322632)
  SMS: "Bank of America: Interest Paid of $1.12 was credited to account ending 9661 on 12/21. Available balance: $869.48."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1.12 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1.12, Acc=9661, Type=CREDIT, Bal=$869.48
  ⚠️ DISCREPANCIES:
     • Mismatch in 'source': Expected='CARD', Parsed='BANK'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L078 (24273)
  SMS: "Chase: MIKE CHEN sent you $121.80 with Zelle(R). Deposited to acct ...9384. Avail bal: $20,507.62."
  🧠 Human/Cognitive Thought: Executed financial transaction: $121.80 (CREDIT)
  ⚙️ Parser Extracted: Amount=$121.80, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
     • Mismatch in 'balance': Expected='20,507.62', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L079 (262966)
  SMS: "384605 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L080 (37777)
  SMS: "USPS: Your package 9420647491911254695 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L081 (93557)
  SMS: "Wells Fargo: 186720 is your access code. It expires in 10 minutes. Don't share it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L082 (262966)
  SMS: "729671 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L083 (322632)
  SMS: "Bank of America: A Monthly Maintenance Fee of $12.00 was charged to account ending 9661 on 12/24. Available balance: $857.48."
  🧠 Human/Cognitive Thought: Executed financial transaction: $12.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$12.00, Acc=9661, Type=DEBIT, Bal=$857.48
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L084 (322632)
  SMS: "Bank of America: Your Monthly Maintenance Fee of $12.00 was REFUNDED to account ending 9661 on 12/25. Available balance: $869.48."
  🧠 Human/Cognitive Thought: Executed financial transaction: $12.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$12.00, Acc=9661, Type=CREDIT, Bal=$869.48
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L085 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $143.52 at ONLINE MERCHANT with debit card ending 7761? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$143.52, Acc=7761, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='7761'
     • Mismatch in 'amount': Expected='None', Parsed='143.52'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L086 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L087 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,421.88. Min payment $35.00 due 01/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,421.88 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,421.88, Acc=9111, Type=DEBIT, Bal=$3,421.88
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L088 (24273)
  SMS: "Chase: RAJ PATEL sent you $156.31 with Zelle(R). Deposited to acct ...9384. Avail bal: $20,663.93."
  🧠 Human/Cognitive Thought: Executed financial transaction: $156.31 (CREDIT)
  ⚙️ Parser Extracted: Amount=$156.31, Acc=None, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'account': Expected='9384', Parsed='None'
     • Mismatch in 'balance': Expected='20,663.93', Parsed='None'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L089 (24273)
  SMS: "Chase: You sent $174.91 to MIKE CHEN with Zelle(R) from acct ending 9384 on 12/27. Avail bal: $20,489.02."
  🧠 Human/Cognitive Thought: Executed financial transaction: $174.91 (DEBIT)
  ⚙️ Parser Extracted: Amount=$174.91, Acc=9384, Type=DEBIT, Bal=$20,489.02
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L090 (322632)
  SMS: "Bank of America: A cash deposit of $300.00 was made at ATM #B2231 to account ending 9661 on 12/27. Available balance: $1,169.48."
  🧠 Human/Cognitive Thought: Executed financial transaction: $300.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$300.00, Acc=9661, Type=CREDIT, Bal=$1,169.48
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L091 (262966)
  SMS: "440588 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L092 (24273)
  SMS: "Chase: You made a $68.03 debit card purchase with card ending 7761 at SPOTIFY USA on 12/27. Avail bal: $20,420.99."
  🧠 Human/Cognitive Thought: Executed financial transaction: $68.03 (DEBIT)
  ⚙️ Parser Extracted: Amount=$68.03, Acc=7761, Type=DEBIT, Bal=$20,420.99
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L093 (24273)
  SMS: "Chase: You made a $97.58 debit card purchase with card ending 7761 at STARBUCKS #2214 on 12/28. Avail bal: $20,323.41."
  🧠 Human/Cognitive Thought: Executed financial transaction: $97.58 (DEBIT)
  ⚙️ Parser Extracted: Amount=$97.58, Acc=7761, Type=DEBIT, Bal=$20,323.41
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L094 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L095 (24273)
  SMS: "Chase: You made a $10.03 debit card purchase with card ending 7761 at APPLE.COM/BILL on 12/29. Avail bal: $20,313.38."
  🧠 Human/Cognitive Thought: Executed financial transaction: $10.03 (DEBIT)
  ⚙️ Parser Extracted: Amount=$10.03, Acc=7761, Type=DEBIT, Bal=$20,313.38
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L096 (322632)
  SMS: "Bank of America: A purchase of $66.67 at TRADER JOE'S #552 was charged to your credit card ending in 9111 on 12/29."
  🧠 Human/Cognitive Thought: Executed financial transaction: $66.67 (DEBIT)
  ⚙️ Parser Extracted: Amount=$66.67, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L097 (24273)
  SMS: "Chase: You made a $50.49 debit card purchase with card ending 7761 at BEST BUY #442 on 12/29. Avail bal: $20,262.89."
  🧠 Human/Cognitive Thought: Executed financial transaction: $50.49 (DEBIT)
  ⚙️ Parser Extracted: Amount=$50.49, Acc=7761, Type=DEBIT, Bal=$20,262.89
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L098 (322632)
  SMS: "Bank of America: A purchase of $11.95 at TARGET T-1043 was charged to your credit card ending in 9111 on 12/30."
  🧠 Human/Cognitive Thought: Executed financial transaction: $11.95 (DEBIT)
  ⚙️ Parser Extracted: Amount=$11.95, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L099 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,500.50. Min payment $35.00 due 01/18."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,500.50 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,500.50, Acc=9111, Type=DEBIT, Bal=$3,500.50
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_134221_usa_batch4_L100 (93557)
  SMS: "Wells Fargo: 206830 is your access code. It expires in 10 minutes. Don't share it."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L101 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_134221_usa_batch4_L102 (322632)
  SMS: "Bank of America: A purchase of $161.80 at BEST BUY #442 was charged to your credit card ending in 9111 on 01/01."
  🧠 Human/Cognitive Thought: Executed financial transaction: $161.80 (DEBIT)
  ⚙️ Parser Extracted: Amount=$161.80, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------

FINAL RESULT: 73/102 Passed (71.6% Accuracy)
=================================================================


```

---

## 3. Archival Record
- Raw sample moved to `samples/processed/20260820_134221_usa_batch4.xml`.
