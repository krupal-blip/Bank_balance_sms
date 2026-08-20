# REGION_CONFIG — every knob, India's value, your decision

The complete list of region-specific values in the feature. India's value is shown as a
**hint only** — do not copy it forward. Work top to bottom; the table order matches the
port order in `README.md`.

Legend: **MUST** = ship-blocking. **TUNE** = works with India's value but will be wrong for
your data. **KEEP** = region-neutral, do not touch.

---

## 1. Sender identity — MUST

`src/smsmodel/SenderResolver.kt.template`

| Knob | India (hint) | Your decision |
|---|---|---|
| Sender ID scheme | TRAI **DLT headers**: 2-char prefix + root ticker, e.g. `VM-HDFCBK`, `AD-HDFCCC` | What does a bank SMS sender look like in your market? |
| Prefix strip | `^[A-Z0-9]{2}-` | Only if your market has a fixed prefix. Most don't. |
| Root table size | 113 root tickers → canonical bank name | Your banks. One root per bank, not per product. |
| Match strategy | `startsWith(root)`, **longest root wins**, then `contains` fallback | **KEEP this strategy.** One bank registers many product IDs (`HDFCBK`/`HDFCCC`/`HDFCLN`…); a root match covers future ones for free. |
| Bare-number reject | `^\+?\d{6,}$` → return null | **Careful.** In US/CA banks send from numeric short codes. This rule would silently blind you. |
| Unresolvable senders | Co-op banks and rural banks with no consistent code → return `null`, fall back to model's BANK span | Every market has an unregulated tail. Plan the fallback, don't pretend it's empty. |

Sender scheme by market, to save you the research:

| Market | Shape | Implication |
|---|---|---|
| India | `XX-TICKER` DLT header | Prefix strip + root table |
| EU / UAE / SG / AU | Alphanumeric sender ID, no fixed prefix | Root table, no strip |
| US / CA | Numeric short codes (5–6 digits) | Short-code → bank map; **must not** hit the bare-number reject |
| BR / MX | Mixed short code + long number | Both paths needed |

---

## 2. Bank name table — MUST

`src/region/RegionProfile.kt.template` → `banks`

| Knob | India (hint) | Your decision |
|---|---|---|
| Canonical names | ~88 entries (`"HDFC Bank"`, `"State Bank of India"`, `"SBI"`, …) incl. abbreviations as separate entries | Your market's banks + the abbreviations their SMS actually use |
| Logo mapping | name → `R.drawable.bl_*` | Your assets |

**Consistency rule:** every canonical name the sender resolver can return must exist in
this table, or the two layers disagree about the same bank and the row gets a blank logo.
Assert it in a unit test.

---

## 3. Money format — MUST

`src/region/RegionProfile.kt.template` → currency block

| Knob | India (hint) | Your decision |
|---|---|---|
| Symbol | `₹` (`₹`) | Yours |
| Symbol position | **prefix**: `"₹ 1234.50"` | Prefix or suffix? `1.234,50 €` is suffix. |
| Decimal separator | `.` | `,` across most of the EU |
| Grouping separator | `,` **in lakh/crore groups** (`6,87,367.28` — not 3-digit groups) | 3-digit almost everywhere else; apostrophe in CH |
| Decimal places | 2 | 0 for JPY/KRW/VND; 3 for KWD/BHD/OMR/JOD |
| Max integer digits | `\d{1,10}` | India-specific reasoning: crore-scale balances are 8–9 digits. Re-derive for your currency's realistic maximum — don't copy 10. |
| Written currency words | `rs.` `inr.` `re.` `₹` `$` (all accepted) | Every spelling your banks use, including the informal one |

**The lakh-grouping trap, worth understanding before you set the regex.** India's
non-uniform grouping (`INR.6,87,367.28`) let the model emit *one comma group* as the whole
amount — `6` as amount, `87367` as balance. The fix is `MONEY_TOKEN_RE`: a model value is
only trusted if it appears in the body as a **complete** money token. That check is
region-neutral and worth keeping even if your grouping is uniform — it also catches
truncation.

