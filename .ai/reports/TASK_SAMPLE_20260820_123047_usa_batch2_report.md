# Task Execution Report: `TASK_SAMPLE_20260820_123047_usa_batch2`

---

## 1. Executive Summary
- **Source File**: `samples/usa_batch2.xml`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Agent**: `opencode` (via Sample Scooper)
- **Processed Messages**: 105
- **Accuracy**: 87.6%
- **Status**: `COMPLETED`
- **Execution Timestamp**: 2026-08-20T12:30:47.364960

---

## 2. Test Execution Output
```text

=================================================================
       US SMS TEST SUITE — COGNITIVE VS PARSER EVALUATION        
=================================================================

Running 105 Test Cases from: temp_TASK_SAMPLE_20260820_123047_usa_batch2.json

[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L001 (322632)
  SMS: "Bank of America: A purchase of $242.58 at KROGER #771 was charged to your credit card ending in 9111 on 08/21."
  🧠 Human/Cognitive Thought: Executed financial transaction: $242.58 (DEBIT)
  ⚙️ Parser Extracted: Amount=$242.58, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L002 (322632)
  SMS: "Bank of America: A purchase of $198.89 at KROGER #771 was charged to your credit card ending in 9111 on 08/22."
  🧠 Human/Cognitive Thought: Executed financial transaction: $198.89 (DEBIT)
  ⚙️ Parser Extracted: Amount=$198.89, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L003 (322632)
  SMS: "Bank of America: A withdrawal/debit of $71.87 (ATM WITHDRAWAL #A5521) posted to account ending 9661 on 08/23. Available balance: $1,270.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $71.87 (DEBIT)
  ⚙️ Parser Extracted: Amount=$71.87, Acc=9661, Type=DEBIT, Bal=$1,270.50
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L004 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $279.92 at PANERA BREAD #4402 on 08/23. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L005 (24273)
  SMS: "Chase: You made a $13.26 debit card purchase with card ending 882 at WALMART SUPERCENTER on 08/24. Avail bal: $8,996.86."
  🧠 Human/Cognitive Thought: Executed financial transaction: $13.26 (DEBIT)
  ⚙️ Parser Extracted: Amount=$13.26, Acc=882, Type=DEBIT, Bal=$8,996.86
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L006 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L007 (24273)
  SMS: "Chase: A refund of $23.45 from AMAZON MKTPLACE was credited to your debit card ending 882 on 08/25. Avail bal: $9,020.31."
  🧠 Human/Cognitive Thought: Executed financial transaction: $23.45 (CREDIT)
  ⚙️ Parser Extracted: Amount=$23.45, Acc=882, Type=CREDIT, Bal=$9,020.31
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L008 (322632)
  SMS: "Bank of America: A purchase of $206.12 at KROGER #771 was charged to your credit card ending in 9111 on 08/26."
  🧠 Human/Cognitive Thought: Executed financial transaction: $206.12 (DEBIT)
  ⚙️ Parser Extracted: Amount=$206.12, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L009 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 09/01. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L010 (24273)
  SMS: "Chase: You made a $116.49 debit card purchase with card ending 882 at UBER TRIP on 08/26. Avail bal: $8,903.82."
  🧠 Human/Cognitive Thought: Executed financial transaction: $116.49 (DEBIT)
  ⚙️ Parser Extracted: Amount=$116.49, Acc=882, Type=DEBIT, Bal=$8,903.82
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L011 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $156.02 at WHOLE FOODS MKT on 08/27. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L012 (24273)
  SMS: "Chase: You scheduled a payment of $190.41 to COMCAST CABLE for 09/04 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $190.41 (CREDIT)
  ⚙️ Parser Extracted: Amount=$190.41, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L013 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L014 (322632)
  SMS: "BofA: A pending authorization of $64.10 at DELTA AIR 0062341 is on your credit card ending 9111. Final amount may vary."
  🧠 Human/Cognitive Thought: Executed financial transaction: $64.10 (DEBIT)
  ⚙️ Parser Extracted: Amount=$64.10, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L015 (322632)
  SMS: "Bank of America: A purchase of $64.10 at DELTA AIR 0062341 was charged to your credit card ending in 9111 on 08/28."
  🧠 Human/Cognitive Thought: Executed financial transaction: $64.10 (DEBIT)
  ⚙️ Parser Extracted: Amount=$64.10, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L016 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $82.62 at AMAZON MKTPLACE on 08/29. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L017 (24273)
  SMS: "Chase: You made a $110.07 debit card purchase with card ending 882 at APPLE.COM/BILL on 08/30. Avail bal: $8,793.75."
  🧠 Human/Cognitive Thought: Executed financial transaction: $110.07 (DEBIT)
  ⚙️ Parser Extracted: Amount=$110.07, Acc=882, Type=DEBIT, Bal=$8,793.75
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L018 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $716.11. Min payment $35.00 due 09/18."
  🧠 Human/Cognitive Thought: Executed financial transaction: $716.11 (DEBIT)
  ⚙️ Parser Extracted: Amount=$716.11, Acc=9111, Type=DEBIT, Bal=$716.11
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L019 (24273)
  SMS: "Chase: You made a $70.14 debit card purchase with card ending 882 at STARBUCKS #2214 on 08/31. Avail bal: $8,723.61."
  🧠 Human/Cognitive Thought: Executed financial transaction: $70.14 (DEBIT)
  ⚙️ Parser Extracted: Amount=$70.14, Acc=882, Type=DEBIT, Bal=$8,723.61
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L020 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $76.13 with Zelle(R). Deposited to acct ...9384. Avail bal: $8,799.74."
  🧠 Human/Cognitive Thought: Executed financial transaction: $76.13 (CREDIT)
  ⚙️ Parser Extracted: Amount=$76.13, Acc=9384, Type=CREDIT, Bal=$8,799.74
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L021 (22000)
  SMS: "G-964394 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L022 (322632)
  SMS: "Bank of America: AutoPay payment of $150.00 was made to your credit card ending 9111 from account 9661 on 09/02. Avail bal acct 9661: $1,120.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $150.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$150.00, Acc=9111, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L023 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 09/08. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L024 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $566.11. Min payment $35.00 due 09/20."
  🧠 Human/Cognitive Thought: Executed financial transaction: $566.11 (DEBIT)
  ⚙️ Parser Extracted: Amount=$566.11, Acc=9111, Type=DEBIT, Bal=$566.11
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L025 (24273)
  SMS: "Chase: Direct deposit of $456.34 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 09/03. Avail bal: $9,256.08."
  🧠 Human/Cognitive Thought: Executed financial transaction: $456.34 (CREDIT)
  ⚙️ Parser Extracted: Amount=$456.34, Acc=9384, Type=CREDIT, Bal=$9,256.08
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L026 (262966)
  SMS: "792149 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L027 (22000)
  SMS: "G-424197 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L028 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $566.11. Min payment $35.00 due 09/22."
  🧠 Human/Cognitive Thought: Executed financial transaction: $566.11 (DEBIT)
  ⚙️ Parser Extracted: Amount=$566.11, Acc=9111, Type=DEBIT, Bal=$566.11
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L029 (322632)
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
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L030 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L031 (24273)
  SMS: "Chase: Direct deposit of $2,112.31 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 09/06. Avail bal: $11,368.39."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,112.31 (CREDIT)
  ⚙️ Parser Extracted: Amount=$2,112.31, Acc=9384, Type=CREDIT, Bal=$11,368.39
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L032 (22000)
  SMS: "G-765616 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L033 (24273)
  SMS: "Chase: You made a $42.03 debit card purchase with card ending 882 at CHIPOTLE 1187 on 09/08. Avail bal: $11,326.36."
  🧠 Human/Cognitive Thought: Executed financial transaction: $42.03 (DEBIT)
  ⚙️ Parser Extracted: Amount=$42.03, Acc=882, Type=DEBIT, Bal=$11,326.36
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L034 (322632)
  SMS: "Bank of America: A purchase of $297.53 at KROGER #771 was charged to your credit card ending in 9111 on 09/08."
  🧠 Human/Cognitive Thought: Executed financial transaction: $297.53 (DEBIT)
  ⚙️ Parser Extracted: Amount=$297.53, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L035 (322632)
  SMS: "Bank of America: A purchase of $133.07 at TARGET T-1043 was charged to your credit card ending in 9111 on 09/08."
  🧠 Human/Cognitive Thought: Executed financial transaction: $133.07 (DEBIT)
  ⚙️ Parser Extracted: Amount=$133.07, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L036 (24273)
  SMS: "Chase: You transferred $500.00 from acct ...9384 to external account ending 9661 on 09/09. Avail bal: $10,826.36."
  🧠 Human/Cognitive Thought: Executed financial transaction: $500.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$500.00, Acc=9384, Type=DEBIT, Bal=$10,826.36
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L037 (322632)
  SMS: "Bank of America: A transfer of $500.00 was received into account ending 9661 on 09/09. Available balance: $1,620.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $500.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$500.00, Acc=9661, Type=CREDIT, Bal=$1,620.50
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L038 (+14155550132)
  SMS: "Hey are we still on for dinner tonight?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L039 (322632)
  SMS: "Bank of America: A purchase of $137.61 at SPOTIFY USA was charged to your credit card ending in 9111 on 09/10."
  🧠 Human/Cognitive Thought: Executed financial transaction: $137.61 (DEBIT)
  ⚙️ Parser Extracted: Amount=$137.61, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L040 (262966)
  SMS: "619585 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L041 (24273)
  SMS: "Chase: You scheduled a payment of $151.84 to COMCAST CABLE for 09/19 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $151.84 (CREDIT)
  ⚙️ Parser Extracted: Amount=$151.84, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L042 (24273)
  SMS: "Chase: You made a $138.01 debit card purchase with card ending 882 at PANERA BREAD #4402 on 09/11. Avail bal: $10,688.35."
  🧠 Human/Cognitive Thought: Executed financial transaction: $138.01 (DEBIT)
  ⚙️ Parser Extracted: Amount=$138.01, Acc=882, Type=DEBIT, Bal=$10,688.35
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L043 (24273)
  SMS: "Chase: Direct deposit of $2,249.23 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 09/12. Avail bal: $12,937.58."
  🧠 Human/Cognitive Thought: Executed financial transaction: $2,249.23 (CREDIT)
  ⚙️ Parser Extracted: Amount=$2,249.23, Acc=9384, Type=CREDIT, Bal=$12,937.58
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L044 (24273)
  SMS: "Chase: You transferred $200.00 from acct ...9384 to WELLS FARGO acct ...4417 on 09/12. Avail bal: $12,737.58."
  🧠 Human/Cognitive Thought: Executed financial transaction: $200.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$200.00, Acc=9384, Type=DEBIT, Bal=$12,737.58
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L045 (24273)
  SMS: "Chase: Your transfer of $200.00 to acct ...4417 FAILED and was reversed to acct ...9384. Avail bal: $12,937.58."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L046 (24273)
  SMS: "Chase: You sent $168.43 to SARAH LOPEZ with Zelle(R) from acct ending 9384 on 09/13. Avail bal: $12,769.15."
  🧠 Human/Cognitive Thought: Executed financial transaction: $168.43 (DEBIT)
  ⚙️ Parser Extracted: Amount=$168.43, Acc=9384, Type=DEBIT, Bal=$12,769.15
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L047 (24273)
  SMS: "Chase: You scheduled a payment of $141.87 to COMCAST CABLE for 09/21 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $141.87 (CREDIT)
  ⚙️ Parser Extracted: Amount=$141.87, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L048 (24273)
  SMS: "Chase: EMILY DAVIS sent you $44.83 with Zelle(R). Deposited to acct ...9384. Avail bal: $12,813.98."
  🧠 Human/Cognitive Thought: Executed financial transaction: $44.83 (CREDIT)
  ⚙️ Parser Extracted: Amount=$44.83, Acc=9384, Type=CREDIT, Bal=$12,813.98
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L049 (24273)
  SMS: "Chase: You scheduled a payment of $276.99 to COMCAST CABLE for 09/22 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $276.99 (CREDIT)
  ⚙️ Parser Extracted: Amount=$276.99, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L050 (322632)
  SMS: "Bank of America: A withdrawal/debit of $147.93 (GEICO AUTO INS) posted to account ending 9661 on 09/15. Available balance: $1,472.57."
  🧠 Human/Cognitive Thought: Executed financial transaction: $147.93 (DEBIT)
  ⚙️ Parser Extracted: Amount=$147.93, Acc=9661, Type=DEBIT, Bal=$1,472.57
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L051 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L052 (24273)
  SMS: "Chase: EMILY DAVIS requested $64.07 from you with Zelle(R). Review in the Chase Mobile app. No money has moved."
  🧠 Human/Cognitive Thought: Executed financial transaction: $64.07 (DEBIT)
  ⚙️ Parser Extracted: Amount=$64.07, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L053 (24273)
  SMS: "Chase: You made a $69.81 debit card purchase with card ending 882 at PANERA BREAD #4402 on 09/17. Avail bal: $12,744.17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $69.81 (DEBIT)
  ⚙️ Parser Extracted: Amount=$69.81, Acc=882, Type=DEBIT, Bal=$12,744.17
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L054 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $140.47 with Zelle(R). Deposited to acct ...9384. Avail bal: $12,884.64."
  🧠 Human/Cognitive Thought: Executed financial transaction: $140.47 (CREDIT)
  ⚙️ Parser Extracted: Amount=$140.47, Acc=9384, Type=CREDIT, Bal=$12,884.64
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L055 (24273)
  SMS: "Chase: You scheduled a payment of $186.35 to COMCAST CABLE for 09/26 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $186.35 (CREDIT)
  ⚙️ Parser Extracted: Amount=$186.35, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L056 (+14155550132)
  SMS: "Happy birthday!! 🎉"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L057 (322632)
  SMS: "Bank of America: A withdrawal/debit of $96.12 (PG&E UTILITY) posted to account ending 9661 on 09/20. Available balance: $1,376.45."
  🧠 Human/Cognitive Thought: Executed financial transaction: $96.12 (DEBIT)
  ⚙️ Parser Extracted: Amount=$96.12, Acc=9661, Type=DEBIT, Bal=$1,376.45
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L058 (322632)
  SMS: "Bank of America: A purchase of $221.70 at AMAZON MKTPLACE was charged to your credit card ending in 9111 on 09/20."
  🧠 Human/Cognitive Thought: Executed financial transaction: $221.70 (DEBIT)
  ⚙️ Parser Extracted: Amount=$221.70, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L059 (322632)
  SMS: "Bank of America: An Overdraft Item Fee of $35 was assessed to account ending 9661 on 09/20. Available balance: $1,341.45."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,341.45 (DEBIT)
  ⚙️ Parser Extracted: Amount=$1,341.45, Acc=9661, Type=DEBIT, Bal=$1,341.45
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L060 (24273)
  SMS: "Chase: You made a $70.93 debit card purchase with card ending 882 at DELTA AIR 0062341 on 09/20. Avail bal: $12,813.71."
  🧠 Human/Cognitive Thought: Executed financial transaction: $70.93 (DEBIT)
  ⚙️ Parser Extracted: Amount=$70.93, Acc=882, Type=DEBIT, Bal=$12,813.71
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L061 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 09/27. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L062 (24273)
  SMS: "Chase: Direct deposit of $1,663.43 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 09/21. Avail bal: $14,477.14."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,663.43 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,663.43, Acc=9384, Type=CREDIT, Bal=$14,477.14
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L063 (262966)
  SMS: "284377 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L064 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $323.75 at UBER TRIP on 09/22. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L065 (24273)
  SMS: "Chase: You made a $57.47 debit card purchase with card ending 882 at TRADER JOE'S #552 on 09/22. Avail bal: $14,419.67."
  🧠 Human/Cognitive Thought: Executed financial transaction: $57.47 (DEBIT)
  ⚙️ Parser Extracted: Amount=$57.47, Acc=882, Type=DEBIT, Bal=$14,419.67
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L066 (24273)
  SMS: "Chase: A mobile check deposit of $1,250.00 was received for acct ...9384 on 09/23. Avail bal: $15,669.67."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,250.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,250.00, Acc=9384, Type=CREDIT, Bal=$15,669.67
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L067 (24273)
  SMS: "Chase: A hold has been placed on part of your recent deposit to acct ...9384. $250.00 available now; remainder available in 2 business days."
  🧠 Human/Cognitive Thought: Executed financial transaction: $250.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$250.00, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L068 (24273)
  SMS: "Chase: RAJ PATEL sent you $144.21 with Zelle(R). Deposited to acct ...9384. Avail bal: $15,813.88."
  🧠 Human/Cognitive Thought: Executed financial transaction: $144.21 (CREDIT)
  ⚙️ Parser Extracted: Amount=$144.21, Acc=9384, Type=CREDIT, Bal=$15,813.88
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L069 (322632)
  SMS: "Bank of America: A purchase of $9.32 at STARBUCKS #2214 was charged to your credit card ending in 9111 on 09/24."
  🧠 Human/Cognitive Thought: Executed financial transaction: $9.32 (DEBIT)
  ⚙️ Parser Extracted: Amount=$9.32, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L070 (24273)
  SMS: "Chase: You scheduled a payment of $211.20 to COMCAST CABLE for 10/02 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $211.20 (CREDIT)
  ⚙️ Parser Extracted: Amount=$211.20, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L071 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $339.70 at TRADER JOE'S #552 on 09/25. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L072 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L073 (322632)
  SMS: "Bank of America: A withdrawal/debit of $127.77 (ATM WITHDRAWAL #A5521) posted to account ending 9661 on 09/26. Available balance: $1,213.68."
  🧠 Human/Cognitive Thought: Executed financial transaction: $127.77 (DEBIT)
  ⚙️ Parser Extracted: Amount=$127.77, Acc=9661, Type=DEBIT, Bal=$1,213.68
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L074 (322632)
  SMS: "BofA Alert: Credit card ending 9111 was used for 1234.56 USD at BEST BUY #442 on 09/27. Avail credit: $1,600.10. Fraud? Call 800.732.9194."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,600.10 (DEBIT)
  ⚙️ Parser Extracted: Amount=$1,600.10, Acc=9111, Type=DEBIT, Bal=$1,600.10
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L075 (24273)
  SMS: "Chase: You made a $.99 debit card purchase with card ending 882 at APPLE.COM/BILL on 09/27. Avail bal: $15,812.89."
  🧠 Human/Cognitive Thought: Executed financial transaction: $15,812.89 (DEBIT)
  ⚙️ Parser Extracted: Amount=$15,812.89, Acc=882, Type=DEBIT, Bal=$15,812.89
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L076 (24273)
  SMS: "Chase: You made a $161.23 debit card purchase with card ending 882 at 7-ELEVEN 33481 on 09/28. Avail bal: $15,651.66."
  🧠 Human/Cognitive Thought: Executed financial transaction: $161.23 (DEBIT)
  ⚙️ Parser Extracted: Amount=$161.23, Acc=882, Type=DEBIT, Bal=$15,651.66
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L077 (322632)
  SMS: "Bank of America: A purchase of $290.87 at SHELL OIL 5744 was charged to your credit card ending in 9111 on 09/28."
  🧠 Human/Cognitive Thought: Executed financial transaction: $290.87 (DEBIT)
  ⚙️ Parser Extracted: Amount=$290.87, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L078 (322632)
  SMS: "Bank of America: A withdrawal/debit of $138.95 (COMCAST CABLE) posted to account ending 9661 on 09/28. Available balance: $1,074.73."
  🧠 Human/Cognitive Thought: Executed financial transaction: $138.95 (DEBIT)
  ⚙️ Parser Extracted: Amount=$138.95, Acc=9661, Type=DEBIT, Bal=$1,074.73
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L079 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L080 (89887)
  SMS: "DOMINO'S: BOGO large pizzas today only! dominos.com. Txt STOP to end."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L081 (24273)
  SMS: "Chase: You made a $98.15 debit card purchase with card ending 882 at SHELL OIL 5744 on 09/29. Avail bal: $15,553.51."
  🧠 Human/Cognitive Thought: Executed financial transaction: $98.15 (DEBIT)
  ⚙️ Parser Extracted: Amount=$98.15, Acc=882, Type=DEBIT, Bal=$15,553.51
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L082 (24273)
  SMS: "Chase: $76.40 was debited from checking acct ending 9384 via debit card 882 at COSTCO WHSE #482 on 09/30. Avail bal: $15,477.11."
  🧠 Human/Cognitive Thought: Executed financial transaction: $76.40 (DEBIT)
  ⚙️ Parser Extracted: Amount=$76.40, Acc=9384, Type=DEBIT, Bal=$15,477.11
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L083 (24273)
  SMS: "Chase: You made a $11.40 debit card purchase with card ending 882 at DELTA AIR 0062341 on 09/30. Avail bal: $15,465.71."
  🧠 Human/Cognitive Thought: Executed financial transaction: $11.40 (DEBIT)
  ⚙️ Parser Extracted: Amount=$11.40, Acc=882, Type=DEBIT, Bal=$15,465.71
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L084 (24273)
  SMS: "Chase: Your one-time code is 123207. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L085 (24273)
  SMS: "Chase: You made a $27.42 debit card purchase with card ending 882 at SHELL OIL 5744 on 10/01. Avail bal: $15,438.29."
  🧠 Human/Cognitive Thought: Executed financial transaction: $27.42 (DEBIT)
  ⚙️ Parser Extracted: Amount=$27.42, Acc=882, Type=DEBIT, Bal=$15,438.29
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L086 (322632)
  SMS: "Bank of America: A purchase of $12.94 at APPLE.COM/BILL was charged to your credit card ending in 9111 on 10/02."
  🧠 Human/Cognitive Thought: Executed financial transaction: $12.94 (DEBIT)
  ⚙️ Parser Extracted: Amount=$12.94, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L087 (322632)
  SMS: "Bank of America: A purchase of $199.95 at 7-ELEVEN 33481 was charged to your credit card ending in 9111 on 10/03."
  🧠 Human/Cognitive Thought: Executed financial transaction: $199.95 (DEBIT)
  ⚙️ Parser Extracted: Amount=$199.95, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L088 (322632)
  SMS: "Bank of America: A withdrawal/debit of $26.76 (GEICO AUTO INS) posted to account ending 9661 on 10/03. Available balance: $1,047.97."
  🧠 Human/Cognitive Thought: Executed financial transaction: $26.76 (DEBIT)
  ⚙️ Parser Extracted: Amount=$26.76, Acc=9661, Type=DEBIT, Bal=$1,047.97
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L089 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $412.77 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$412.77, Acc=882, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='882'
     • Mismatch in 'amount': Expected='None', Parsed='412.77'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L090 (24273)
  SMS: "Chase: Thanks for confirming. The $412.77 charge at ONLINE MERCHANT on card 882 has been approved and posted. Avail bal: $15,025.52."
  🧠 Human/Cognitive Thought: Executed financial transaction: $412.77 (DEBIT)
  ⚙️ Parser Extracted: Amount=$412.77, Acc=None, Type=DEBIT, Bal=$15,025.52
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L091 (262966)
  SMS: "567532 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L092 (37777)
  SMS: "USPS: Your package 9435303465369919986 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L093 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,103.66. Min payment $35.00 due 10/24."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,103.66 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,103.66, Acc=9111, Type=DEBIT, Bal=$3,103.66
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L094 (24273)
  SMS: "Chase: You made a $171.59 debit card purchase with card ending 882 at STARBUCKS #2214 on 10/06. Avail bal: $14,853.93."
  🧠 Human/Cognitive Thought: Executed financial transaction: $171.59 (DEBIT)
  ⚙️ Parser Extracted: Amount=$171.59, Acc=882, Type=DEBIT, Bal=$14,853.93
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_123047_usa_batch2_L095 (+14155550132)
  SMS: "Running 10 min late"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L096 (22000)
  SMS: "G-208915 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L097 (86753)
  SMS: "Venmo: You paid John Miller $45.00. Funding source: Chase debit card ...882. Avail Venmo balance: $0.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $45.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$45.00, Acc=882, Type=DEBIT, Bal=$0.00
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L098 (24273)
  SMS: "Chase: You sent $46.05 to SARAH LOPEZ with Zelle(R) from acct ending 9384 on 10/08. Avail bal: $14,762.88."
  🧠 Human/Cognitive Thought: Executed financial transaction: $46.05 (DEBIT)
  ⚙️ Parser Extracted: Amount=$46.05, Acc=9384, Type=DEBIT, Bal=$14,762.88
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L099 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L100 (24273)
  SMS: "Chase: Your one-time code is 747015. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L101 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,103.66. Min payment $35.00 due 10/27."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,103.66 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,103.66, Acc=9111, Type=DEBIT, Bal=$3,103.66
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L102 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $123.55 at TARGET T-1043 on 10/09. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L103 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $73.71 with Zelle(R). Deposited to acct ...9384. Avail bal: $14,836.59."
  🧠 Human/Cognitive Thought: Executed financial transaction: $73.71 (CREDIT)
  ⚙️ Parser Extracted: Amount=$73.71, Acc=9384, Type=CREDIT, Bal=$14,836.59
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L104 (322632)
  SMS: "Bank of America: A purchase of $88.20 at TARGET T-1043 was charged to your credit card ending in 9111 on 10/10."
  🧠 Human/Cognitive Thought: Executed financial transaction: $88.20 (DEBIT)
  ⚙️ Parser Extracted: Amount=$88.20, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_123047_usa_batch2_L105 (322632)
  SMS: "Bank of America: A purchase of $88.20 at TARGET T-1043 was charged to your credit card ending in 9111 on 10/10."
  🧠 Human/Cognitive Thought: Executed financial transaction: $88.20 (DEBIT)
  ⚙️ Parser Extracted: Amount=$88.20, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------

FINAL RESULT: 92/105 Passed (87.6% Accuracy)
=================================================================


```

---

## 3. Archival Record
- Raw sample moved to `samples/processed/20260820_123047_usa_batch2.xml`.
