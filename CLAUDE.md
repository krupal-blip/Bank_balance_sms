# AGENT HANDOFF — Test Data Generator & QA Agent (Bank_balance_sms)

You are the Test Data Generator & QA Agent for this repo. This file gives any Claude agent
(Claude Code, Antigravity Claude model, claude.ai session) full context to continue the workflow.

## Mission
Generate realistic per-country SMS test batches to drive the SMS transaction parser to 100% accuracy.
REGION LOCK: currently **USA**. Do NOT move to another region until the user confirms 100% pass.

## Repo protocol
- Batches: `samples/usa/usa_batch{N}.xml` — next batch number: **11**
- Expected ledger: `samples/usa/usa_batch{N}_expected.json` (spec: `samples/GROUND_TRUTH_SPEC.md`)
  ALSO embed the same JSON in `<expected_summary>` inside the XML.
- After push to main, the Scooper daemon pulls, runs OpenCode harness, writes reports to `.ai/reports/`.
- Signal file (when present): `samples/NEXT_BATCH_REQUEST.md`.
- KNOWN PIPELINE BUGS to keep flagging: Scooper feeds `.md`/`*_expected.json` into the SMS harness
  (must route expected JSONs to the ledger auditor); cognitive labeler tags personal/promo texts as DEBIT/BANK.
- DAEMON GOTCHA: `automation/engine/autonomous_runner.py` runs live and **moves** anything dropped in
  `samples/usa/` into `automation/processed/<ts>_<name>` within seconds (that is why batch4/5 show as
  deleted from `samples/`). Generate to a temp dir, then `cp` + `git add` + `git commit` in ONE shell
  command so the blob is captured before the daemon relocates the file.

## XML format
```
<smses count="N" region="USA" batch="N">
  <sms><receivedTime>epoch_millis</receivedTime><address>sender</address><body>text</body></sms>
  ...
  <expected_summary>{json}</expected_summary>
</smses>
```

## Batch composition rules
- 100–120 SMS per batch; ~40% negative samples (OTPs, declines, fraud reply-YES/NO prompts,
  AutoPay SETUP notices, scheduled/future payments, statements, reminders, pending auths,
  Zelle requests, promos/delivery/personal noise, duplicates).
- Only EXECUTED money movements touch ledgers. Duplicates count once.
- Running balances must stay mathematically consistent within and ACROSS batches.
- Timestamps continue chronologically from the previous batch.

## User profile & ledger state (after batch 10 — opening balances for batch 11)
| Bank | Acct/Card | State | Status |
|---|---|---|---|
| Chase (sender 24273) | Acct 9384 | $35,478.94 | OPEN |
| Chase | Savings 5520 | $9,885.81 | OPEN |
| Chase | Debit 7761 (subset of 9384) | mirrors 9384 | OPEN |
| Chase | Debit 882 (subset of 9384) | — | CLOSED (fraud, batch 4) |
| Bank of America (322632) | Acct 9661 | $0.00 | CLOSED (batch 7, swept to Chase) |
| Bank of America | Credit card 9111, limit $6,000 | owed $83.79 / avail $5,916.21 | OPEN |
| Bank of America | AU card 3040 (was 2nd plastic on 9111) | — | CLOSED (removed batch 10) |
| Wells Fargo (93557) | Acct 4417 | $5,344.61 | OPEN |
| Wells Fargo | Debit 9111 (subset of 4417) — deliberate last-4 collision with BofA 9111 | mirrors 4417 | OPEN |
| **Citibank (692484)** | Acct 6208 (opened batch 8) | $1,785.50 | OPEN |
| Citibank | Debit 8890 (subset of 6208) | mirrors 6208 | OPEN |
| Citibank | Credit card 4310, limit $3,500 (opened batch 10) | owed $1,861.35 / avail $1,638.65 | OPEN |

FOUR institutions live now (Chase, BofA card-only, Wells Fargo, Citi). BofA has NO checking, so its
card 9111 is paid from Chase 9384. Batch 10 ends at epoch **1818983245000** (2027-08-23).
Batch 11 timestamps start after that.

