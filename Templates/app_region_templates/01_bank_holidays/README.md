# 01 — Bank holidays

Shows which days banks are closed, optionally per sub-region.

India source: `apiUtils/HolidayInterface.kt`, `models/{BankHolidayResponse,Holiday,HolidayData}.kt`,
`viewModelModules/BankHolidayViewModel.kt`, `activity/{BankHolidaysActivity,HolidayActivity}.kt`,
`adapter/{Holiday,HolidaysHistory}Adapter.kt`, assets `new_state_2024.json` / `new_state_2025.json`.

## How India does it

Retrofit `@POST("bank_holiday")` → `{Status, Response_Code, Response_Msg, Data}`, where `Data`
is a list of `{State_Name, bank_holiday: [{Date, Day, Holiday}]}`. Bundled per-year JSON
(`{"states": {"<State>": [...]}}`) is the offline fallback. Dates arrive as `11 January 2025`.

## What changes per region

| Knob | India | Yours |
|---|---|---|
| Scope | **Per sub-region** — 30+ state calendars | Most markets are **national**, which deletes the whole state-picker UI |
| Sub-region label | "State" | "Province", "Land", "Prefecture", or nothing |
| Recurring closures | **RBI closes banks 2nd + 4th Saturday of every month**, plus Sundays | Your central bank's rule, or none |
| Date format | `d MMMM yyyy` | Yours — parse with `Locale.ROOT` |
| Weekend | Sat/Sun, with the Saturday rule above | **Fri/Sat in much of the Gulf.** Hardcoding Sat/Sun is a real bug there. |
| Calendar system | Gregorian | Some holidays are lunar (Eid, Diwali) and **shift yearly** — they cannot be hardcoded once |
| Source | Own backend endpoint | Central-bank publication, or bundle-only |

## The two traps

**1. Recurring closures may not be in your feed.** India's API returns them as ordinary rows
(`"2nd Saturday Bank Holiday"`), so the app never computes them. If your source omits weekly
or biweekly closures, you must generate them — otherwise the calendar is wrong every other
week and users plan around it.

**2. Lunar holidays move.** Eid, Diwali, Chinese New Year shift each Gregorian year and are
sometimes confirmed only weeks ahead. A bundled asset **will** go stale. Keep the API path as
primary and treat the bundle as a fallback with a visible "as of" date — never ship
bundle-only for a market with lunar holidays.

## Files

- `src/HolidayModels.kt.template` — wire models + domain model, decoupled
- `src/HolidayRepository.kt.template` — API → bundle fallback → recurring-closure fill
- `data/holidays.example.json` — file shape, fabricated placeholder region
