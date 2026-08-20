# HANDOUT_NOTES — read first

This is the **internal hand-out build** of `sms_trans_tracker`, packaged for another region
team inside the company.

Source: Bank Balance Checker **4.1.1** (versionCode 412), India / TRAI-DLT.
Packaged: 2026-08-19.

---

## What was removed, and why

| Removed | Was | Why |
|---|---|---|
| `corpus/real_sms_corpus.csv` | 371 real Indian bank SMS (2,211 lines) | **Real people's financial data.** 84 distinct real counterparty names, each appearing in the message bodies, plus real account tails from ~3 real devices. Not shareable, even internally. |

Replaced by `corpus/corpus_format_example.csv` — the same column layout with **fabricated
rows** using a placeholder region ("Examplia" / `EX$`), covering every message class you
need to collect: debit, credit, card, ATM, handle-payment, balance-only, executed mandate,
mandate created, mandate cancelled, notice+ledger pair, card bill ack, OTP, declined,
reminder, non-uniform grouping, personal number, promo.

**You do not need India's corpus.** It has no porting value — a model trained on Indian
templates cannot be reused for your region, and every threshold has to be re-derived
against *your* data. Collect your own: `template/corpus/CORPUS_SPEC.md` is the spec, and it
includes the privacy rules you must follow while doing it.

## What is included

| Kept | Size | Note |
|---|---|---|
| `src/` | 160 KB | **India production source, verbatim.** Real shipping code — internal only, do not forward outside the company. |
| `assets/sms_model_v7.bin` | 308 KB | The trained India model. Included for reference; **cannot be reused for another region** (`template/MODEL_TRAINING.md` explains why). Internal only. |
| `template/` | 160 KB | The region-neutral skeleton. This is what you build from. |
| `corpus/DLT_SMS_TEMPLATES.md` | 5.7 KB | Indian bank SMS format reference — a worked example of the template document *you* need to produce |
| `corpus/MIGRATION_v1_to_v2.md` | 11 KB | Tokenizer/embedding v1→v2 architecture writeup. Read before retraining. |
| `PIPELINE.md`, `REGIONALIZE.md` | | India design docs |

**Verified clean:** no API keys, no credentials, no Firebase project ids, no
`google-services` data anywhere in this archive. All 84 real counterparty names from the
removed corpus were checked against every text file here — none leaked.

**One residual, disclosed:** `corpus/MIGRATION_v1_to_v2.md` quotes 7 real message excerpts
as worked examples. Their counterparties are all **businesses** (Blinkit, INDmoney, AWS
India, bharatpe), not individuals, and the account references are masked tails (`*5665`,
`XX5665`, `X4029`) of the kind banks themselves broadcast. Judged acceptable for internal
distribution. If this archive ever goes external, drop that file too.

---

## Where to start

1. **`template/README.md`** — the port guide. Start here.
2. **`template/corpus/CORPUS_SPEC.md`** — do this first in practice. Everything else is
   downstream of having a real corpus.
3. **`template/REGION_CONFIG.md`** — every region knob, India's value, your decision.
4. **`template/MODEL_TRAINING.md`** — the model is India-trained; this is how to translate
   it, and how to ship without it if you have no ML capacity.
5. **`PIPELINE.md`** — how the India implementation actually works, stage by stage.

Your worklist is 47 `TODO(REGION)` markers:

```bash
grep -rn "TODO(REGION)" template/    # what you must fill in
grep -rn "IN-HINT"      template/    # India hints to DELETE as you port
```

## Nothing here compiles

Every template file is `*.kt.template`; the India reference under `src/` is not on any
Gradle source set. Rename to `.kt` and wire into your own module only after that file's
`TODO(REGION)`s are resolved.

## Handling

Internal distribution only. Contains production source and a trained model. If this needs
to go to a contractor, vendor, or any external party, strip `src/` and
`assets/sms_model_v7.bin` first — `template/` plus the design docs are enough to do the
port, and the hints stand on their own without the source beside them.
