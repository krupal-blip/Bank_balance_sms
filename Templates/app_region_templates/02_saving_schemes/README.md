# 02 — Saving schemes

Reference screens for government/state savings products: what they are, current rate,
eligibility, lock-in, tax treatment.

India source: `activity/SavingSchemesActivity.kt`, `fragment/SavingSchemes.kt`,
`res/layout/{activity,fragment}_saving_schemes.xml`.

## How India does it

8 hardcoded schemes — **PPF, EPF, NPS, SCSS, NSC, Post Office savings, PM Vaya Vandana
Yojana, PM Jan Dhan Yojana**. The fragment fires an `Intent` with a `"name"` extra; the
activity then picks content from **`loadData_EN()` or `loadData_HI()`** — two hand-maintained
Kotlin functions holding the same content in two languages.

## Why this is the highest-effort port of the five

**Nothing transfers.** The products, interest rates, tax treatment, eligibility ages,
lock-in periods and contribution caps are all Indian statute. This is not a translation
job — it is a research job in your market's tax and savings law.

You need, per scheme: name, what it is, current rate, who can open it, minimum/maximum
contribution, lock-in, tax treatment, and the official source URL.

## Two things to fix while porting — don't inherit these

**1. Content belongs in data, not in Kotlin.** `loadData_EN()` / `loadData_HI()` means adding
a language is a code change, and the two copies drift silently — nothing detects that the
Hindi rate was left stale after an English update. Move to
`data/schemes.<region>.json` + string resources, and the whole `loadData_*` pattern
disappears.

**2. Rates go stale, and a stale rate is worse than no rate.** These are published figures
users may act on. India's are revised **quarterly** by the Ministry of Finance and are
compiled into the binary — an app release is required to correct one.

Non-negotiable for the port:
- store `rate`, `rateEffectiveFrom`, and `sourceUrl` per scheme
- **display the effective date next to every rate**
- link the official source so the user can verify
- state plainly that figures are informational, not advice

If you cannot commit to keeping rates current, ship the schemes **without** rates —
description, eligibility and the source link still carry real value, and none of it expires
quarterly.

## Files

- `src/SchemeCatalog.kt.template` — data-driven catalog + staleness handling
- `data/schemes.example.json` — file shape, fabricated placeholder region
