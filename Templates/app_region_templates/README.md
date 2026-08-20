# app_region_templates — the other region-dependent features

Companion to `sms_trans_tracker/template/`. Same convention, five more features.

Extracted from **Bank Balance Checker 4.1.1**, India. Nothing here is on a Gradle source
set; `*.template` files never compile.

## Markers — your whole worklist

| Marker | Meaning |
|---|---|
| `TODO(REGION)` | Fill in for your region. Don't ship a file with one left. |
| `// IN-HINT:` | How India does it. **Delete after porting.** |
| `// KEEP:` | Region-neutral. Copy as-is. |

```bash
grep -rn "TODO(REGION)" .    # worklist
grep -rn "IN-HINT"      .    # India leftovers to delete
```

## The five

| # | Feature | India-only part | Port cost |
|---|---|---|---|
| [01](01_bank_holidays/) | **Bank holidays** | RBI 2nd/4th-Saturday rule, 30+ state calendars, `bank_holiday` API | Medium — data + one rule |
| [02](02_saving_schemes/) | **Saving schemes** | PPF/EPF/NPS/SCSS/NSC + 3 more govt schemes | High — all content is India policy |
| [03](03_bank_code_lookup/) | **Bank code lookup** | IFSC / MICR / ICR codes, bank→state→city→branch drill-down | Medium — swap the code system |
| [04](04_balance_channels/) | **Balance-check channels** | `*99#` NUUP USSD, missed-call banking, SMS keywords | High — mostly absent outside India |
| [05](05_net_banking/) | **Net banking portals** | Per-bank portal URLs | Low — URL table only |

Not templated (already region-neutral): cards, PDF export, finance projection, paywall,
language switcher.

## One structural change worth making

**India hardcodes region data in Kotlin.** Schemes live in `loadData_EN()` / `loadData_HI()`
as duplicated in-code content; the bank table is a 15 KB `BankArray.kt` literal; holidays are
per-year bundled JSON plus an API.

Every template here moves that to **data files behind one seam** —
`_shared/RegionFeatureProfile.kt.template`. Target state: a new region ships **new JSON and
new string resources, no new Kotlin**. Adding a third region should then be a data task.

Start at [`REGION_CHECKLIST.md`](REGION_CHECKLIST.md) for every knob in one table.