Money is `BigDecimal` throughout. **Never `Double`.** This is not negotiable regardless of
region.

---

## 4. Account / card masking — MUST

`src/region/RegionProfile.kt.template` → account block

| Knob | India (hint) | Your decision |
|---|---|---|
| Account mask forms | `XX5665`, `X5665`, `*5665`, `...5665`, bare `5665` | Your banks' masking. Watch for `•` and `#`. |
| Account regex | `(x+\d{3,}\|\.{2,}\d{2,}\|\d{3,})` | Rewrite for your masks |
| Card-ending phrase | `card (ending with\|ending in\|x\|*) 1234` | Your wording, your language |
| Normalization | reduce to the **trailing digit run**, `""` if no run ≥3 digits | **KEEP.** This is what makes `XX5665` and `*5665` group as ONE account instead of several. |
| IBAN markets | n/a | If your market quotes IBAN tails, the digit-run rule still works but the min length changes |

---

## 5. Language / keyword layer — MUST

`src/pipeline/TransactionFilter.kt.template`

All of these are English word lists in India. Every one needs your language(s). Multilingual
markets need **all** languages a bank might send in.

| Knob | India (hint) | Note |
|---|---|---|
| Credit/debit keywords | `debited` `credited` `debit` `credit` | Reconciles against the model's type head; keyword wins on disagreement |
| Balance phrases | `avl` `avlbl` `available` `total` `new` `net` `closing` `clear` + `bal`/`balance` | Two tiers: qualified then bare |
| Mandate / recurring | `mandate` `autopay` `e-mandate` `standing instruction` | India's NACH vocabulary. EU = SEPA Direct Debit, US = ACH. |
| Creation verbs | `created` `raised` `registered` `approved` `set up` `will be debited` | Only mandate **creation** is a non-transaction — an *executed* autopay debit is real money. Getting this backwards inflated computed balances. |
| Cancel verbs | `cancelled` `revoked` `stopped` `deleted` `de-registered` | Cancel notices quote the mandate amount, which was landing in the balance field |
| OTP | `otp` `one time password` | |
| Declined | `declined` `payment failed` `low funds alert` | |
| Reminder | `maintain balance` `auto debit due/on` | |
| Card-type phrases | `credit card` / `card member` vs `debit card` | Decides whether a body owns its own account |
| Future tense | `will be` `shall be` `is scheduled to` `is due to` `is going to` `is expected to` `will get` `shall get` `would be` | Fed to the **model** as a synthetic token — see `MODEL_TRAINING.md` |

---

## 6. Batch engine vocabulary — MUST

`src/kpdo/BatchEngine.kt.template`

| Knob | India (hint) | Your decision |
|---|---|---|
| Notice markers | `umrn` `umn` `mandate` `payment alert`, card-bill acks (`payment of … received towards`) | **`UMRN` is an India-only NACH mandate id.** Find your clearing system's equivalent. |
| Reference extraction | `refno` `reference no` **`utr`** `txn id` → `[A-Za-z0-9]{6,}` | **`UTR` is India-only.** EU: `E2E ref`. US: `trace number`. |
| Limit-not-balance | `available limit` / `avl limit` | A card ack reports available *limit*, not an account balance. Every market with credit cards needs this distinction. |

---

## 7. Thresholds — TUNE

Re-derive each against **your** corpus. Copying India's numbers is the single most likely
source of a quietly-wrong port.

