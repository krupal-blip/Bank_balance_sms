# PIPELINE — how SMS becomes a transaction row

Two entry points, one shared extraction core, one Room table.

```
                ┌──────────────────────── ENTRY ────────────────────────┐
                │                                                       │
   new SMS arrives                                        user opens app / first sync
   android.provider.Telephony.SMS_RECEIVED                Repository.kt:98
                │                                                       │
   MessageListener (BroadcastReceiver)                     SmsTransactionIngestor
   goAsync() → Dispatchers.IO                                .importInbox(limit)
                │                                          single-flight Mutex
   SmsTransactionIngestor                                          │
     .importIncoming(intent)                              ContentResolver query
                │                                         Telephony.Sms.CONTENT_URI
   getMessagesFromIntent → join bodies                    date DESC
   dao.isMassageAlreadyExistInDatabase(body)                      │
                │                                         per row → SmsRecord
                │                                         drop dup bodies (HashSet)
                │                                         drop bodies already in DB
                │                                         yield()+delay every N rows
                │                                                │
                │                                         List<SmsInput>
                │                                                │
                ▼                                                ▼
       bankTransactionFilter(...)                       KpdoEngine.process(inputs)
       (1 message, no cross-message pass)               (batch, 2 passes)
                │                                                │
                └──────────────► bankTransactionFilterMl ◄────────┘
                                  (the real extractor)
                                          │
                                          ▼
                                   BankSMSModell?  (null = not a transaction)
                                          │
                            dao.insertAllBank(rows)   ← OnConflictStrategy.IGNORE
                            unique index (body, date)
                                          │
                            FinanceProjection.rebuild(context)
```

---

## Stage 1 — capture

### Live path: `MessageListener` (`src/pipeline/MessageListener.kt`)

`BroadcastReceiver`, registered with `intent-filter android:priority="999"`.

**Hard constraint:** `onReceive()` is on the main thread. The receiver must
`goAsync()`, launch on `Dispatchers.IO`, wrap work in `withTimeoutOrNull`, and call
`pendingResult.finish()` in `finally`. Any blocking or suspending call made directly in
`onReceive()` is an ANR. This is the reference implementation for that shape — keep it.

After a row is inserted it also raises the "new transaction" notification
(`RemoteViews`, `CHANNEL_ID`) and sets `Constant.isNewTransactionAdded`.

### Backfill path: `SmsTransactionIngestor.importInbox()`

Called from `Repository.kt:98`. Guarded by `importInboxMutex` because `onResume` and
`initDate` both used to trigger it and each saw an empty DB, double-inserting the whole
inbox.

It **collects first, parses once as a batch**. That is deliberate: KPDO's Pass 2 cannot
pair a two-SMS payment or resolve account inheritance chronologically if messages are fed
one at a time.

**Contract out of stage 1:** `SmsInput(address, body, date, massageId, typeID, thread)` —
`date` is epoch-millis **as a String**, matching `BankSMSModell.date`.

---

## Stage 2 — extraction, per message

`bankTransactionFilter` (`src/pipeline/BankTransactionFilter.kt`) is now a thin
pass-through to `bankTransactionFilterMl` (`src/pipeline/BankTransactionFilterMl.kt`).
That file is the heart of the feature: ~450 lines, model call plus every guardrail.

Order of operations:

1. **`MlParserHolder.get(env)`** — lazily loads `assets/sms_model_v7.bin` through
   `ModelWeights.load()` and caches ONE `SmsParser`. Loading is via the
   `SmsFilterContext` interface (`openAsset` / `getBankName` / `getMerchantName`), so the
   parser is testable without Android.

2. **`SmsParser.parse(body, address)`** — see stage 3.

3. **`BankSenderResolver.resolve(address)`** — DLT-header lookup, independent of the
   model. Strips the 2-char prefix (`VM-`, `AD-`) then matches the bank's **root ticker**
   (`HDFC` covers `HDFCBK`/`HDFCCC`/`HDFCLN`/…), longest root wins. Returns `null` for
   bare phone numbers and for co-op/RRB senders with no consistent code — caller falls
   back to the model's BANK span. 113 roots.

