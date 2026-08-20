# 05 — Net banking portals

Opens a bank's net-banking login page.

India source: `activity/NetBankingActivity.kt` (58 lines — a `WebView` loading the `"uri"`
intent extra), portal URLs in `bankbalance.db` → `tbl_bank_info.netbank_api`.

Lowest-effort port of the five: swap the URL table. **But read the security section — it is
the reason this file is longer than the feature.**

## What changes

| Knob | India | Yours |
|---|---|---|
| Portal URLs | `netbank_api` per row, ~48 banks | Your banks' URLs |
| Data location | SQLite asset column | One JSON with the channel table |

That's it. No format, locale, or regulatory difference in the mechanism.

## Security — the part that matters

**These are real bank login pages.** The user types real credentials into a `WebView` your app
controls. That makes the app part of the credential path, and the bar is higher than for any
other screen in the product.

India's implementation loads whatever URL arrives in the intent extra and overrides
`shouldOverrideUrlLoading` to load every URL in-place — so **any** redirect, ad, or injected
link keeps rendering inside the same WebView, and the user has no way to tell.

Non-negotiable for the port:

| Rule | Why |
|---|---|
| **HTTPS only** — reject `http://` outright | A cleartext login page is a credential leak |
| **Allowlist hosts** against your own table | Stops an open redirect from landing a phishing page inside your chrome |
| **Open unknown hosts in the system browser**, not in-app | The browser shows a real address bar and the user's own security UI |
| **No `addJavascriptInterface`** | Bridges native capability to page JS. Never on a login page. |
| **Show the real host** prominently | The user must be able to see where they are |
| **No `savePassword`, no form autofill** | Don't become a credential store |
| **Clear cookies on exit** | A shared device must not leave a live session |
| **Never log the URL or POST body** | Session tokens and credentials end up in logs and crash reports |

Also worth checking before you ship: some markets' banks **prohibit framing their login page**
in third-party apps, in their terms. Verify, or route to the system browser everywhere — which
is the safer default regardless.

## Recommendation

Prefer `ACTION_VIEW` to the system browser over an in-app `WebView`. You lose in-app styling
and gain: the browser's address bar, its phishing protection, its password manager, and
removal of your app from the credential path entirely. The `WebView` path below exists because
India ships it — it is not the shape to copy.

## Files

- `src/NetBankingPortal.kt.template` — allowlist, host validation, both open paths
