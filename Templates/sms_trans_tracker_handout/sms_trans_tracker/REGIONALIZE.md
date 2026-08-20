# REGIONALIZE — porting this feature to another region

The architecture is region-neutral. What is India-specific is **data, regexes, and the
trained model** — not the pipeline shape. Work the tiers in order; tier 1 alone gets a
working extractor on a new region's SMS.

---

## Tier 1 — must change

### 1. Sender identity: `src/smsmodel/BankSenderResolver.kt`

**India-specific by construction.** 113 root tickers + the TRAI DLT header format
(`^[A-Z0-9]{2}-`, e.g. `VM-HDFCBK`). No other country uses this scheme.

Replace with whatever the target region's sender identity actually is:

| Region shape | What to build instead |
|---|---|
| Alphanumeric sender IDs (EU, UAE, SG) | Same root-prefix table, drop the 2-char-prefix strip |
| Short codes (US, CA) | Numeric short-code → bank map; `BARE_PHONE_RE` must NOT reject them |
| Plain long numbers only | Return `null` always; rely entirely on the model's BANK span |

Keep the design: **root-prefix matching, longest match wins**. One bank registers many
product IDs; enumerating every suffix is unmaintainable. Also keep the risk note — this
resolver only sets the display name/logo, never amount or account, so a collision costs a
wrong label, not wrong money. Tune it freely.

### 2. Bank name table: `src/shared/BankList.kt.excerpt`

`Bank_List` (≈88 Indian bank display names) is what the model's raw BANK span is matched
against, and `getBankList()` maps each to a logo drawable. Both are pure region data.
**First file to replace.** Every name here must also exist as a canonical value in the
sender resolver, or the two layers will disagree about the same bank.

### 3. Currency and money regexes: `src/pipeline/BankTransactionFilterMl.kt`

| Constant | India assumption | Change to |
|---|---|---|
| `"₹ $amountNum"` (₹ prefix, 4 sites) | Rupee symbol, prefix position | Target symbol/code, and prefix-vs-suffix (`1.234,50 €` is suffix) |
| `AMOUNT_RE = \d{1,10}(\.\d{1,2})?` | `.` decimal, `,` grouping, ≤10 digits | `,` decimal + `.` grouping in most of the EU; 3 decimals for KWD/BHD/OMR; 0 for JPY/KRW |
| `QUALIFIED_BAL_RE`, `BARE_BAL_RE`, `BODY_AMT_RE` | English "Avl Bal", "Rs.", "INR" | Target-language balance wording |
| `MONEY_TOKEN_RE` | `,`-grouped digits | Locale grouping separator |
| `CARD_ENDING_RE` | "ending with 1234", `XX1234` | Target card-masking convention |
| `ACCOUNT_RE = (x+\d{3,}\|\.{2,}\d{2,}\|\d{3,})` | `X`-masked accounts | Target masking (`*`, `•`, IBAN tails) |

The **10-digit** amount width is not arbitrary — crore-scale balances are 8–9 digits.
Re-derive the ceiling for the target currency rather than copying it.

`BigDecimal` everywhere for money. Do not "simplify" to `Double`.

### 4. The trained model: `assets/sms_model_v7.bin`

**Cannot be reused across regions.** It was trained on Indian bank SMS templates and
English/Hinglish banking vocabulary. On a new region it will return low `isBank`
confidence and get gated out by `MIN_CONFIDENCE`.

To retrain you need, in order:

1. **A real corpus.** India's was 371 real messages (removed from this hand-out — see
   `HANDOUT_NOTES.md`; `corpus/corpus_format_example.csv` shows the format),
   and the A/B set for every model change. Build the equivalent for the target region:
   real messages, not synthetic, spanning every bank you claim to support.
2. **Template documentation.** `corpus/DLT_SMS_TEMPLATES.md` documents Indian formats per
   bank; the trainer's synthetic generator is built from these. Do the same for the
   target region.
3. **The trainer.** Not in this repo. Read `corpus/MIGRATION_v1_to_v2.md` for the
   architecture the `.bin` format expects.
4. **Byte-compatibility.** `Tokenizer.trigramIds` must match the trainer's `trigram_ids`
   exactly, and `ModelWeights.load()` must match the writer's tensor order. Verify with a
   Python→Kotlin parity fixture before shipping.

**Non-Latin scripts need work beyond retraining.** `Tokenizer.TOKEN_RE` is
`[A-Za-z0-9]+|[^\sA-Za-z0-9]`, so Arabic, Thai, Devanagari or CJK text collapses into
single punctuation-class tokens. Character trigrams over UTF-8 still work in principle —
FNV-1a already hashes bytes — but `TOKEN_RE` and `MAX_TRI` must be re-derived, and CJK
needs a different segmentation strategy entirely.

### 5. Keyword layers: `src/shared/PipelineExtensions.kt.excerpt`

