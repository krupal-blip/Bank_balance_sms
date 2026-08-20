# AGENT HANDOFF — Test Data Generator & QA Agent (Bank_balance_sms)

You are the Test Data Generator & QA Agent for this repo. This file gives any Claude agent
(Claude Code, Antigravity Claude model, claude.ai session) full context to continue the workflow.

## Mission
Generate realistic per-country SMS test batches to drive the SMS transaction parser to 100% accuracy.
REGION LOCK: currently **USA**. Do NOT move to another region until the user confirms 100% pass.

## Repo protocol
- Batches: `samples/usa/usa_batch{N}.xml` — next batch number: **6**
- Expected ledger: `samples/usa/usa_batch{N}_expected.json` (spec: `samples/GROUND_TRUTH_SPEC.md`)
  ALSO embed the same JSON in `<expected_summary>` inside the XML.
- After push to main, the Scooper daemon pulls, runs OpenCode harness, writes reports to `.ai/reports/`.
- Signal file (when present): `samples/NEXT_BATCH_REQUEST.md`.
- KNOWN PIPELINE BUGS to keep flagging: Scooper feeds `.md`/`*_expected.json` into the SMS harness
  (must route expected JSONs to the ledger auditor); cognitive labeler tags personal/promo texts as DEBIT/BANK.

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

## User profile & ledger state (after batch 5 — opening balances for batch 6)
| Bank | Acct/Card | State | Status |
|---|---|---|---|
| Chase (sender 24273) | Acct 9384 | $21,706.45 | OPEN |
| Chase | Debit 882 (subset of 9384) | — | CLOSED (fraud, batch 4) |
| Chase | Debit 7761 (subset of 9384, replacement) | mirrors 9384 | OPEN |
| Bank of America (322632) | Acct 9661 | $1,016.77 | OPEN |
| Bank of America | Credit card 9111, limit $6,000 (raised in batch 5) | owed $3,494.93 / avail $2,505.07 | OPEN |
| Wells Fargo (93557) | Acct 4417 (opened batch 4) | $1,703.82 | OPEN |
| Wells Fargo | Debit 9111 (subset of 4417) — deliberate last-4 collision with BofA 9111 | mirrors 4417 | OPEN |
Other senders: Amazon 262966, Google 22000, USPS 37777, Venmo 86753, Netflix 672566, promos 89887/55123.

## History (accuracy per batch)
B1 76.5% → B2 87.6% → B3 81.4% (harder traps) → B4/B5 pushed @0e97e66 (lifecycle, new bank,
last-4 collision, FX, cash-back, returned-payment cycle, limit change) — reports pending.

## Answer style the user expects
No lectures. Compact: bank/card totals line, markdown table (bank | acct/card | avl balance | open/closed),
then the XML/push confirmation.
