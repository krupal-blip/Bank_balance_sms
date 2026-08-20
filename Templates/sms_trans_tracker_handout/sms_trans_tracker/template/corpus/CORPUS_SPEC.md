# CORPUS_SPEC — do this before you write any code

The corpus is the port. Everything else is downstream of it: you cannot tune a
threshold, evaluate a regex, or train a model without one, and you cannot tell
whether your port works without a held-out split.

India's was **371 real messages**. It is **removed from this hand-out** (real people's
financial data — see `../../HANDOUT_NOTES.md`). Its content would be useless to you
anyway; for the file shape use `corpus_template.csv` here, or
`../../corpus/corpus_format_example.csv`.

---

## Minimum bar

| Requirement | Minimum | Why |
|---|---|---|
| Total messages | **300**, 500+ preferred | Below ~300 a held-out split is too small to measure a false-positive rate |
| Banks covered | **every bank you claim to support** | A bank absent from the corpus is a bank you are guessing about |
| Real, not synthetic | **100% real** | Synthetic-only training scores well against your own generator and fails on the first bank that words things differently |
| Held-out split | **20%, reserved before you generate anything** | Otherwise every number you report is meaningless |
| Negative examples | **~40% of the corpus** | See below — this is the part people skip |

## Message types to collect

Both classes matter. A corpus of only transactions teaches the model to say yes to
everything.

**Positives — real money movement:**

- debit from account
- credit to account
- card transaction (credit card and debit card separately)
- ATM / cash withdrawal
- transfer out and transfer in
- recurring payment **executed** (this is a real transaction — see negatives)
- refund / reversal
- a balance-carrying message and a balance-less one for the same account

**Negatives — no money moved (target ~40%):**

- OTP / verification code
- recurring payment **mandate created** (not a transaction)
- recurring payment **mandate cancelled** (not a transaction, and it *quotes an amount*, which is exactly why it's dangerous)
- declined / failed payment
- low-balance and maintain-balance reminders
- statement ready / minimum due
- future-tense notices ("will be debited tomorrow")
- promotional and loan-offer messages from the same bank senders
- card bill-payment acknowledgement (a restatement of a payment recorded elsewhere)
- non-bank messages from senders that *look* bank-like

**Pairs — collect deliberately:**

Find at least 10 cases where **one payment produced two messages** (notice + ledger
entry). These are what the batch engine's pairing pass exists for, and you cannot verify
that pass without them. India's canonical case is a NACH recurring debit that sends a
mandate notice and a separate ledger entry with different bodies and different references.

---

## File format

See `corpus_template.csv`. One row per message.

| Column | Meaning |
|---|---|
| `sender` | Raw sender address exactly as received — with prefix, casing, everything |
| `body` | Raw message text, verbatim. Do not clean, trim, or fix typos. |
| `is_transaction` | `1` / `0` — the is-transaction head's label |
| `bank` | Expected canonical bank name, blank if unresolvable |
| `account` | Expected account, **already normalized** to trailing digits |
| `amount` | Expected amount, plain number, no symbol, `.` decimal |
| `balance` | Expected balance, same format, blank if none |
| `txn_type` | `CREDIT` / `DEBIT` / `OTHER` |
| `source` | `BANK` / `CARD` / `NONE` |
| `merchant` | Expected merchant, blank if none |
| `notes` | Free text — why this row is interesting, especially for negatives |

Rules:

- **Verbatim bodies.** Preserve the odd spacing, the `INR.` with a period, the missing
  space after the currency symbol. Those quirks are the thing you are training against.
- **Normalized expectations.** `account` should be `5665`, not `XX5665` — that is what the
  parser is expected to output.
- **Plain numbers for money.** No symbol, no grouping separators, `.` as decimal, whatever
  your currency's decimal count is. Formatting is a display concern.
- **Fill `notes` for every negative.** Six months later nobody remembers why a row was
  labelled `0`.

---

## Privacy — read before you collect

Real bank SMS are financial records about identifiable people.

- **Redact account numbers beyond the masked tail.** If a message leaks a full account
  number, replace the extra digits — the parser only ever uses the trailing run.
- **Remove names, phone numbers, addresses, VPAs/handles** unless a handle is the merchant
  value you are testing, in which case replace it with a fake one of the same shape.
- **Get consent** for anything collected from a real user's device, and keep a record of
  it.
- **Do not commit an unredacted corpus** to a shared repo, and check whether it may leave
  its country of origin before you upload it anywhere.
- Redaction must **preserve shape**: same length, same masking style, same separators.
  Replacing `XX5665` with `ACCOUNT` destroys the row.

---

## Sourcing, in order of quality

1. **Your own team's devices.** Fast, consented, but biased toward a few banks and toward
   staff-typical transaction patterns.
2. **A recruited beta panel.** Best coverage-to-effort ratio. Pay them, get written
   consent, redact on intake.
3. **Bank documentation and support pages.** Many banks publish their alert templates.
   Excellent for the template document, but these are *idealized* — real messages drift.
4. **Existing users, opt-in.** Highest volume, heaviest consent and compliance work.

Do not scrape SMS from anywhere. Do not buy a dataset of real bank messages.

---

## Coverage check before you move on

```
banks claimed supported          ____
banks present in corpus          ____   ← must be equal
messages per bank (minimum)      ____   ← aim for 5+
positives / negatives            ____ / ____   ← negatives ~40%
notice+ledger pairs              ____   ← 10+
held-out split reserved          ____%  ← 20%
languages present                ____   ← every language a bank sends in
redaction pass done              [ ]
consent recorded                 [ ]
```

Once this table is filled, go to `../MODEL_TRAINING.md`. Until it is, there is nothing to
train and nothing to measure.
