# Indian Banking Transaction SMS Templates (DLT-Compliant Reference)

> **Overview**
>
> Transaction alerts across Indian banking sectors generally follow structured **TRAI DLT (Distributed Ledger Technology)** messaging guidelines. While the exact wording varies by bank, most transaction SMS messages follow standardized formats with variable placeholders.
>
> **Placeholder Legend**
>
> | Placeholder | Description |
> |------------|-------------|
> | `{amount}` | Transaction amount |
> | `{date}` | Date/Time of transaction |
> | `{account}` | Masked account number |
> | `{balance}` | Available account balance |
> | `{mode}` | UPI / IMPS / NEFT / RTGS / ATM / POS / AEPS |
> | `{merchant}` | Merchant or sender/payee name |
> | `{reference}` | UTR / Transaction Reference / IMPS Ref |
> | `{card}` | Masked debit/credit card number |
> | `{branch}` | Branch or bank name |

---

# Tier 1 — National Public Sector Banks (PSBs)

## State Bank of India (SBI)

### Credit

```text
Dear Customer,
Your A/c {account} has been credited with INR {amount}
on {date} by {merchant}
(Ref: {reference}).
Avail Bal: INR {balance}.
-SBI
```

### Debit

```text
Dear Customer,
Your A/c no. {account}
is debited for Rs.{amount}
on {date}
and A/c {account} credited
(IMPS Ref no. {reference}).

If not done by you,
call 1800111109.

-SBI
```

---

## Punjab National Bank (PNB)

### Credit

```text
PNB Alert:
Rs.{amount} Credited to A/c {account}
via {mode}
on {date}.

Total Bal:
Rs.{balance}.

Download PNB ONE App.
```

---

## Bank of Baroda (BoB)

### Debit

```text
Txn Alert:

Your Bank of Baroda A/c {account}
debited for Rs.{amount}
on {date}
via ATM/POS.

Avail Bal:
Rs.{balance}.

Call 18002584455
if unauthorized.
```

---

# Tier 2 — National Private Sector Banks

## HDFC Bank

### Credit

```text
Money Credited!

Rs.{amount}
deposited to HDFC Bank A/c {account}
on {date}
via {mode}.

Info: {reference}

Bal:
Rs.{balance}

-HDFC Bank
```

---

## ICICI Bank

### Debit

```text
Alert:

Your ICICI Bank A/c {account}
debited for INR {amount}
on {date}
at {merchant}.

Ref:
{reference}

Avail Bal:
INR {balance}
```

---

## Axis Bank

### Credit

```text
Axis Bank:

Account {account}
credited with Rs.{amount}
on {date}
via UPI.

UPI Ref:
{reference}

Net Balance:
Rs.{balance}
```

### Debit

```text
Your Axis Bank Card {card}
has been spent for Rs.{amount}
at {merchant}
on {date}.

Limit Available:
Rs.{balance}
```

---

# Tier 3 — Scheduled Commercial & Co-operative Banks

## Federal Bank

### Credit

```text
Federal Bank:

Rs.{amount}
credited to A/c {account}
on {date}
via IMPS.

Ref:
{reference}

Total Available Balance:
Rs.{balance}
```

---

## Kotak Mahindra Bank

### Debit

```text
Kotak Bank:

Rs.{amount}
debited from A/c {account}
on {date}.

Mode:
{mode}

Available Bal:
Rs.{balance}

Dial 18002090000
if not done by you.
```

---

## State Co-operative Banks

### Saraswat Bank (Credit)

```text
Saraswat Bank:

Your A/c {account}
is credited with
Rs.{amount}
on {date}
through {mode}.

Bal:
Rs.{balance}
```

### Maharashtra State Co-operative Bank (Debit)

```text
MSCB Alert:

Account {account}
debited for Rs.{amount}
on {date}
via ATM cash withdrawal.

Remaining Balance:
Rs.{balance}
```

---

# Tier 4 — Regional Rural Banks (RRBs) & Local Co-operative Banks

## Regional Rural Banks

Examples:
- Baroda UP Bank
- Kerala Gramin Bank
- Other RRBs

### Credit

```text
Gramin Bank Alert:

Your Account {account}
has been credited
for Rs.{amount}
on {date}.

Available Balance:
Rs.{balance}
```

### Debit

```text
KGB Alert:

Rs.{amount}
debited from your A/c {account}
on {date}
via AEPS/MicroATM.

Balance:
Rs.{balance}
```

---

## District Central Co-operative Banks (DCCBs)

### Credit

```text
Dear Customer,

A/c {account}
credited with Rs.{amount}
on {date}.

Total Balance:
Rs.{balance}

- {branch} DCCB
```

---

## Urban Co-operative Banks (UCBs)

### Debit

```text
ALERT:

Rs.{amount}
debited from A/c {account}
on {date}.

In case of fraud,
contact your branch immediately.

- {branch} UCB
```

---

# Common Transaction Channels

- UPI
- IMPS
- NEFT
- RTGS
- ATM Withdrawal
- ATM Deposit
- POS Purchase
- E-commerce
- Cash Deposit
- Cash Withdrawal
- AEPS
- MicroATM
- NACH
- ECS
- Standing Instruction
- Auto Debit
- FASTag
- BBPS
- Interest Credit
- Salary Credit
- Refund
- Reversal

---

# Common Variables

| Variable | Meaning |
|----------|---------|
| `{amount}` | Transaction amount |
| `{account}` | Masked account number |
| `{balance}` | Available balance |
| `{merchant}` | Sender / Receiver / Merchant |
| `{reference}` | UTR / Reference Number |
| `{mode}` | Payment mode |
| `{date}` | Date & Time |
| `{card}` | Masked card number |
| `{branch}` | Bank or branch name |

---

# Notes

- These templates are representative examples of commonly observed Indian banking transaction SMS formats.
- Actual SMS content varies by bank, transaction type, CBS platform, and DLT template registration.
- Banks may include additional information such as fraud advisories, customer care numbers, masked identifiers, or promotional text while remaining within TRAI DLT compliance requirements.

---

# References

1. https://support.exotel.com/support/solutions/articles/3000102659-how-to-send-sms-using-exotel-with-dlt-template-scrubbing-
2. https://www.smsgatewayhub.com/sample-templates
3. https://www.smsindiahub.in/sample-templates/
4. https://www.infobip.com/docs/essentials/asia-registration/dlt-templates
5. https://www.scribd.com/document/825760529/SBI-mobile-SMS-debit
6. https://kapsystem.com/blog/transactional-sms-formats-examples-and-templates-for-2025/
7. https://www.scribd.com/doc/316428924/Template
8. https://www.scribd.com/document/498969862/template-guidelines