4. **Confidence gate** — `MIN_CONFIDENCE = 0.80f` on the "is this a transaction?" head
   only. Type/source uncertainty deliberately does **not** kill a row whose amount and
   account are clean and separately validated.

5. **Veto guardrails** (`TransactionRowValidator`) — drop mandate/e-mandate notices, OTP
   messages, declined transactions, payment reminders, statement/min-due alerts, rows
   where the "account" is actually the amount, and messages from personal phone numbers
   with no bank hint.

6. **Numeric repair** — the model's spans get cross-checked against regex over the raw
   body: `repairTruncatedNumber`, `repairTruncatedBalance`,
   `repairAmountAgainstBalance`, `isBodyMoneyToken`. A truncated amount is repaired from
   the body rather than saved wrong. `BigDecimal` throughout — never `Double` for money.

7. **Account resolution** — legacy regex extractor first
   (`String.getAccountNumber()`), then the model's normalized span, then
   `AccountMemory.recall()` (last account seen for that bank) for messages that carry no
   account at all. Credit-card bodies own their own account: if the body says
   "credit card" and carries a card-ending, that wins and `typeOf` becomes `CARD`.

8. **Type reconciliation** — model head vs `String.transactionType()` keyword scan.
   Keyword wins when the two disagree and the keyword is not `Other`.

**Contract out of stage 2:** `BankSMSModell?`. `null` means "not a transaction" and is
the normal, expected result for most inbox messages.

---

## Stage 3 — the model (`src/smsmodel/`)

| File | Role |
|---|---|
| `Tokenizer.kt` | Regex tokenize → **per-token trigram bucket ids**, FNV-1a hash, `VOCAB_SIZE 4096`, `MAX_TRI 8`. Prepends a synthetic `futuremarkertoken` when a future-tense marker is present. |
| `ModelWeights.kt` | `DataInputStream` reader for the `.bin`; `Tensor(data, rows, cols)`. |
| `GruCell.kt` | One GRU step. **Carries mutable scratch buffers.** |
| `CrfDecoder.kt` | Viterbi decode over the tag lattice. |
| `SmsParser.kt` | Orchestration: embed → biGRU → concat+mean-pool → 4 heads → CRF → span merge. |

Input is `"<address> | <body>"` — the sender is prepended exactly as the training
generator did. Dropping it silently degrades accuracy.

**Embedding:** a token's vector is the **mean of its trigram embeddings**. That is what
makes `debit` / `debited` / `debiting` share representation (`deb`,`ebi`,`bit`) instead of
landing in three unrelated buckets as in v1. `Tokenizer.trigramIds` must stay
**byte-identical to the trainer's `trigram_ids`** or every embedding lookup shifts.

**Heads:** BIO tag sequence (11 tags: BANK/ACCOUNT/AMOUNT/BALANCE/MERCHANT × B/I + O),
`isBank` (2), `ttype` (CREDIT/DEBIT/OTHER), `src` (VIA_BANK/VIA_CARD/NONE).

**Span merge:** first span of each type wins. AMOUNT/ACCOUNT/BALANCE join with no
separator; a MERCHANT span containing `@` joins with no separator so
`rahul123 @ ybl` → `rahul123@ybl`; everything else joins with spaces.

**Account normalization:** `XX5665` / `*5665` / `...5665` / `X5665` all reduce to the
trailing digit run (`5665`) so they group as ONE account. Returns `""` if no run of ≥3
digits exists.

### Two concurrency traps already paid for — do not undo

- **`GruCell` must be created per `parse()` call, never held as a field.** One shared
  `SmsParser` parsing two messages concurrently on `Dispatchers.Default` corrupted each
  other's scratch buffers: ~0.5 % of parses returned a wrong or empty amount under
  8-thread load. `parse()` is reentrant as written.
- **First `BankDataBase.getInstance()` opens the DB and runs migrations eagerly**, so it
  must first run on IO. The app pre-warms it in deferred-init step 0.5.

---

## Stage 4 — cross-message batch (`src/kpdo/KpdoEngine.kt`)

Only on the inbox path. `Dispatchers.Default`. Sorts by date first — Pass 2 is stateful,
so identical input must always give identical output.

