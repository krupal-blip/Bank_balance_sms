# 03 — Bank code lookup

Find a branch's routing code, or resolve a code back to a branch.

India source: `activity/IfscActivity.kt` (553 lines), `adapter/IfscBankListAdapter.kt`,
`fragment/IfscHistoryFragment.kt`, `apiUtils/ApiInterface.kt` (`@POST("bank")`,
`@POST("branch")`), asset `bankbalance.db` → `tbl_bank_info` (48 rows).

## How India does it

Drill-down: **bank → state → city → branch**, each level a `@POST` returning the next list.
The result carries **IFSC** (11 chars, `AAAA0BBBBBB` — 4-char bank code, `0`, 6-char branch),
plus **MICR** (9 digits, cheque clearing) and **ICR**. Searches are kept in a local history.

## What changes per region

The *concept* is universal, the *format* is not. Only the code system and the drill-down depth
change — the screens, search, and history are reusable.

| Market | Primary code | Shape | Drill-down |
|---|---|---|---|
| India | IFSC | `^[A-Z]{4}0[A-Z0-9]{6}$` | bank → state → city → branch |
| EU | IBAN + BIC/SWIFT | BIC `^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$` | bank → branch (flat) |
| US | ABA routing | 9 digits, **checksum-validated** | bank → branch |
| UK | Sort code | `^\d{2}-?\d{2}-?\d{2}$` | bank → branch |
| AU | BSB | `^\d{3}-?\d{3}$` | bank → branch |
| CA | Transit + institution | 5 + 3 digits | bank → branch |

## Three notes

**1. Most markets are flatter than India.** India needs state and city because the registry is
huge and geographically indexed. A flat national registry means `bank → branch` — **delete the
two intermediate screens** rather than shipping them empty.

**2. Validate offline before you call the API.** IFSC has a fixed shape; ABA has a real
checksum; IBAN has mod-97. Rejecting a malformed code locally is instant and saves a
round-trip. India relies on the server.

**3. Secondary codes are optional.** MICR and ICR are cheque-clearing artefacts. If your
market has no cheque system worth speaking of, drop those fields — don't render empty rows.

## Files

- `src/BankCodeLookup.kt.template` — validation, drill-down, offline-first
