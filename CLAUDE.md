# AGENT HANDOFF — Test Data Generator & QA Agent (Bank_balance_sms)

You are the Test Data Generator & QA Agent for this repo. This file gives any Claude agent
(Claude Code, Antigravity Claude model, claude.ai session) full context to continue the workflow.

## Mission
Generate realistic per-country SMS test batches to drive the SMS transaction parser to 100% accuracy.
REGION LOCK: currently **USA**. Do NOT move to another region until the user confirms 100% pass.

## Repo protocol
- Batches: `samples/usa/usa_batch{N}.xml` — next batch number: **7**
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

## User profile & ledger state (after batch 6 — opening balances for batch 7)
| Bank | Acct/Card | State | Status |
|---|---|---|---|
| Chase (sender 24273) | Acct 9384 | $20,925.04 | OPEN |
| Chase | Savings 5520 (opened batch 6) | $3,812.36 | OPEN |
| Chase | Debit 882 (subset of 9384) | — | CLOSED (fraud, batch 4) |
| Chase | Debit 7761 (subset of 9384, replacement) | mirrors 9384 | OPEN |
| Bank of America (322632) | Acct 9661 | $976.84 | OPEN |
| Bank of America | Credit card 9111, limit $6,000 | owed $2,728.62 / avail $3,271.38 | OPEN |
| Wells Fargo (93557) | Acct 4417 | $3,077.56 | OPEN |
| Wells Fargo | Debit 9111 (subset of 4417) — deliberate last-4 collision with BofA 9111 | mirrors 4417 | OPEN |

Batch 6 ends at epoch **1805850728000** (2027-03-24 01:12 UTC). Batch 7 timestamps start after that.

Other senders: Amazon 262966, Google 22000, USPS 37777, Venmo 86753, Netflix 672566, promos 89887/55123,
CreditKarma 54321, personal +14155550132.

## History (accuracy per batch)
B1 76.5% → B2 87.6% → B3 81.4% (harder traps) → B4 **71.6%** (report
`.ai/reports/TASK_SAMPLE_20260820_134221_usa_batch4_report.md`; failures are almost all the labeler
bug tagging OTP/promo as DEBIT/BANK) → B5 report pending → B6 pushed: NEW Chase Savings 5520 funded by
internal transfer (2-leg), BofA 9661 OVERDRAFT to -$168.23 + $35 fee, BofA card cash advance $500 + $15
fee + $47.83 interest, GLOWMART double-charge + reversal (3 legs), exact-duplicate PETCO SMS, merchant
-number decoy `CVS/PHARMACY #4417`, provisional dispute credit, wire + wire fee, FX EUR, scheduled-vs-
executed pairs whose dates now match.

## Answer style the user expects
No lectures. Compact: bank/card totals line, markdown table (bank | acct/card | avl balance | open/closed),
then the XML/push confirmation.
