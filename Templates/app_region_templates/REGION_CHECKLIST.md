# REGION_CHECKLIST — all five features, every knob

India's value is a **hint**. Don't copy it forward.

## Step 0 — decide what your market even has

Before any code. Several of these features have no equivalent outside India, and disabling
one is a config line where porting it is weeks.

| Feature | Exists in your market? | If no |
|---|---|---|
| Bank holidays | almost certainly yes | — |
| Saving schemes | yes, but **entirely different products** | research task, not translation |
| Bank code lookup | yes, different code system | swap the system |
| Balance channels | **often not** — USSD/missed-call are largely India/Africa/SE-Asia | drop from `enabledFeatures` |
| Net banking | yes | URL table only |

Record it in `RegionFeatureProfile.enabledFeatures`. An empty screen is worse than a hidden one.

---

## 01 — Bank holidays

| Knob | India | Yours |
|---|---|---|
| Scope | per sub-region, 30+ states | national in most markets → delete the state picker |
| Sub-region label | "State" | "Province" / "Land" / none |
| Recurring closures | **RBI: 2nd + 4th Saturday, all Sundays** | your central bank's rule, or none |
| In the feed already? | yes, as normal rows | **if not, you must generate them** or the calendar is wrong every other week |
| Date format | `d MMMM yyyy` | yours — parse with `Locale.ROOT`, not the device locale |
| Weekend days | Sat/Sun | **Fri/Sat across much of the Gulf** — never hardcode |
| Lunar holidays | Diwali, Eid — shift yearly | keep network as primary; a bundle goes stale |
| Source | own `@POST("bank_holiday")` | central-bank publication or bundle |

## 02 — Saving schemes

| Knob | India | Yours |
|---|---|---|
| Products | PPF, EPF, NPS, SCSS, NSC, Post Office, PM Vaya Vandana, PM Jan Dhan | **100% your statute — nothing transfers** |
| Rate revision | quarterly, Ministry of Finance | who publishes, how often |
| Content location | `loadData_EN()` / `loadData_HI()` in an Activity | **JSON + string resources** |
| Rate freshness | no date shown, compiled into binary | store + **display** `rateEffectiveFrom` |
| Disclosure | — | "informational only" + source link; check local rules |

Can't keep rates current? Ship schemes **without** rates. Description, eligibility and the
source link don't expire.

## 03 — Bank code lookup

| Knob | India | Yours |
|---|---|---|
| Primary code | IFSC `^[A-Z]{4}0[A-Z0-9]{6}$` | ABA (US) / sort code (UK) / BSB (AU) / BIC+IBAN (EU) / transit (CA) |
| Checksum | none, shape only | **ABA is mod-10, IBAN is mod-97 — implement it** |
| Secondary codes | MICR, ICR (cheque clearing) | drop if no cheque system |
| Drill-down | bank → state → city → branch | usually bank → branch — **delete the middle screens** |
| Validation | server-side only | **offline first**, then network |

## 04 — Balance channels

| Knob | India | Yours |
|---|---|---|
| USSD | `*99#` NUUP, per-bank codes | rare outside India / Africa / SE Asia |
| Missed call | widespread | rare in the West |
| SMS keyword | widespread | often being retired |
| IVR | widespread | **the portable one** |
| Bank table | 3 overlapping sources (90 vs 48 banks) | **one JSON, stable ids** |
| Dialling | `ACTION_DIAL` | keep — `CALL_PHONE` is Play-restricted |
| `#` in USSD | encoded as `%23` | **keep, or the code truncates silently** |
| Dual-SIM | picks arbitrary SIM | let the user choose or show which |

## 05 — Net banking

| Knob | India | Yours |
|---|---|---|
| URLs | `netbank_api`, ~48 banks | yours |
| Scheme check | none | **https only** |
| Host allowlist | none | **required** |
| Redirects | all stay in-app | unknown hosts → system browser |
| JS interface | — | **never** |
| Session on exit | persists | clear cookies + storage |

Prefer the system browser over an in-app WebView. Real address bar, real phishing protection,
your app out of the credential path.

---

## Cross-cutting

| Item | Note |
|---|---|
| **Region data in Kotlin** | India hardcodes schemes, banks (15 KB literal) and holidays in code. Move all of it to `data/*.json` + string resources. Target: a new region ships **no new Kotlin**. |
| **Play-restricted permissions** | `READ_SMS`/`RECEIVE_SMS` (SMS parser), `CALL_PHONE` (USSD). Each needs a declaration and review, and **policy differs by market**. Confirm before building. |
| **Locale ≠ region** | A user in Germany may run the app in Hindi. Region drives data; locale drives text. Keep them separate — India conflates them in `loadData_EN`/`loadData_HI`. |
| **Freshness stamps** | Holidays and scheme rates both go stale. Show "as of" everywhere; a silently wrong figure is the failure mode. |

## Worklist

```bash
grep -rn "TODO(REGION)" .    # what to fill in
grep -rn "IN-HINT"      .    # India hints to delete
```
