# Migration Guide — SMS Parser v1 → v2 (pretrained model)

Audience: code agent working in the Bank Balance Checker Android project.

---

## STATUS 2026-08-05 — APPLIED (asset `sms_model_v7.bin`)

This guide is now **historical**. State when picked up: steps 2/3/4 (the three
`.kt` files) were **already applied** in an earlier session — all three were
byte-identical to `Tokenizer_v2.kt` / `ModelWeights_v2.kt` / `SmsParser_v2.kt`.
The app was already running v2 *code* against a v2 *model* (`sms_model_v5.bin`).

So the remaining work was not "v1 → v2" — that had happened. It was a
**model-weights refresh inside v2**: `sms_model_v5.bin` → `sms_model_v7_final.bin`
(shipped as `assets/sms_model_v7.bin`), plus the step-5 `PARSER_VERSION` bump.

The "keep v1 in production" recommendation at the top is therefore **stale** —
it describes the v1→v2 architecture switch, which is long since shipped. Its
migration gate ("migrate only if a reconciliation round has fixed the non-txn
regression") is what v7 was measured against, and v7 passes it. Details below.

### Measured v5 vs v7 (not assumed)

Both bins run through the real production classes (`Tokenizer`/`ModelWeights`/
`GruCell`/`CrfDecoder`/`SmsParser` compiled as-is — they are pure JVM, no Android
deps) over **371 real messages** reconstructed from `docs/BankName-BankSMSModell.csv`.
Header check (doc step 1) passes on v7: `4096 / 64 / 64 / 11 / 8`.

| metric | v5 (was live) | v7 (now live) |
|---|---|---|
| doc's 7 known-good strings (step 2) | 6/6 | 6/6 |
| amount extracted (guardrail-valid) | 332 | **348** |
| amount matches stored value | 338 | **347** |
| missed amount on a row that passes the gate | 22 | **12** |
| account matches stored | 325 | 325 |
| synthetic non-txn dropped (step 3) | 1/4 | **3/4** |
| credit↔debit inversions vs stored | **2** | 4 |
| malformed amount string | **4** | 5 |

v7 wins clearly on recall (16 more amounts extracted, 10 fewer missed on kept
rows) and on the doc's own blocking condition, the non-txn regression (1/4 → 3/4
dropped). Notably it fixes the ATM class — `Withdrawn Rs.10000 From HDFC Bank
Card x9082 At YOGI CHOWK` extracts no amount under v5, correct `10000` under v7.

### Known residual regressions in v7 — accepted, not unknown

1. **`.00` amount on `Sent Rs.<amt> From HDFC Bank A/C` (4/371, ~1%).** v7 emits
   the amount span as `.00` where v5 gave `200.00`. `digitsOfAmount()` yields
   `""`, so the guardrail rejects the value — the row loses its amount rather
   than storing a wrong one. Balance math is not corrupted, but a real
   transaction can go missing. This lands on the *most common* message shape in
   the corpus, so it is the first thing to check against live data.
2. **2 extra credit↔debit inversions (4 vs 2), all on credit-card payment
   confirmations** — `PAYMENT ... RECEIVED TOWARDS YOUR CREDIT CARD`, `Payment of
   Rs 13263 was credited to your card`. v7 calls these DEBIT, the app stored
   Credited. These *do* affect balance math, and this class is the reason
   `typeAgree` fell 358 → 352.

Neither is a blocker on the measured balance of evidence, but both are real and
should be re-checked once live 4.0.6 data is available.

### Rollback (one file, still true)

`tools/sms_model/sms_model_v5_rollback.bin` is the exact previous live model.
Copy it to `app/src/main/assets/`, point `MlParserHolder` at it, bump
`PARSER_VERSION`. The `.kt` files do **not** need reverting — v5 and v7 are the
same v2 format/architecture. Reverting to true v1 additionally needs the three
v1 `.kt` files plus `sms_model_v4_v1format_rollback.bin` (that one is v1 format
and will fail the `require(version == 2)` gate under current code, by design).

---

## READ THIS FIRST — do not migrate blindly

