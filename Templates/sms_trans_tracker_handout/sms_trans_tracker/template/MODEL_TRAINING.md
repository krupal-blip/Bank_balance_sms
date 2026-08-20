# MODEL_TRAINING — the model carries India logic, and how to translate it

`assets/sms_model_v7.bin` (315 KB) **cannot be reused for another region.** This document
says exactly what is India-baked inside it, what you must rebuild, and what you can ship
while you wait.

---

## Why the `.bin` does not transfer

The weights encode Indian bank SMS templates and English/Hinglish banking vocabulary. Feed
it a German or Arabic bank SMS and the is-transaction head returns low confidence, the
`MIN_CONFIDENCE` gate drops the row, and you get **zero transactions with no error** — the
worst possible failure shape, because it looks like the feature is simply working and the
user has no bank SMS.

Four separate layers of India logic. All four need attention; only one is "retraining".

| # | Layer | Where it lives | India assumption |
|---|---|---|---|
| 1 | **Script** | `Tokenizer.TOKEN_RE` | Latin letters + ASCII digits only |
| 2 | **Sub-word vocabulary** | trigram buckets in the weights | English morphology (`debit`/`debited`/`debiting` share `deb`,`ebi`,`bit`) |
| 3 | **Templates** | training corpus + synthetic generator | Indian bank message layouts |
| 4 | **Label scheme** | the 4 output heads | Indian product taxonomy (bank a/c vs credit card) |

---

## Layer 1 — script

```kotlin
private val TOKEN_RE = Regex("[A-Za-z0-9]+|[^\\sA-Za-z0-9]")
```

Anything outside `[A-Za-z0-9]` becomes a **single punctuation-class token**. So an Arabic,
Thai, Hebrew, Devanagari or CJK message collapses into a run of one-character tokens
carrying no signal.

| Script family | Change needed |
|---|---|
| Latin + diacritics (EU, LATAM, TR, VN) | Widen the class to Unicode letters: `[\p{L}\p{N}]+`. Character trigrams then work as-is — FNV-1a already hashes UTF-8 bytes. |
| Arabic, Hebrew | Unicode letter class, plus decide RTL digit handling (Arabic-Indic digits `٠١٢` vs ASCII — banks mix them) |
| Devanagari, Thai, Khmer | Unicode letter class. No spaces between words in Thai/Khmer → trigrams over the raw run actually help, but re-derive `MAX_TRI`. |
| CJK | **Different strategy.** Characters are morphemes; trigrams over Han are close to meaningless. Use per-character unigrams + bigrams, or a real segmenter. Raise vocab size well above 4096. |

Whatever you change, the Kotlin and the trainer must change **together** — see the parity
fixture below.

---

## Layer 2 — sub-word vocabulary

The embedding for a token is the **mean of its trigram embeddings**. That is the whole
reason the model generalizes across word forms without a template for each one, and it is
what the self-supervised pretraining phase exploits to discover that money-movement verbs
are interchangeable.

This mechanism is **language-agnostic and worth keeping**. What is India-specific is only
*which* buckets carry meaning, and that is learned — it comes free with retraining.

Two knobs to re-derive, in `Tokenizer`:

| Knob | India | Re-derive because |
|---|---|---|
| `VOCAB_SIZE = 4096` | fits English banking vocabulary | Richer morphology (Turkish, Finnish) or a larger script needs more buckets. Collisions show up as confused word forms. Cost is `.bin` size, roughly linear. |
| `MAX_TRI = 8` | caps trigrams averaged per token | Longer average word length → raise. German compounds (`Kontostandmitteilung`) need well above 8. |

Sanity check before training: bucket your whole corpus and measure the collision rate on
the top 500 tokens. If two frequent, semantically unrelated words share a bucket, raise
`VOCAB_SIZE`.

---

## Layer 3 — templates and corpus

This is the real work, and it is **data work, not ML work**.

### Step 1 — real corpus

See `corpus/CORPUS_SPEC.md`. Minimum 300 real messages, every bank you claim to support,
spanning credit, debit, balance-only, card, recurring-payment, OTP, declined and reminder
messages. India's was 371 messages, **removed from this hand-out** (see
`../HANDOUT_NOTES.md`). Use `../corpus/corpus_format_example.csv` for the file shape.

Real messages, not synthetic. Synthetic-only training produces a model that scores well
against your own generator and fails on the first real bank that words things differently.

### Step 2 — document the templates

`../corpus/DLT_SMS_TEMPLATES.md` is the India equivalent: per-bank message layouts. Build
yours the same way. This document is what the synthetic generator is written from, and it
is also the artifact that survives staff turnover.

### Step 3 — generator + training

The trainer is **not in this repo**; the `.bin` is the shipped artifact. Read
`../corpus/MIGRATION_v1_to_v2.md` for the architecture the `.bin` format expects, then
reproduce this shape:

```
synthetic templates (from step 2)  ─┐
                                    ├─► self-supervised pretrain (trigram embeddings)
large unlabeled message pool       ─┘              │
                                                   ▼
              labeled corpus (step 1) ────► supervised finetune (4 heads + CRF)
                                                   │
                                                   ▼
                                          export .bin  ─► parity fixture ─► ship
```