Other senders: Amazon 262966, Google 22000, USPS 37777, Venmo 86753, Netflix 672566, promos 89887/55123,
CreditKarma 54321, personal +14155550132, **Citi 692484**, **IRS 77958**.

## History (accuracy per batch)
B1 76.5% → B2 87.6% → B3 81.4% (harder traps) → B4 **71.6%** (report
`.ai/reports/TASK_SAMPLE_20260820_134221_usa_batch4_report.md`; failures are almost all the labeler
bug tagging OTP/promo as DEBIT/BANK) → B5 report pending → B6 pushed: NEW Chase Savings 5520 funded by
internal transfer (2-leg), BofA 9661 OVERDRAFT to -$168.23 + $35 fee, BofA card cash advance $500 + $15
fee + $47.83 interest, GLOWMART double-charge + reversal (3 legs), exact-duplicate PETCO SMS, merchant
-number decoy `CVS/PHARMACY #4417`, provisional dispute credit, wire + wire fee, FX EUR, scheduled-vs-
executed pairs whose dates now match → B7 pushed (109 sms, 39.4% neg, all-new traps, no B1-B6 repeats):
BofA checking 9661 CLOSED with $1,144.84 sweep to Chase (2 legs, ends exactly $0.00); AU card 3040 added
as 2nd plastic on revolving 9111; deposit with delayed availability ($4,200, "$225 available now");
pre-auth $125 → settle $61.28; same txn reported twice in DIFFERENT wording (KROGER $58.20); My Plan
installment ($480 → only 1st $120 executes); micro-deposits $0.32/$0.47 + $0.79 reversal; Zelle sent
then RETURNED; $12 fee then courtesy reversal; foreign ATM CAD (3 legs: USD $147.62 + $5 intl + $4.43 FX);
payroll SPLIT across 2 banks; card annual fee $95; rewards-are-not-money decoy ("9,384 points" mimicking
acct 9384, "$25 cash rewards earned"); smishing SMS from shortcode 55123; paper check #1042 clearing.

## Answer style the user expects
No lectures. Compact: bank/card totals line, markdown table (bank | acct/card | avl balance | open/closed),
then the XML/push confirmation.

## Batches 8-10 (pushed together)
- **B8** (107 sms, 40.2% neg): **Citibank opens** — checking 6208 funded by $2,500 external transfer from
  Chase (2 legs), debit card 8890, $200 new-account bonus. **IRS amended refund $2,310** ACH credit, plus an
  IRS *smishing* decoy from shortcode 55123 and an informational IRS processing notice (neither is money).
  Incoming wire $8,500. HERTZ hold-then-**released** (zero legs). BofA AutoPay auto-cancelled because its
  funding account 9661 closed in batch 7. CreditKarma "$6,208 total balances" decoy vs Citi acct 6208.
- **B9** (105 sms, 41.9% neg): **three disputed chargebacks with different outcomes** —
  A WON (BofA 9111, $412.90 GADGETHUB: purchase + provisional credit = 2 legs, "now permanent" adds none),
  B LOST (Citi 6208 via card 8890, $189.55 TRAVELNOW: purchase + provisional credit + **reversal** = 3 legs),
  C left OPEN at batch end (WF 4417 via debit 9111, $76.40 STREAMLYFE, case WF-77410).
  Also ATM cash-deposit **verification adjustment** ($500 credited, $20 clawed back) and a prorated
  mid-cycle subscription refund ($8.33).
- **B10** (101 sms, 40.6% neg): **Citi credit card 4310** issued, limit $3,500. **Balance transfer** $1,200
  BofA 9111 -> Citi 4310 (credit one revolver, debit the other) + 3% $36.00 fee = 3 legs; household debt
  rises only by the fee. **Cross-batch dispute close**: WF-77410 settles $40 permanent / **$36.40 clawback**
  — only the clawback is a leg here. AU card 3040 removed (status CLOSED). Duplicate-authorization refund
  $71.55. Accrued-but-unposted Citi interest ($2.14) and earned-not-credited cash back ($18.42) are traps.
