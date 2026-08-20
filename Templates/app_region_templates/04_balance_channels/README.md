# 04 — Balance-check channels

Ways to check a balance without opening a banking app: USSD code, missed call, SMS keyword,
IVR line.

India source: asset `ussd.json` (~90 banks), asset `bankbalance.db` → `tbl_bank_info`
(48 rows: `bank_inquiry`, `bank_care`, `netbank_api`, `mini_statement`, `bank_short`) and
`smsinfo` (314 rows), `utils/BankArray.kt` (15 KB Kotlin literal), `model/BankModel.kt`
(`BankMissCallModel`, `SmsModel`, `CallModel`), `activity/SendSmsActivity.kt`.

## Read this before porting — most of it doesn't exist outside India

This is the **least portable feature of the five**. India has an unusually rich set of
offline banking channels; most markets have one or none.

| Channel | India | Elsewhere |
|---|---|---|
| **USSD** | `*99#` NUUP — a national UPI-over-USSD service, per-bank codes like `*99*87#` | Common in **Sub-Saharan Africa** (M-Pesa and peers) and parts of SE Asia. **Effectively absent** in EU/US/UK/AU/JP. |
| **Missed call** | Widespread — dial, it hangs up, balance arrives by SMS | Some SE Asian and African markets. Absent in the West. |
| **SMS keyword** | Widespread, per-bank keywords | Some markets; often being retired |
| **IVR toll-free** | Widespread | **The most portable of the four** — nearly every market has one |

**If your market has none of these, do not port this feature.** Delete it from the profile's
`enabledFeatures`. An empty channel screen is worse than an absent one — and hiding it is one
config line.

Realistic outcome for a Western market: keep **IVR only**, and the feature becomes a
per-bank phone list.

## What changes

| Knob | India | Yours |
|---|---|---|
| Channels present | all four | usually IVR, sometimes none |
| USSD code shape | `*99*87#`, `#99*54#` | if you have USSD at all |
| Bank table | ~90 banks across `ussd.json` + `tbl_bank_info` + `BankArray.kt` | your banks, **one source** |
| Data location | split across 2 assets + a 15 KB Kotlin literal | one JSON |

## Fix while porting: three sources become one

India's bank table is split across `ussd.json`, the `tbl_bank_info` SQLite table, and
`BankArray.kt`. They overlap, disagree on bank naming, and cover different bank counts
(90 vs 48). Consolidate to a single `data/channels.<region>.json` keyed by a stable bank id.

## Platform reality

- **`CALL_PHONE` / USSD dialling is restricted.** Placing a USSD call needs `CALL_PHONE`,
  another Play-restricted permission with a policy review. Prefer `ACTION_DIAL`
  (pre-fills the dialler, user presses call) — **no permission required**, and it is the
  honest interaction anyway.
- **Sending an SMS needs `SEND_SMS`** — also restricted. Prefer an SMS intent with the body
  pre-filled.
- **USSD codes contain `#`,** which must be URL-encoded as `%23` in a `tel:` URI or the code
  is silently truncated. India's `SendSmsActivity` handles this; keep it.
- **Dual-SIM** devices pick an arbitrary SIM. A USSD balance check on the wrong SIM fails
  confusingly. Let the user choose, or at least say which SIM was used.

## Files

- `src/BalanceChannels.kt.template` — one consolidated table + intent builders
- `data/channels.example.json` — file shape, fabricated placeholder region