| Knob | India | How to set yours |
|---|---|---|
| `MIN_CONFIDENCE` | `0.80f` on the is-transaction head only | Sweep 0.5→0.95 on your corpus, pick the knee. A weaker model needs a **lower** gate + **stronger** guardrails — measure that trade, don't assume it. |
| Confidence source | is-transaction head **only**, deliberately not type/source | **KEEP.** Type/source uncertainty must not kill a row whose amount and account are clean and separately validated. |
| `PAIR_WINDOW_MS` | `3 days` | How long does a notice lead its ledger entry in **your** clearing system? |
| Pair amount tolerance | `0.01` | Currency decimals |
| Balance-chain tolerance | `1.0` (one rupee) | **Wrong for JPY (0 decimals) and KWD (3).** One minor unit. |
| Model vocab size | `4096` buckets | Larger script/vocabulary → raise it. Costs `.bin` size. |
| `MAX_TRI` | `8` trigrams averaged per token | Longer average word length → raise |
| Inbox paging | yield every `50`, clear caches after `100` | Tune to device class, not region |

---

## 8. Stable internal keys — TUNE CAREFULLY

`src/region/RegionProfile.kt.template` is **not** where these belong.

| Constant | India value | Rule |
|---|---|---|
| `CREDIT` | `"Credited"` | These exact strings are **written into the database** and compared by every downstream query, filter and UI branch. |
| `DEBIT` | `"Debited"` | Keep them as stable **internal keys**, localize only at display time. |
| `OTHER` | `"Other"` | Changing a value is a **data migration**, not a rename. |
| `VIA_BANK` | `"BANK"` | Same. |
| `VIA_CARD` | `"CARD"` | Same. |

Recommendation for a fresh build: make them an `enum` with an explicit stable `dbValue`,
and never let a display string touch the column. India used raw strings; you don't have to
inherit that.

---

## 9. Schema — decide once

`src/model/TransactionRow.kt.template`

| Field | India | Recommendation for a new build |
|---|---|---|
| `date` | epoch millis **as `String`** | Use `Long`. India's choice works but forces `toLongOrNull()` at every comparison and sort. Changing it later touches the entity, the DAO, the batch engine and the reparse. |
| `amount`, `avlBal` | `String`, pre-formatted with `₹` | Store a **`Long` minor-unit** amount plus a separate currency code; format at display. India's pre-formatted strings mean every consumer re-parses, and the currency symbol is baked into the data. |
| Unique index | `(body, date)` | **KEEP.** This is the last line of defence against re-inserting the same message. |
| Insert conflict | `OnConflictStrategy.IGNORE` | **KEEP**, pairs with the index above. |
| `massageId`, `typeID`, `thread` | carried from the SMS provider | KEEP — needed to correlate back to the inbox |

Deviating from India here is **encouraged** — those two choices are the pipeline's main
legacy debt. Just decide before you write the first migration, not after.

---

## 10. Platform / policy — MUST

`manifest/AndroidManifest.snippet.xml.template`

| Knob | Note |
|---|---|
| `RECEIVE_SMS`, `READ_SMS` | Play-restricted. Needs a Permissions Declaration and a policy review. |
| Market policy | SMS-permission policy **differs by market**. Confirm the feature is permitted in your target market **before** building it. This has killed ports. |
| Receiver priority | India uses `999`. Cosmetic — ordering only. |
| Boot receiver | `RECEIVE_BOOT_COMPLETED` only if you re-trigger backfill after reboot |

---

## 11. KEEP — do not touch

Region-neutral. Copy verbatim from `../src/`. Each one is here because of a real production
bug; see `../PIPELINE.md` stage 3.

- `GruCell.kt`, `CrfDecoder.kt`, `ModelWeights.kt` — pure math and binary IO
- `GruCell` created **per `parse()` call**, never a field — a shared instance corrupted
  ~0.5% of concurrent parses
- `MessageListener`'s `goAsync()` → IO → timeout → `finally { finish() }` shape
- First `Database.getInstance()` on IO — it opens the DB and runs migrations eagerly
- Batch parse on `Dispatchers.Default`, ingestion on `Dispatchers.IO`
- Collect-then-batch-parse in the ingestor (the batch engine needs all messages at once)
- Single-flight mutex on inbox import
- `"<sender> | <body>"` model input convention
- Span-merge rules and trailing-digit account normalization
- Reparse-in-place on `PARSER_VERSION` bump — ids preserved, table never nuked
