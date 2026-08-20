# Task Execution Report: `TASK_SAMPLE_20260820_124335_usa_batch3`

---

## 1. Executive Summary
- **Source File**: `samples/usa_batch3.xml`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Agent**: `opencode` (via Sample Scooper)
- **Processed Messages**: 102
- **Accuracy**: 81.4%
- **Status**: `COMPLETED`
- **Execution Timestamp**: 2026-08-20T12:43:35.618989

---

## 2. Test Execution Output
```text

=================================================================
       US SMS TEST SUITE — COGNITIVE VS PARSER EVALUATION        
=================================================================

Running 102 Test Cases from: temp_TASK_SAMPLE_20260820_124335_usa_batch3.json

[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L001 (322632)
  SMS: "Bank of America: A purchase of $38.14 at BEST BUY #442 was charged to your credit card ending in 9111 on 10/05."
  🧠 Human/Cognitive Thought: Executed financial transaction: $38.14 (DEBIT)
  ⚙️ Parser Extracted: Amount=$38.14, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L002 (24273)
  SMS: "Chase: You made a $53.99 debit card purchase with card ending 882 at SPOTIFY USA on 10/05. Avail bal: $14,782.60."
  🧠 Human/Cognitive Thought: Executed financial transaction: $53.99 (DEBIT)
  ⚙️ Parser Extracted: Amount=$53.99, Acc=882, Type=DEBIT, Bal=$14,782.60
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L003 (24273)
  SMS: "Chase: You made a $132.33 debit card purchase with card ending 882 at WALMART SUPERCENTER on 10/06. Avail bal: $14,650.27."
  🧠 Human/Cognitive Thought: Executed financial transaction: $132.33 (DEBIT)
  ⚙️ Parser Extracted: Amount=$132.33, Acc=882, Type=DEBIT, Bal=$14,650.27
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L004 (262966)
  SMS: "282295 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L005 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $164.69 at CHIPOTLE 1187 on 10/07. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L006 (+14155550132)
  SMS: "Hey are we still on for dinner tonight?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L007 (262966)
  SMS: "818341 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L008 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $613.12 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$613.12, Acc=882, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='882'
     • Mismatch in 'amount': Expected='None', Parsed='613.12'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L009 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 10/14. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L010 (24273)
  SMS: "Chase: You made a $67.33 debit card purchase with card ending 882 at CHIPOTLE 1187 on 10/09. Avail bal: $14,582.94."
  🧠 Human/Cognitive Thought: Executed financial transaction: $67.33 (DEBIT)
  ⚙️ Parser Extracted: Amount=$67.33, Acc=882, Type=DEBIT, Bal=$14,582.94
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L011 (24273)
  SMS: "Chase: RAJ PATEL sent you $81.74 with Zelle(R). Deposited to acct ...9384. Avail bal: $14,664.68."
  🧠 Human/Cognitive Thought: Executed financial transaction: $81.74 (CREDIT)
  ⚙️ Parser Extracted: Amount=$81.74, Acc=9384, Type=CREDIT, Bal=$14,664.68
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L012 (24273)
  SMS: "Chase: Direct deposit of $1,314.13 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 10/09. Avail bal: $15,978.81."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,314.13 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,314.13, Acc=9384, Type=CREDIT, Bal=$15,978.81
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L013 (24273)
  SMS: "Chase: Your online payment of $173.31 to BANK OF AMERICA CARD 9111 from acct ...9384 is complete on 10/10. Avail bal: $15,805.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $173.31 (CREDIT)
  ⚙️ Parser Extracted: Amount=$173.31, Acc=9384, Type=CREDIT, Bal=$15,805.50
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Chase'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L014 (322632)
  SMS: "BofA: Payment of $173.31 received on credit card ending 9111. Thank you. Avail credit: $1,143.31."
  🧠 Human/Cognitive Thought: Executed financial transaction: $173.31 (CREDIT)
  ⚙️ Parser Extracted: Amount=$173.31, Acc=9111, Type=CREDIT, Bal=$1,143.31
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L015 (22000)
  SMS: "G-547085 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L016 (24273)
  SMS: "Chase: Your one-time code is 224604. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L017 (24273)
  SMS: "Chase: You sent $75.86 to MIKE CHEN with Zelle(R) from acct ending 9384 on 10/11. Avail bal: $15,729.64."
  🧠 Human/Cognitive Thought: Executed financial transaction: $75.86 (DEBIT)
  ⚙️ Parser Extracted: Amount=$75.86, Acc=9384, Type=DEBIT, Bal=$15,729.64
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L018 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 10/18. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L019 (24273)
  SMS: "Chase: You made a $145.61 debit card purchase with card ending 882 at KROGER #771 on 10/12. Avail bal: $15,584.03."
  🧠 Human/Cognitive Thought: Executed financial transaction: $145.61 (DEBIT)
  ⚙️ Parser Extracted: Amount=$145.61, Acc=882, Type=DEBIT, Bal=$15,584.03
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L020 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,056.69. Min payment $35.00 due 10/30."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,056.69 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,056.69, Acc=9111, Type=DEBIT, Bal=$3,056.69
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L021 (322632)
  SMS: "Bank of America: A purchase of $77.30 at TARGET T-1043 was charged to your credit card ending in 9111 on 10/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $77.30 (DEBIT)
  ⚙️ Parser Extracted: Amount=$77.30, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L022 (322632)
  SMS: "Bank of America: A purchase of $77.30 at TARGET T-1043 was charged to your credit card ending in 9111 on 10/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $77.30 (DEBIT)
  ⚙️ Parser Extracted: Amount=$77.30, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L023 (+14155550132)
  SMS: "Movie at 8?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L024 (+14155550132)
  SMS: "Movie at 8?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L025 (322632)
  SMS: "Bank of America: A refund of $36.52 from COSTCO WHSE #482 was credited to your credit card ending in 9111 on 10/14. Avail credit: $1,102.53."
  🧠 Human/Cognitive Thought: Executed financial transaction: $36.52 (CREDIT)
  ⚙️ Parser Extracted: Amount=$36.52, Acc=9111, Type=CREDIT, Bal=$1,102.53
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L026 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $105.79 at KROGER #771 on 10/14. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L027 (+14155550132)
  SMS: "Running 10 min late"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L028 (+14155550132)
  SMS: "Movie at 8?"
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L029 (322632)
  SMS: "BofA: A pending authorization of $71.78 at TARGET T-1043 is on your credit card ending 9111. Final amount may vary."
  🧠 Human/Cognitive Thought: Executed financial transaction: $71.78 (DEBIT)
  ⚙️ Parser Extracted: Amount=$71.78, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L030 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L031 (24273)
  SMS: "Chase: You scheduled a payment of $174.86 to COMCAST CABLE for 10/24 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $174.86 (CREDIT)
  ⚙️ Parser Extracted: Amount=$174.86, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L032 (322632)
  SMS: "Bank of America: A purchase of $79.32 at WHOLE FOODS MKT was charged to your credit card ending in 9111 on 10/17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $79.32 (DEBIT)
  ⚙️ Parser Extracted: Amount=$79.32, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L033 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $485.08 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$485.08, Acc=882, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='882'
     • Mismatch in 'amount': Expected='None', Parsed='485.08'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L034 (24273)
  SMS: "Chase: You made a $148.65 debit card purchase with card ending 882 at AMAZON MKTPLACE on 10/17. Avail bal: $15,435.38."
  🧠 Human/Cognitive Thought: Executed financial transaction: $148.65 (DEBIT)
  ⚙️ Parser Extracted: Amount=$148.65, Acc=882, Type=DEBIT, Bal=$15,435.38
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L035 (322632)
  SMS: "Bank of America: A purchase of $175.27 at WALMART SUPERCENTER was charged to your credit card ending in 9111 on 10/17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $175.27 (DEBIT)
  ⚙️ Parser Extracted: Amount=$175.27, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L036 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $32.43 with Zelle(R). Deposited to acct ...9384. Avail bal: $15,467.81."
  🧠 Human/Cognitive Thought: Executed financial transaction: $32.43 (CREDIT)
  ⚙️ Parser Extracted: Amount=$32.43, Acc=9384, Type=CREDIT, Bal=$15,467.81
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L037 (24273)
  SMS: "Chase: You sent $120.81 to JOHN MILLER with Zelle(R) from acct ending 9384 on 10/18. Avail bal: $15,347.00."
  🧠 Human/Cognitive Thought: Executed financial transaction: $120.81 (DEBIT)
  ⚙️ Parser Extracted: Amount=$120.81, Acc=9384, Type=DEBIT, Bal=$15,347.00
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L038 (24273)
  SMS: "Chase: JOHN MILLER sent you $111.99 with Zelle(R). Deposited to acct ...9384. Avail bal: $15,458.99."
  🧠 Human/Cognitive Thought: Executed financial transaction: $111.99 (CREDIT)
  ⚙️ Parser Extracted: Amount=$111.99, Acc=9384, Type=CREDIT, Bal=$15,458.99
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L039 (262966)
  SMS: "877412 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L040 (322632)
  SMS: "Bank of America: A withdrawal/debit of $84.02 (ATM WITHDRAWAL #A5521) posted to account ending 9661 on 10/20. Available balance: $963.95."
  🧠 Human/Cognitive Thought: Executed financial transaction: $84.02 (DEBIT)
  ⚙️ Parser Extracted: Amount=$84.02, Acc=9661, Type=DEBIT, Bal=$963.95
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L041 (24273)
  SMS: "Chase: You made a $129.43 debit card purchase with card ending 882 at WHOLE FOODS MKT on 10/20. Avail bal: $15,329.56."
  🧠 Human/Cognitive Thought: Executed financial transaction: $129.43 (DEBIT)
  ⚙️ Parser Extracted: Amount=$129.43, Acc=882, Type=DEBIT, Bal=$15,329.56
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L042 (322632)
  SMS: "BofA: A pending authorization of $87.08 at APPLE.COM/BILL is on your credit card ending 9111. Final amount may vary."
  🧠 Human/Cognitive Thought: Executed financial transaction: $87.08 (DEBIT)
  ⚙️ Parser Extracted: Amount=$87.08, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L043 (24273)
  SMS: "Chase: Direct deposit of $860.83 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 10/21. Avail bal: $16,190.39."
  🧠 Human/Cognitive Thought: Executed financial transaction: $860.83 (CREDIT)
  ⚙️ Parser Extracted: Amount=$860.83, Acc=9384, Type=CREDIT, Bal=$16,190.39
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L044 (24273)
  SMS: "Chase: You made a $124.51 debit card purchase with card ending 882 at HOME DEPOT #6641 on 10/21. Avail bal: $16,065.88."
  🧠 Human/Cognitive Thought: Executed financial transaction: $124.51 (DEBIT)
  ⚙️ Parser Extracted: Amount=$124.51, Acc=882, Type=DEBIT, Bal=$16,065.88
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L045 (24273)
  SMS: "Chase: Direct deposit of $1,705.30 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 10/22. Avail bal: $17,771.18."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,705.30 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,705.30, Acc=9384, Type=CREDIT, Bal=$17,771.18
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L046 (24273)
  SMS: "Chase: Your online payment of $265.56 to BANK OF AMERICA CARD 9111 from acct ...9384 is complete on 10/23. Avail bal: $17,505.62."
  🧠 Human/Cognitive Thought: Executed financial transaction: $265.56 (CREDIT)
  ⚙️ Parser Extracted: Amount=$265.56, Acc=9384, Type=CREDIT, Bal=$17,505.62
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Chase'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L047 (322632)
  SMS: "BofA: Payment of $265.56 received on credit card ending 9111. Thank you. Avail credit: $1,113.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $265.56 (CREDIT)
  ⚙️ Parser Extracted: Amount=$265.56, Acc=9111, Type=CREDIT, Bal=$1,113.50
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L048 (24273)
  SMS: "Chase: You made a $76.96 debit card purchase with card ending 882 at COSTCO WHSE #482 on 10/23. Avail bal: $17,428.66."
  🧠 Human/Cognitive Thought: Executed financial transaction: $76.96 (DEBIT)
  ⚙️ Parser Extracted: Amount=$76.96, Acc=882, Type=DEBIT, Bal=$17,428.66
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L049 (322632)
  SMS: "Bank of America: A refund of $20.17 from 7-ELEVEN 33481 was credited to your credit card ending in 9111 on 10/23. Avail credit: $1,133.67."
  🧠 Human/Cognitive Thought: Executed financial transaction: $20.17 (CREDIT)
  ⚙️ Parser Extracted: Amount=$20.17, Acc=9111, Type=CREDIT, Bal=$1,133.67
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L050 (24273)
  SMS: "Chase: SARAH LOPEZ sent you $137.17 with Zelle(R). Deposited to acct ...9384. Avail bal: $17,565.83."
  🧠 Human/Cognitive Thought: Executed financial transaction: $137.17 (CREDIT)
  ⚙️ Parser Extracted: Amount=$137.17, Acc=9384, Type=CREDIT, Bal=$17,565.83
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L051 (24273)
  SMS: "Chase: You made a $23.50 debit card purchase with card ending 882 at AMAZON MKTPLACE on 10/25. Avail bal: $17,542.33."
  🧠 Human/Cognitive Thought: Executed financial transaction: $23.50 (DEBIT)
  ⚙️ Parser Extracted: Amount=$23.50, Acc=882, Type=DEBIT, Bal=$17,542.33
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L052 (322632)
  SMS: "Bank of America: A purchase of $104.39 at 7-ELEVEN 33481 was charged to your credit card ending in 9111 on 10/25."
  🧠 Human/Cognitive Thought: Executed financial transaction: $104.39 (DEBIT)
  ⚙️ Parser Extracted: Amount=$104.39, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L053 (22000)
  SMS: "G-363473 is your Google verification code."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L054 (322632)
  SMS: "Bank of America: A withdrawal/debit of $86.74 (GEICO AUTO INS) posted to account ending 9661 on 10/26. Available balance: $877.21."
  🧠 Human/Cognitive Thought: Executed financial transaction: $86.74 (DEBIT)
  ⚙️ Parser Extracted: Amount=$86.74, Acc=9661, Type=DEBIT, Bal=$877.21
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L055 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,170.72. Min payment $35.00 due 11/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,170.72 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,170.72, Acc=9111, Type=DEBIT, Bal=$3,170.72
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L056 (24273)
  SMS: "Chase: Direct deposit of $1,529.60 from ACME TECHNOLOGIES PAYROLL posted to acct ...9384 on 10/26. Avail bal: $19,071.93."
  🧠 Human/Cognitive Thought: Executed financial transaction: $1,529.60 (CREDIT)
  ⚙️ Parser Extracted: Amount=$1,529.60, Acc=9384, Type=CREDIT, Bal=$19,071.93
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L057 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $144.42 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$144.42, Acc=882, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='882'
     • Mismatch in 'amount': Expected='None', Parsed='144.42'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L058 (24273)
  SMS: "Chase: You made a $124.54 debit card purchase with card ending 882 at CHIPOTLE 1187 on 10/27. Avail bal: $18,947.39."
  🧠 Human/Cognitive Thought: Executed financial transaction: $124.54 (DEBIT)
  ⚙️ Parser Extracted: Amount=$124.54, Acc=882, Type=DEBIT, Bal=$18,947.39
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L059 (322632)
  SMS: "Bank of America: A purchase of $131.93 at KROGER #771 was charged to your credit card ending in 9111 on 10/27."
  🧠 Human/Cognitive Thought: Executed financial transaction: $131.93 (DEBIT)
  ⚙️ Parser Extracted: Amount=$131.93, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L060 (24273)
  SMS: "Chase: Your online payment of $391.52 to BANK OF AMERICA CARD 9111 from acct ...9384 is complete on 10/28. Avail bal: $18,555.87."
  🧠 Human/Cognitive Thought: Executed financial transaction: $391.52 (CREDIT)
  ⚙️ Parser Extracted: Amount=$391.52, Acc=9384, Type=CREDIT, Bal=$18,555.87
  ⚠️ DISCREPANCIES:
     • Mismatch in 'bank': Expected='Bank of America', Parsed='Chase'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L061 (322632)
  SMS: "BofA: Payment of $391.52 received on credit card ending 9111. Thank you. Avail credit: $1,288.87."
  🧠 Human/Cognitive Thought: Executed financial transaction: $391.52 (CREDIT)
  ⚙️ Parser Extracted: Amount=$391.52, Acc=9111, Type=CREDIT, Bal=$1,288.87
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L062 (24273)
  SMS: "Chase: Your one-time code is 803278. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L063 (24273)
  SMS: "Chase: You made a $68.70 debit card purchase with card ending 882 at UBER TRIP on 10/29. Avail bal: $18,487.17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $68.70 (DEBIT)
  ⚙️ Parser Extracted: Amount=$68.70, Acc=882, Type=DEBIT, Bal=$18,487.17
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L064 (24273)
  SMS: "Chase: You scheduled a payment of $249.36 to COMCAST CABLE for 11/07 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $249.36 (CREDIT)
  ⚙️ Parser Extracted: Amount=$249.36, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L065 (672566)
  SMS: "Netflix: Your payment method was updated successfully. If this wasn't you, visit netflix.com/account."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L066 (322632)
  SMS: "Bank of America: A withdrawal/debit of $17.49 (GEICO AUTO INS) posted to account ending 9661 on 10/30. Available balance: $859.72."
  🧠 Human/Cognitive Thought: Executed financial transaction: $17.49 (DEBIT)
  ⚙️ Parser Extracted: Amount=$17.49, Acc=9661, Type=DEBIT, Bal=$859.72
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L067 (24273)
  SMS: "Chase: You transferred $400.00 from acct ...9384 to external account ending 9661 on 10/31. Avail bal: $18,087.17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $400.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$400.00, Acc=9384, Type=DEBIT, Bal=$18,087.17
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L068 (322632)
  SMS: "Bank of America: A transfer of $400.00 was received into account ending 9661 on 10/31. Available balance: $1,259.72."
  🧠 Human/Cognitive Thought: Executed financial transaction: $400.00 (CREDIT)
  ⚙️ Parser Extracted: Amount=$400.00, Acc=9661, Type=CREDIT, Bal=$1,259.72
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L069 (24273)
  SMS: "Chase Fraud Alert: Did you attempt $292.82 at ONLINE MERCHANT with debit card ending 882? Reply YES or NO."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$292.82, Acc=882, Type=DEBIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='882'
     • Mismatch in 'amount': Expected='None', Parsed='292.82'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='DEBIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L070 (24273)
  SMS: "Chase: You scheduled a payment of $254.49 to COMCAST CABLE for 11/09 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $254.49 (CREDIT)
  ⚙️ Parser Extracted: Amount=$254.49, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L071 (24273)
  SMS: "Chase: MIKE CHEN requested $56.82 from you with Zelle(R). Review in the Chase Mobile app. No money has moved."
  🧠 Human/Cognitive Thought: Executed financial transaction: $56.82 (DEBIT)
  ⚙️ Parser Extracted: Amount=$56.82, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L072 (322632)
  SMS: "Bank of America: A purchase of $73.59 at STARBUCKS #2214 was charged to your credit card ending in 9111 on 11/02."
  🧠 Human/Cognitive Thought: Executed financial transaction: $73.59 (DEBIT)
  ⚙️ Parser Extracted: Amount=$73.59, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L073 (322632)
  SMS: "Bank of America: A withdrawal/debit of $85.05 (T-MOBILE) posted to account ending 9661 on 11/03. Available balance: $1,174.67."
  🧠 Human/Cognitive Thought: Executed financial transaction: $85.05 (DEBIT)
  ⚙️ Parser Extracted: Amount=$85.05, Acc=9661, Type=DEBIT, Bal=$1,174.67
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L074 (37777)
  SMS: "USPS: Your package 9414352513849945149 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L075 (322632)
  SMS: "Bank of America: A purchase of $67.25 at AMAZON MKTPLACE was charged to your credit card ending in 9111 on 11/03."
  🧠 Human/Cognitive Thought: Executed financial transaction: $67.25 (DEBIT)
  ⚙️ Parser Extracted: Amount=$67.25, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L076 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $280.36 at BEST BUY #442 on 11/04. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L077 (322632)
  SMS: "Bank of America: A purchase of $70.38 at CVS/PHARMACY #883 was charged to your credit card ending in 9111 on 11/04."
  🧠 Human/Cognitive Thought: Executed financial transaction: $70.38 (DEBIT)
  ⚙️ Parser Extracted: Amount=$70.38, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L078 (262966)
  SMS: "413586 is your Amazon OTP. Do not share it with anyone."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L079 (322632)
  SMS: "BofA: A pending authorization of $33.81 at BEST BUY #442 is on your credit card ending 9111. Final amount may vary."
  🧠 Human/Cognitive Thought: Executed financial transaction: $33.81 (DEBIT)
  ⚙️ Parser Extracted: Amount=$33.81, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L080 (24273)
  SMS: "Chase: You made a $120.00 debit card purchase with card ending 882 at KROGER #771 on 11/05. Avail bal: $17,967.17."
  🧠 Human/Cognitive Thought: Executed financial transaction: $120.00 (DEBIT)
  ⚙️ Parser Extracted: Amount=$120.00, Acc=882, Type=DEBIT, Bal=$17,967.17
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L081 (37777)
  SMS: "USPS: Your package 9485825906204525607 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L082 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $388.64 at SHELL OIL 5744 on 11/06. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L083 (322632)
  SMS: "Bank of America: Your credit card ending in 9111 statement is ready. Balance: $3,122.35. Min payment $35.00 due 11/25."
  🧠 Human/Cognitive Thought: Executed financial transaction: $3,122.35 (DEBIT)
  ⚙️ Parser Extracted: Amount=$3,122.35, Acc=9111, Type=DEBIT, Bal=$3,122.35
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L084 (24273)
  SMS: "Chase: RAJ PATEL sent you $67.52 with Zelle(R). Deposited to acct ...9384. Avail bal: $18,034.69."
  🧠 Human/Cognitive Thought: Executed financial transaction: $67.52 (CREDIT)
  ⚙️ Parser Extracted: Amount=$67.52, Acc=9384, Type=CREDIT, Bal=$18,034.69
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L085 (24273)
  SMS: "Chase: You made a $61.58 debit card purchase with card ending 882 at STARBUCKS #2214 on 11/08. Avail bal: $17,973.11."
  🧠 Human/Cognitive Thought: Executed financial transaction: $61.58 (DEBIT)
  ⚙️ Parser Extracted: Amount=$61.58, Acc=882, Type=DEBIT, Bal=$17,973.11
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L086 (24273)
  SMS: "Chase: You made a $127.26 debit card purchase with card ending 882 at BEST BUY #442 on 11/09. Avail bal: $17,845.85."
  🧠 Human/Cognitive Thought: Executed financial transaction: $127.26 (DEBIT)
  ⚙️ Parser Extracted: Amount=$127.26, Acc=882, Type=DEBIT, Bal=$17,845.85
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L087 (322632)
  SMS: "Bank of America: A purchase of $45.23 at APPLE.COM/BILL was charged to your credit card ending in 9111 on 11/09."
  🧠 Human/Cognitive Thought: Executed financial transaction: $45.23 (DEBIT)
  ⚙️ Parser Extracted: Amount=$45.23, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L088 (24273)
  SMS: "Chase: EMILY DAVIS requested $20.43 from you with Zelle(R). Review in the Chase Mobile app. No money has moved."
  🧠 Human/Cognitive Thought: Executed financial transaction: $20.43 (DEBIT)
  ⚙️ Parser Extracted: Amount=$20.43, Acc=None, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L089 (322632)
  SMS: "Bank of America: A withdrawal/debit of $71.36 (COMCAST CABLE) posted to account ending 9661 on 11/10. Available balance: $1,103.31."
  🧠 Human/Cognitive Thought: Executed financial transaction: $71.36 (DEBIT)
  ⚙️ Parser Extracted: Amount=$71.36, Acc=9661, Type=DEBIT, Bal=$1,103.31
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L090 (24273)
  SMS: "Chase: You scheduled a payment of $128.26 to COMCAST CABLE for 11/18 from acct ...9384. To cancel, visit chase.com/pay."
  🧠 Human/Cognitive Thought: Executed financial transaction: $128.26 (CREDIT)
  ⚙️ Parser Extracted: Amount=$128.26, Acc=9384, Type=CREDIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L091 (24273)
  SMS: "Chase: Your one-time code is 986798. Don't share it. We'll never call to ask for it."
  🧠 Human/Cognitive Thought: 2FA OTP / Verification Code. Zero financial movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L092 (322632)
  SMS: "BofA Reminder: Payment of $35.00 minimum is due on credit card ending 9111 by 11/17. Avoid late fees."
  🧠 Human/Cognitive Thought: Informational reminder/notice. No executed transaction.
  ⚙️ Parser Extracted: Amount=$35.00, Acc=9111, Type=CREDIT, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'is_transaction': Expected='False', Parsed='True'
     • Mismatch in 'account': Expected='None', Parsed='9111'
     • Mismatch in 'amount': Expected='None', Parsed='35.00'
     • Mismatch in 'txn_type': Expected='OTHER', Parsed='CREDIT'
     • Mismatch in 'source': Expected='NONE', Parsed='CARD'
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L093 (24273)
  SMS: "Chase: You made a $51.95 debit card purchase with card ending 882 at TARGET T-1043 on 11/11. Avail bal: $17,793.90."
  🧠 Human/Cognitive Thought: Executed financial transaction: $51.95 (DEBIT)
  ⚙️ Parser Extracted: Amount=$51.95, Acc=882, Type=DEBIT, Bal=$17,793.90
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L094 (322632)
  SMS: "Bank of America: A withdrawal/debit of $36.81 (T-MOBILE) posted to account ending 9661 on 11/12. Available balance: $1,066.50."
  🧠 Human/Cognitive Thought: Executed financial transaction: $36.81 (DEBIT)
  ⚙️ Parser Extracted: Amount=$36.81, Acc=9661, Type=DEBIT, Bal=$1,066.50
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L095 (322632)
  SMS: "Bank of America: A purchase of $84.01 at SPOTIFY USA was charged to your credit card ending in 9111 on 11/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $84.01 (DEBIT)
  ⚙️ Parser Extracted: Amount=$84.01, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L096 (322632)
  SMS: "Bank of America: A purchase of $54.44 at KROGER #771 was charged to your credit card ending in 9111 on 11/13."
  🧠 Human/Cognitive Thought: Executed financial transaction: $54.44 (DEBIT)
  ⚙️ Parser Extracted: Amount=$54.44, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L097 (24273)
  SMS: "Chase: You made a $149.64 debit card purchase with card ending 882 at PANERA BREAD #4402 on 11/13. Avail bal: $17,644.26."
  🧠 Human/Cognitive Thought: Executed financial transaction: $149.64 (DEBIT)
  ⚙️ Parser Extracted: Amount=$149.64, Acc=882, Type=DEBIT, Bal=$17,644.26
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L098 (322632)
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
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L099 (24273)
  SMS: "Chase: JOHN MILLER sent you $92.72 with Zelle(R). Deposited to acct ...9384. Avail bal: $17,736.98."
  🧠 Human/Cognitive Thought: Executed financial transaction: $92.72 (CREDIT)
  ⚙️ Parser Extracted: Amount=$92.72, Acc=9384, Type=CREDIT, Bal=$17,736.98
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L100 (322632)
  SMS: "Bank of America: A purchase of $46.93 at COSTCO WHSE #482 was charged to your credit card ending in 9111 on 11/15."
  🧠 Human/Cognitive Thought: Executed financial transaction: $46.93 (DEBIT)
  ⚙️ Parser Extracted: Amount=$46.93, Acc=9111, Type=DEBIT, Bal=$None
-----------------------------------------------------------------
[✅ PASS] TASK_SAMPLE_20260820_124335_usa_batch3_L101 (322632)
  SMS: "BofA: Your credit card ending 9111 was DECLINED for $389.01 at TRADER JOE'S #552 on 11/15. Call 800.732.9194 if you need help."
  🧠 Human/Cognitive Thought: Declined transaction alert. Blocked money movement.
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
-----------------------------------------------------------------
[❌ FAIL] TASK_SAMPLE_20260820_124335_usa_batch3_L102 (37777)
  SMS: "USPS: Your package 9480897275522687331 is out for delivery today."
  🧠 Human/Cognitive Thought: Executed financial transaction: $None (DEBIT)
  ⚙️ Parser Extracted: Amount=$None, Acc=None, Type=OTHER, Bal=$None
  ⚠️ DISCREPANCIES:
     • Mismatch in 'txn_type': Expected='DEBIT', Parsed='OTHER'
     • Mismatch in 'source': Expected='BANK', Parsed='NONE'
-----------------------------------------------------------------

FINAL RESULT: 83/102 Passed (81.4% Accuracy)
=================================================================


```

---

## 3. Archival Record
- Raw sample moved to `samples/processed/20260820_124335_usa_batch3.xml`.