Two phases matter. Pretraining is what teaches the embedding space that money verbs are
interchangeable; finetuning on a small labeled corpus alone will overfit.

### Step 4 — the `.bin` contract

`ModelWeights.load()` reads a `DataInputStream` in a **fixed tensor order**. Your exporter
must write in exactly the order the Kotlin reads:

- embedding table `[VOCAB_SIZE × embDim]`
- forward GRU: `wIhF`, `wHhF`, `bIhF`, `bHhF`
- backward GRU: `wIhB`, `wHhB`, `bIhB`, `bHhB`
- tag head `tagW`, `tagB` → 11 tags
- is-transaction head `isBankW`, `isBankB` → 2
- transaction-type head `ttypeW`, `ttypeB` → 3
- source head `srcW`, `srcB` → 3
- CRF transition matrix `crfTrans` `[numTags × numTags]`

Read `../src/smsmodel/ModelWeights.kt` for the exact reader. If you change tensor shapes,
change both sides in the same commit.

### Step 5 — the parity fixture (do not skip)

Pick ~50 messages. Run them through the Python tokenizer+model and through the Kotlin
`Tokenizer` + `SmsParser`. **Assert identical token ids and identical output spans.**

India verified this with a Python→Kotlin fixture, and it is the only thing that catches a
`trigram_ids` drift. A one-character difference in the tokenizer shifts every embedding
lookup, and the symptom is not a crash — it is quietly degraded extraction that looks like
"the model just isn't very good".

---

## Layer 4 — label scheme

The four heads encode a product taxonomy. Check each against your market before assuming
the shape carries over.

| Head | India labels | Question for your market |
|---|---|---|
| Tags (11) | `O` + B/I × `BANK` `ACCOUNT` `AMOUNT` `BALANCE` `MERCHANT` | Do you need more? A market quoting both booked and value dates, or FX amounts, needs extra span types. |
| is-transaction (2) | yes / no | Universal. Keep. |
| type (3) | `CREDIT` `DEBIT` `OTHER` | Keep. `OTHER` is load-bearing — it holds real transactions the model can't confidently type, and they must not be dropped. |
| source (3) | `VIA_BANK` `VIA_CARD` `NONE` | India separates bank account from credit card because they behave differently in the ledger. If your market has a third instrument class (e.g. a dominant wallet), it belongs here. |

Adding a tag type changes `tagW` rows, the tag list in `SmsParser`, the CRF transition
matrix size, **and** the span-merge switch. Budget for it up front rather than bolting it
on.

### One India-specific head behaviour to keep

`MERCHANT` spans containing `@` are joined **without spaces** so a UPI VPA comes out as
`rahul123@ybl`, not `rahul123 @ ybl`. UPI is India-specific; the *rule* is not — any market
with handle-style payment identifiers (`@` addresses, PIX keys, email-based transfers) needs
it. Keep it, retarget the example.

---

## The synthetic future-tense token

`Tokenizer` prepends a synthetic token `futuremarkertoken` when the message contains a
future-tense marker (`will be`, `is scheduled to`, …). This gives the classifier an explicit
future-vs-completed signal with **no tensor changes** — a cheap trick worth reusing.

Two requirements:
1. The marker list must match the trainer's `FUTURE_MARKERS` **exactly**.
2. Translate the markers to your language(s). A German model needs `wird`, `werden`,
   `voraussichtlich`; the English list contributes nothing.

Why it matters: "Rs.500 **will be** debited tomorrow" is not a transaction. Without this
signal the model has to infer tense from context and gets it wrong often enough to create
phantom rows.

---

## Evaluation — the gate that decides whether you ship the model at all

Build the regex-only path first (port order step 3). Measure it. That number is your
**floor**.

```
Field-level accuracy on the held-out corpus split:

                     regex-only    model+guardrails
  is-transaction        ____            ____
  bank                  ____            ____
  account               ____            ____
  amount                ____            ____
  balance               ____            ____
  type (credit/debit)   ____            ____
  merchant              ____            ____

  false-positive rate   ____            ____   ← weight this heaviest
```

Rules:

- **Model+guardrails must beat regex-only overall.** If it doesn't, ship regex-only. A
  regex extractor that is honest about what it can't parse beats a model that confidently
  reports a wrong balance.
- **False positives cost more than misses.** A missed transaction is invisible. A phantom
  transaction corrupts the running balance and every projection built on it.
- **Never evaluate on training data.** Hold out a split before you generate anything.
- **Re-run on every guardrail change**, not just model changes — that is why
  `PARSER_VERSION` versions the model and the guardrails together as one string.

---

## If you have no ML capacity

Legitimate and supported. Ship steps 1–3 and 5–7 of the port order and skip step 4:

- Set `USE_ML_PARSER = false` (India keeps this flag for exactly this reason).
- The keyword and regex layer already handles the common templates — it is the model's
  fallback path in production today, not a toy.
- Keep `MIN_CONFIDENCE` and the model call site intact so a `.bin` can be dropped in later
  without touching the pipeline.
- Bump `PARSER_VERSION` when you do add the model. `ReparseMigration` then re-parses all
  existing rows through it, so early users' history gets upgraded automatically.

That last point is the real payoff of the reparse design: **shipping regex-only is not a
dead end.** History is re-polished the day the model lands.