v2 is **not strictly better** than v1. Measured, both directions:

| Metric | v1 (104 KB) | v2 (307 KB) |
|---|---|---|
| Novel/unseen phrasings (8 cases) | 5/8 | **7/8** |
| Checkpoint strings (7 known real) | 7/7 | 7/7 |
| Amount-numeric on 3175 real msgs | **1013** | 950 |
| Non-txn detection (YESBNK, 32 msgs) | **~1 false** | 11 false |

v2 generalizes better to bank formats it has never seen. v1 is sharper on the
formats already covered. The regression is structural, not a bug: v2 averages
character-trigram embeddings, which is what buys synonym generalization and
what blurs sharp boundary decisions.

**Recommended: keep v1 in production for now.** Migrate only if (a) a
reconciliation round has fixed the non-txn regression, or (b) coverage of
unknown banks matters more than precision on known ones for a specific build.

Whatever you do: **keep the previous `.bin` in the repo.** Rollback is then a
one-file revert, not a retrain.

---

## Files: what changes, what does not

Verified by diff, not assumed.

### Changed (3 files + 1 asset)

| File | Scope of change |
|---|---|
| `smsmodel/Tokenizer.kt` | **Full replace.** New API + multi-trigram hashing |
| `smsmodel/ModelWeights.kt` | 8 lines — format version 2, extra header field |
| `smsmodel/SmsParser.kt` | 16 lines — embedding lookup only |
| `assets/sms_model_*.bin` | Replace with `sms_model_pretrained_v2.bin` |

### Unchanged — do NOT touch (byte-identical between v1 and v2)

- `smsmodel/GruCell.kt`
- `smsmodel/CrfDecoder.kt`
- `smsmodel/BankSenderResolver.kt`
- `viewModelModules/BankTransactionFilterMl.kt` — all guardrails, gates,
  `AccountMemory`, `TransactionRowValidator` keep working as-is
- Room schema, `BankSMSModell`, every caller of `bankTransactionFilter`

---

## Step 1 — Asset

Copy `sms_model_pretrained_v2.bin` into `app/src/main/assets/`.

Keep the filename consistent with what `MlParserHolder` opens. Current code
reads `"sms_model_v3.bin"` — either rename the new file to that, or update the
holder. Do not leave a stale v1 `.bin` under the name the holder loads.

## Step 2 — `Tokenizer.kt`

Replace the whole file with `Tokenizer_v2.kt` (rename class file back to
`Tokenizer.kt`, keep the package line).

**API change — this is the breaking one:**

```kotlin
// v1
fun tokenizeWithFutureMarker(text: String): Pair<List<String>, IntArray>   // one id per token

// v2
fun tokenize(text: String): Pair<List<String>, List<IntArray>>             // MANY ids per token
```

Why: v1 hashed a whole token into one bucket, so `debit`, `debited` and
`debiting` were three unrelated buckets. v2 hashes each character trigram
separately; the model averages them, so word forms share representation.
That is the mechanism the pretraining exploited.

The future-marker token is still prepended, now inside `tokenize()` — there is
no separate `tokenizeWithFutureMarker` any more.

## Step 3 — `ModelWeights.kt`

Two edits (or just copy `ModelWeights_v2.kt`):

```kotlin
// 1. version gate
require(version == 2) { "unsupported model version $version (expected 2)" }

// 2. header now has FIVE ints, not four — maxTri was added
val vocab = d.readIntLE(); val emb = d.readIntLE(); val hid = d.readIntLE()
val tags = d.readIntLE(); val maxTri = d.readIntLE()
```

`maxTri` is exposed as a field on `ModelWeights` and passed through the
constructor. Header is self-describing, so dims (emb 64 / hidden 64) need no
hardcoding anywhere.

**A v1 `.bin` will now fail the `require` with a clear message.** That is
intended — it prevents silently loading a v1 asset into v2 code, which would
produce garbage output rather than an error.

## Step 4 — `SmsParser.kt`

Only the embedding step changes:

