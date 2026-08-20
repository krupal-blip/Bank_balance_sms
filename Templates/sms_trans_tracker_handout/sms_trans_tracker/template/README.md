# SMS → Transaction — RAW TEMPLATE

Region-neutral skeleton of the SMS→transaction feature. **India is present only as
hints**, never as shipped data.

Sibling folder `../src/` is the opposite: the full working **India implementation**,
verbatim. Use it when a hint here is not enough and you need to see the real code.

```
sms_trans_tracker/
  src/        ← India production code, verbatim  (REFERENCE — read, don't ship)
  template/   ← YOU ARE HERE. Skeleton to fill in for a new region.
```

---

## The three markers

Every template file uses exactly these. Grep for them — they are your whole worklist.

| Marker | Meaning |
|---|---|
| `TODO(REGION)` | **You must fill this in for your region.** Never ship a file with one left. |
| `// IN-HINT:` | How India solved it. **Reference only — delete the line after you port it.** An India hint left in the file is a bug waiting to happen. |
| `// KEEP:` | Region-neutral. Copy verbatim, do not "improve" it. Most `KEEP` blocks exist because of a production ANR or a data-corruption bug. |

```bash
grep -rn "TODO(REGION)" template/    # your worklist
grep -rn "IN-HINT"      template/    # India leftovers to delete before shipping
```

Files are named `*.kt.template` so they never compile and never get mistaken for real
source. Rename to `.kt` only after the `TODO(REGION)`s in that file are gone.

**Comment density here is intentional.** These files are documentation that happens to be
shaped like Kotlin. The real code you write from them should follow your project's normal
comment rules — the app's `CLAUDE.md` wants minimal comments, and that applies to what you
ship, not to this template.

---

## Folder map

| Path | What it is | Region work |
|---|---|---|
| `src/region/RegionProfile.kt.template` | **The seam.** One interface holding every region-specific value. | Implement once |
| `src/region/RegionProfile.India.hint.kt.template` | India filled into that seam, as a worked example | Read, then delete |
| `src/smsmodel/REUSE_AS_IS.md` | The 3 pure-math files to copy verbatim from `../src/` | None |
| `src/smsmodel/Tokenizer.kt.template` | Text → trigram bucket ids | Script/language |
| `src/smsmodel/SmsParser.kt.template` | biGRU+CRF inference, span merge | Almost none |
| `src/smsmodel/SenderResolver.kt.template` | Sender ID → bank name | **Full rewrite** |
| `src/pipeline/TransactionFilter.kt.template` | Model call + all guardrails | **Heaviest file** |
| `src/pipeline/SmsIngestor.kt.template` | Inbox read + live SMS | Near none |
| `src/pipeline/MessageListener.kt.template` | BroadcastReceiver | None — copy exactly |
| `src/pipeline/ReparseMigration.kt.template` | Re-parse history on version bump | None |
| `src/kpdo/BatchEngine.kt.template` | Cross-message pairing/dedup/balance chain | Vocabulary only |
| `src/model/TransactionRow.kt.template` | The output entity | Schema choices |
| `src/database/Dao.kt.template` | Room DAO | None |
| `corpus/CORPUS_SPEC.md` | What corpus you must collect first | **Do this first** |
| `corpus/corpus_template.csv` | Corpus file format + fake rows | Fill with real data |
| `manifest/AndroidManifest.snippet.xml.template` | Permissions + receiver | Policy check |
| `REGION_CONFIG.md` | **Every region knob in one table**, India value → what to decide | Your checklist |
| `MODEL_TRAINING.md` | **The model carries India logic.** How to retrain/translate it. | Required |

---

## The one rule

> **The model never decides alone.**

Every field the model emits is validated, repaired, or vetoed by plain code against the
raw message text. That layering is why a wrong span becomes a *dropped row* instead of a
*wrong balance shown to a user*.

A new region's model will be weaker than India's at first — smaller corpus, less tuning.
That is survivable **only** because the guardrail layer catches it. Do not ship model
output raw because the new model scores well on your test set.

---

## Port order

Do not reorder. Steps 1–3 are what make step 4 measurable.

| # | Step | Where | Gate before moving on |
|---|---|---|---|
| 1 | **Collect a real corpus** | `corpus/CORPUS_SPEC.md` | ≥300 real messages, every bank you claim |
| 2 | **Sender + bank identity** | `SenderResolver`, `RegionProfile.banks` | Sender→bank correct on ≥90% of corpus |
| 3 | **Money/account regex + keyword layer** | `RegionProfile`, `TransactionFilter` | Regex-only accuracy measured. **This is your floor and your fallback.** |
| 4 | **Train the model** | `MODEL_TRAINING.md` | Model + guardrails beat the step-3 floor. If not, ship step 3 alone. |
| 5 | **Batch vocabulary** | `BatchEngine` notice/reference markers | Two-SMS payments pair correctly |
| 6 | **Tune thresholds** | confidence, pair window, balance tolerance | Measured on corpus, not guessed |
| 7 | **Set `PARSER_VERSION`** | `TransactionFilter` | Region-tagged value so reparse re-polishes existing rows |

Step 3 shipping alone is a legitimate outcome. A regex-only extractor that is honest about
what it cannot parse beats a model that confidently reports the wrong balance.

---

## What you get if you do this right

One `RegionProfile` implementation per market. The pipeline, the model architecture, the
threading, the batch logic and the database are shared. Adding a third region after the
second should touch one file plus one `.bin`.

That is the whole point of the seam in `src/region/` — in the India code the same values
are scattered across six files (`BankSenderResolver`, `LocalSmsGet.Bank_List`,
`BankTransactionFilterMl`, `Extensions`, `KpdoEngine`, `Constant`). Do not scatter them
again.