`String.transactionType()`, `getAccountNumber()`, `getBalance()`, `getMerchantName()` are
English keyword and regex scanners ("debited", "credited", "a/c", "avl bal"). They are the
**fallback and the veto** for the model, so they are not optional — the model alone is not
trusted. Rewrite per target language.

### 6. Notice / veto vocabulary

- `TransactionRowValidator` (in `BankTransactionFilterMl.kt`): mandate, OTP, declined,
  reminder, cancel word lists — all English.
- `KpdoEngine.NOTICE_MARK`: `UMRN`, `UMN`, `mandate`, `payment alert` — **UMRN is an
  India-only NACH identifier.** Replace with the target region's recurring-payment scheme
  (SEPA Direct Debit mandate refs in the EU, ACH in the US).
- `KpdoEngine.REF_RE`: `refno`, `UTR`, `txn id` — **UTR is India-only.** Use the target's
  reference vocabulary.

### 7. Permissions and receiver: `manifest/AndroidManifest.snippet.xml`

`RECEIVE_SMS` + `READ_SMS` are Play-restricted permissions. A new region means a new Play
Console declaration and review, and the SMS-permission policy differs by market. Confirm
the feature is even permitted in the target market before building it.

---

## Tier 2 — probably change

- **`Constant.CREDIT = "Credited"` / `DEBIT = "Debited"` / `OTHER = "Other"`**
  (`src/shared/PipelineConstants.kt.excerpt`) — these exact strings are written to the DB
  and compared by every downstream query and UI filter. Keep them as **stable internal
  keys** and localize at display time. Changing a value is a data migration, not a rename.
  Same for `VIA_BANK = "BANK"` / `VIA_CARD = "CARD"`.
- **`MIN_CONFIDENCE = 0.80f`** — re-tune against the new corpus. A weaker model needs a
  lower gate plus stronger guardrails, and that trade-off has to be measured, not guessed.
- **`PAIR_WINDOW_MS = 3 days`** — derived from how long an Indian NACH notice leads its
  ledger entry. Re-derive for the target clearing system.
- **Balance-chain tolerance `1.0`** — one currency unit. Wrong for zero-decimal currencies
  (JPY) and for 3-decimal ones (KWD).
- **`date` as an epoch-millis String** — works, but if you are rebuilding the schema
  anyway, a real `Long` column is the better choice. It affects `BankSMSModell`,
  `SmsInput`, every DAO query, and `dayOf()`.

---

## Tier 3 — reuse as-is

These carry no region assumptions. Copy them unchanged.

- `src/smsmodel/GruCell.kt`, `CrfDecoder.kt`, `ModelWeights.kt` — pure math and IO.
- `src/smsmodel/SmsParser.kt` — the `"<address> | <body>"` convention, span-merge rules,
  and account normalization are all format-agnostic.
- `src/kpdo/KpdoEngine.kt` — the two-pass structure. Only its regexes are regional.
- `src/pipeline/SmsTransactionIngestor.kt` — cursor paging, single-flight mutex,
  `yield()`/`delay()` batching, collect-then-batch-parse.
- `src/pipeline/MessageListener.kt` — the `goAsync()` + IO + timeout + `finally finish()`
  shape. **Copy this exactly.** Getting it wrong is a guaranteed ANR.
- `src/pipeline/ReparseMigration.kt`, `PhantomRowCleanup.kt` — the
  reparse-in-place-on-version-bump pattern.
- `AccountMemory`, `MlParserHolder`, `SmsFilterContext` — the last-account-per-bank
  inheritance and the Android-free seam for testing.

---

## Port order that works

1. Get a real corpus for the region. Nothing downstream can be evaluated without it.
2. Stand up `BankSenderResolver` + `Bank_List` for the region's banks.
3. Port the currency/account regexes and the keyword layer; measure how far
   **regex-only** gets you against the corpus. This is the floor and the fallback.
4. Retrain the model; A/B against the same corpus. Ship only if it beats the floor.
5. Port KPDO's regional vocabulary (notice markers, reference formats).
6. Re-tune `MIN_CONFIDENCE`, `PAIR_WINDOW_MS`, balance tolerance against the corpus.
7. Set `PARSER_VERSION` to a new region-tagged value so `ReparseMigration` re-polishes
   any pre-existing rows.

## Two things not to redesign away

- **The model never decides alone.** Every field it emits is validated, repaired, or
  vetoed by plain Kotlin against the raw body. That layering is why a wrong span becomes a
  dropped row instead of a wrong balance shown to a user. Do not ship the model output raw
  on the grounds that the new model scores well.
- **Threading.** `MessageListener` off main, first `BankDataBase.getInstance()` on IO,
  `GruCell` per-call, batch parse on `Dispatchers.Default`. Each of these was a real
  production ANR or a real data-corruption bug. See `PIPELINE.md` stage 3.
