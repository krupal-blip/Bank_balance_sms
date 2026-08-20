# sms_trans_tracker — SMS → Transaction

> **Hand-out build.** Read [`HANDOUT_NOTES.md`](HANDOUT_NOTES.md) first — it lists what was
> removed from this archive (the real India corpus) and where to start.

Extracted from **Bank Balance Checker 4.1.1** (versionCode 412), India / TRAI-DLT region.

This folder is a **read-only snapshot**. Nothing here is on any Gradle source set, so
it does not compile and cannot affect the app. It exists so the SMS→transaction feature
can be re-created for another region without re-deriving the design.

## Two halves — pick the one you need

| | `src/` + docs here | [`template/`](template/) |
|---|---|---|
| What | The **India implementation**, verbatim | The **region-neutral skeleton** |
| India content | Everything — real data, real regexes, real model | Hints only, marked `// IN-HINT:` for deletion |
| Use it to | Understand how it actually works; check a hint against real code | **Build a new region** |
| Start at | `PIPELINE.md` | `template/README.md` |

Porting to a new region → go to **[`template/README.md`](template/README.md)**. It carries
the `TODO(REGION)` worklist (47 items), the knob-by-knob table with India's values
(`template/REGION_CONFIG.md`), the corpus you must collect first
(`template/corpus/CORPUS_SPEC.md`), and the model-retranslation guide
(`template/MODEL_TRAINING.md`) — the model is India-trained and cannot be reused as-is.

Snapshot taken 2026-08-19 from `app/src/main/…`. Every `.kt` is a verbatim copy; every
`.excerpt` is a verbatim slice of a larger shared file, with original line numbers kept
in the header so it can be diffed back to source.

---

## What this feature does

Reads the device SMS inbox (and each newly-arriving SMS), decides which messages are
bank transaction messages, extracts **bank / account / amount / balance / merchant /
credit-or-debit** out of free-form text, and writes one `BankSMSModell` row per real
money movement into Room.

The extractor is a **quantized bidirectional-GRU + CRF sequence tagger** that ships as a
315 KB binary asset and runs fully on-device. No network, no server, no vendor SDK.
Around it sits a Kotlin guardrail layer that repairs and vetoes the model's output, and a
batch layer (KPDO) that reasons across messages.

---

## Folder map

| Path | Role |
|---|---|
| `src/smsmodel/` | The on-device model: tokenizer, GRU cell, CRF decoder, weight loader, parser, DLT sender lookup |
| `src/pipeline/` | Ingestion + guardrails: receiver, inbox importer, single-message filter, reparse and cleanup migrations |
| `src/kpdo/` | Cross-message batch engine (pairing, dedup, balance-chain check) |
| `src/model/` | `BankSMSModell` — the transaction row (the actual output entity) |
| `src/database/` | Room DB, DAO, and `BankDataModel` (bank-master entity — see note below) |
| `src/shared/*.excerpt` | The only slices of the app's big shared files that this feature touches |
| `assets/sms_model_v7.bin` | Active model weights (315 KB, identical to `tools/sms_model/sms_model_v7_final.bin`) |
| `corpus/corpus_format_example.csv` | Corpus **file format** + fabricated example rows. The real 371-message India corpus is **removed from this hand-out** — see `HANDOUT_NOTES.md` |
| `corpus/DLT_SMS_TEMPLATES.md` | Indian bank SMS format reference (what the templates look like per bank) |
| `corpus/MIGRATION_v1_to_v2.md` | Tokenizer/embedding v1→v2 migration writeup (why trigram averaging) |
| `manifest/AndroidManifest.snippet.xml` | The permissions + receiver declaration the feature needs |
| `PIPELINE.md` | Stage-by-stage flow and the data contract at each hop |
| `REGIONALIZE.md` | The port checklist: what to replace for a non-India region |

### Note on `BankDataModel` vs `BankSMSModell`

The goal named `BankDataModel`, so both are included, but they are **different tables**:

- **`BankSMSModell`** is the SMS→transaction output row. This is the entity the whole
  pipeline writes. Unique index on `(body, date)`.
- **`BankDataModel`** is the bank-master / branch record (IFSC, ICR, branch, district,
  user's saved account). It is populated from the bundled `bankbalance.db` + IFSC JSON
  assets and from user input — **the SMS pipeline never writes it**. It shares the same
  Room database and appears in the same DAO, which is why it reads as part of the
  feature. For a port, `BankSMSModell` is mandatory; `BankDataModel` is only needed if
  the new region also wants the branch-lookup feature.

---

## Not copied, on purpose

| Thing | Why | Where it is |
|---|---|---|
| `FinanceProjection` / `FinanceProjector` / `FinanceDao` | Downstream consumer, not part of extraction. The pipeline only calls `rebuild()` / `ensureBuilt()` after inserting rows. | `app/…/finance/` |
| `utils/Extensions.kt` in full (49 KB) | Only 7 functions are on this path; they are in `src/shared/PipelineExtensions.kt.excerpt` | `app/…/utils/Extensions.kt` |
| `constants/Constant.kt` in full | Only the type vocabulary matters; in `src/shared/PipelineConstants.kt.excerpt` | `app/…/constants/Constant.kt` |
| `getBankList()` (~400 lines of `R.drawable` refs) | Region logo table, meaningless without this app's drawables | `app/…/model/LocalSmsGet.kt` |
| UI (`SmsListFragment`, `TransactionAdapter`, `NewTransactionShowAActivity`, layouts) | Presentation, fully replaceable per app | `app/…/fragment`, `adapter`, `activity` |
| `sms_model_v4_v1format_rollback.bin`, `sms_model_v5_rollback.bin` | Rollback targets only, 420 KB of dead weight here | `tools/sms_model/` |
| Trainer (`pretrain.py` and friends) | Not in this repo — the `.bin` is the shipped artifact. `Tokenizer` must stay byte-compatible with the trainer's `trigram_ids`. | external |

## Runtime dependencies

Room `2.8.4` (`room-ktx` + `room-compiler` via KSP), kotlinx-coroutines `1.11.0`.
Nothing else — the model is hand-rolled Kotlin float math, no TFLite / ONNX / ML Kit.
minSdk 24.