```kotlin
// v1
val (tokens, ids) = Tokenizer.tokenizeWithFutureMarker(full)
val x = Array(T) { t -> FloatArray(w.embDim) { c -> w.emb[ids[t], c] } }

// v2 — mean of the token's trigram embeddings
val (tokens, triIds) = Tokenizer.tokenize(full)
val x = Array(T) { t ->
    val tris = triIds[t]
    FloatArray(w.embDim) { c ->
        var s = 0f
        for (id in tris) s += w.emb[id, c]
        s / tris.size
    }
}
```

Everything else in the file — biGRU loop, CRF decode, span merging, VPA join,
`normalizeAccount`, confidence from the isBank head — is unchanged.

## Step 5 — Force a reparse

Bump the version constant so `ReparseMigration` re-runs once and rebuilds
stored rows from their original bodies:

```kotlin
const val PARSER_VERSION = "sms_model_v2pre+guards3"
```

Without this, existing Room rows keep v1's parse results.

---

## Verification — run before shipping, in this order

1. **Loads at all.** Log `vocab/emb/hidden/tags/maxTri` on first parse.
   Expect `4096 / 64 / 64 / 11 / 8`. Wrong numbers = wrong asset.
2. **Known-good strings still work.** These 7 pass on both v1 and v2, so any
   failure here means a port bug, not a model difference:
   - `Sent Rs.706.00 From HDFC Bank A/C *5665 To Blinkit ...` → 706.00 / 5665 / DEBIT
   - `Credit Alert! Rs.150.00 credited to HDFC Bank A/c XX5665 ...` → 150.00 / 5665 / CREDIT
   - `E-Mandate! Rs.1999.00 will be deducted ... For INDmoney ...` → 1999.00 / OTHER
   - `Update! INR 35,758.00 deposited in HDFC Bank A/c XX5665 ... Salary ...` → 35,758.00 / CREDIT
   - `Mandate Set Rs.15000.00 For AWS India ...` → NOT a transaction
   - `UPI Mandate: Sent Rs.2.00 from HDFC Bank A/c 5665 To AWS India ...` → 2.00 / DEBIT
   - `Sent Rs.30.00 from Kotak Bank AC X4029 to bharatpe...@unitype ...` → 30.00 / 4029 / DEBIT
3. **Non-transaction regression — the known weak spot.** Check statements,
   mandate-set notices, and promotional/telecom messages are still dropped.
   v2 is measurably worse here; if it is bad in-app, that alone is reason to
   roll back to v1.
4. **Shadow mode if unsure.** Run v2 alongside the live path and log diffs
   instead of switching writes (§5 wiring comment at the bottom of
   `BankTransactionFilterMl.kt`).

---

## Rollback

1. Restore the v1 `.bin` to `assets/`.
2. Restore v1 `Tokenizer.kt`, `ModelWeights.kt`, `SmsParser.kt`.
3. Bump `PARSER_VERSION` again so rows get reparsed by v1.

Note the version gate makes a mismatched pair fail loudly at load, not
silently — so a half-finished rollback will crash on first parse rather than
quietly write wrong amounts to Room. Do all three steps together.

---

## What v2 actually gained (for context, not action)

Self-supervised pretraining (masked-token prediction over 128k lines of
banking text, including all 3175 real messages — no labels needed) taught the
embedding space that money-movement verbs are interchangeable, **without
anyone labelling them**. Measured before any task training:

```
deposited -> credited 0.60 | received 0.45 | settled 0.37
refunded  -> reversed 0.47 | added 0.45
deducted  -> remitted 0.43 | disbursed 0.36
24/24 verbs ranked verbs as nearest neighbours; 0 explained by shared spelling
```

Consequence in practice: unseen verbs like *remitted, disbursed, settled,
collected, stands debited, has been reversed* parse correctly without a
template being written for each one. That is the whole point of v2, and the
reason it is worth reconciling rather than discarding.

Known ceiling, unchanged: no reasoning. "Will be debited" is handled by the
deterministic future-marker token plus the KT gates, not by the model
inferring that future tense means no money moved yet.