**Pass 1, stateless:** every message through `bankTransactionFilterMl`. Every stage-2
guardrail still applies, unchanged. Counts `notTransaction`.

**Pass 2, stateful, three jobs:**

- **2a — notice+ledger pairing.** One money movement can arrive as TWO different SMS
  (HDFC NACH sends a `PAYMENT ALERT … UMRN` notice *and* an `UPDATE: … debited … Avl
  bal` ledger entry). Both parse as valid debits; their refs differ so ref-dedup can't
  pair them; the bodies differ so `unique(body,date)` can't either. Same account + same
  amount within `PAIR_WINDOW_MS` (3 days) = one event: drop the notice (the half with a
  mandate/UMRN marker and no balance), keep the ledger entry. Ambiguous → keep both.
- **2b — dedup** on `(account, amount, type, day, ref)` when a reference exists, else on
  `(account, amount, type, exact-date, body)`. Deliberately does **not** collapse two
  genuine identical repeat payments that carry different refs.
- **2c — balance chain** per account, chronological: `prev ± amount ≈ new_balance`, ±1.0
  tolerance. A free arithmetic audit on the money path that catches misread amounts no
  single-message guardrail can see. **Reports only, never drops** — a missing
  intermediate SMS also breaks the chain.

**Contract out:** `KpdoResult(rows, notTransaction, pairedAway, duplicates, balanceMismatches)`.

The model itself learns nothing here. Weights are frozen; all cross-message knowledge is
plain Kotlin in Pass 2.

---

## Stage 5 — persist (`src/database/`, `src/model/`)

`BankSMSModell` — `@Entity(indices = [Index(value = ["body","date"], unique = true)])`.
All money fields are **`String`**, pre-formatted with the currency symbol (`"₹ 1234.50"`).

`BankDao.insertAllBank(List<BankSMSModell>)` uses `OnConflictStrategy.IGNORE`, so the
unique index is the last line of defence against re-inserting the same message.

`BankDataBase` — Room, `version = 14`, 7 entities, migrations 1→14 all hand-written
(one includes a dedup of rows left by an early duplicate-insert bug in the ingestor).

After any insert: `FinanceProjection.rebuild(context)`; on a no-op pass,
`FinanceProjection.ensureBuilt(context)`.

---

## Stage 6 — maintenance migrations (`src/pipeline/`)

Both run from `BalanceCheckerApplication`'s deferred-init, off the main thread.

- **`ReparseMigration.runIfNeeded()`** — the reason `PARSER_VERSION` exists. Re-runs
  every stored row's **original body** through the current parser and updates **in
  place** (ids preserved, table never nuked). Rows are correlated back by `(body, date)`
  — Room's own unique key. Anything KPDO now drops gets deleted, so a reparse also purges
  historic double-counted NACH rows. Gated on
  `shared.reparsedParserVersion == PARSER_VERSION`; bump the constant after a model
  change and all history is re-polished. Runs **last** in deferred init — inference over
  full history is CPU-heavy and starves main on low-end devices.
- **`PhantomRowCleanup.runOnce()`** — one-shot cleanup, runs right after the Room
  pre-warm.

`PARSER_VERSION = "sms_model_v7+guards9"`. It versions **model + guardrails together**;
changing a regex without bumping it means existing rows keep the old behaviour.

---

## Where each field comes from

| `BankSMSModell` field | Source |
|---|---|
| `bankName`, `logoCode` | `BankSenderResolver.resolve(address)` → else model BANK span → `String.getBankName()` against `Bank_List` |
| `accountNumber` | `String.getAccountNumber()` → model ACCOUNT span (normalized) → `AccountMemory.recall()`; credit-card body overrides |
| `amount` | model AMOUNT span, repaired against body + balance, prefixed with currency |
| `avlBal` | model BALANCE span, repaired via `QUALIFIED_BAL_RE`/`BARE_BAL_RE` → `String.getBalance()` |
| `merchantName` | model MERCHANT span → `String.getMerchantName()` |
| `transactiontype` | model `ttype` head reconciled with `String.transactionType()` keywords |
| `typeOf` | model `src` head; forced to `CARD` for credit-card bodies |
| `body`, `date`, `address`, `massageId`, `typeID`, `thread` | carried straight from `SmsInput` |
