# US Banking Transaction SMS Templates (US Market Reference)

> **Overview**
>
> In the United States, banking alerts are delivered via registered 5-digit and 6-digit **SMS Short Codes** (e.g., Chase `24273`, BofA `73981`, Wells Fargo `93557`, Citi `95686`, Capital One `227898`) or real-time application push notifications.
>
> Unlike India's TRAI-DLT regulatory format, US bank SMS notifications are governed by **TCPA (Telephone Consumer Protection Act)** and **CTIA Mobile Marketing Guidelines**, requiring clear bank identification, transactional purpose, and standard opt-out keywords (`STOP`, `HELP`).

---

## Placeholder Legend

| Placeholder | Description | US Format Example |
|---|---|---|
| `{amount}` | Transaction amount in USD | `42.50` or `1,250.00` |
| `{date}` | Date of transaction | `08/19/2026` or `Aug 19` |
| `{time}` | Time of transaction | `10:30 AM EST` |
| `{account}` | Masked account suffix | `...4321` or `4321` |
| `{balance}` | Available account balance | `2,450.10` |
| `{merchant}` | Payee or point of sale | `TARGET T-1204` or `WALMART` |
| `{mode}` | Transaction channel | `Debit Card`, `Direct Deposit`, `ATM`, `Zelle`, `ACH` |
| `{card}` | Masked credit/debit card tail | `8892` |
| `{shortcode}` | 5 or 6 digit registered bank code | `24273`, `73981`, `93557` |

---

# Tier 1 — National Money-Center Commercial Banks

## JPMorgan Chase Bank, N.A. (Shortcode: 24273 / CHASE)

### 1. Debit Card Purchase
```text
Chase: A debit card purchase of ${amount} was made at {merchant} on {date}. Available bal: ${balance}. Reply HELP for info.
```

### 2. ATM Cash Withdrawal
```text
Chase: ATM withdrawal of ${amount} from checking (...{account}) on {date}. Available balance: ${balance}.
```

### 3. Payroll / Direct Deposit (Credit)
```text
Chase: Direct deposit of ${amount} from {merchant} was deposited to checking (...{account}). Bal: ${balance}.
```

### 4. Zelle Instant Money Transfer
```text
Chase: You sent ${amount} with Zelle to {merchant} on {date}. Available balance: ${balance}.
```

---

## Bank of America, N.A. (Shortcode: 73981 / 34343)

### 1. Credit / Debit Card Charge
```text
Bank of America Alert: Card ending in {card} was charged ${amount} at {merchant} on {date}. Reply HELP for info or call 1-800-432-1000.
```

### 2. Checking Account Deposit (Credit)
```text
Bank of America: A deposit of ${amount} was posted to your checking account (...{account}) on {date}.
```

### 3. Low Balance Warning (Negative - Zero Money Movement)
```text
Bank of America Alert: Your checking account ending in {account} is below your ${amount} limit. Current balance is ${balance}.
```

---

## Wells Fargo Bank, N.A. (Shortcode: 93557)

### 1. Purchase Alert
```text
Wells Fargo Alert: A purchase of ${amount} occurred on card ending in {card} at {merchant} on {date}. Avail Bal: ${balance}.
```

### 2. ATM Withdrawal
```text
Wells Fargo: ${amount} ATM withdrawal from account ending {account}. Available balance: ${balance}.
```

### 3. Text Banking Balance Command (On-Demand)
```text
Wells Fargo: Your checking account ...{account} has an available balance of ${balance} as of {date}.
```

---

## Citibank, N.A. (Shortcode: 95686 / 692484)

### 1. Credit Card Purchase
```text
Citi Alert: ${amount} charged to {card_name} (...{card}) at {merchant} on {date}.
```

### 2. Bank Account Credit
```text
Citi Alert: Direct Deposit of ${amount} received in checking account (...{account}) on {date}. Avail Bal: ${balance}.
```

---

## Capital One, N.A. (Shortcode: 227898 / 227767)

### 1. Real-time Card Authorization
```text
Capital One Alert: You authorized ${amount} at {merchant} with card ending in {card}. Reply HELP for info.
```

### 2. Payment Received towards Credit Card
```text
Capital One: Payment of ${amount} received towards your card ending in {card}. Thank you.
```

---

## U.S. Bank (Shortcode: 872265)

### 1. Debit Purchase Alert
```text
U.S. Bank Alert: A debit card charge of ${amount} was approved at {merchant} on {date}. Account ending in {account}.
```

---

## PNC Bank (Shortcode: 762265 / 90742)

### 1. Account Debit
```text
PNC Alert: Debit transaction of ${amount} on checking (...{account}) at {merchant}. Available balance: ${balance}.
```

---

# Common US Transaction Channels & Modes
* **Debit Card POS / PIN Purchase**
* **Credit Card Swipe / Tap / Online Charge**
* **ACH Direct Deposit (Payroll, SSA)**
* **ACH Direct Debit / AutoPay (Bill Payment)**
* **Zelle Peer-to-Peer Transfer**
* **ATM Cash Withdrawal**
* **Wire Transfer (Domestic / Fedwire)**
* **Mobile Check Deposit**
* **Fee Debit (Overdraft, Maintenance)**
* **Interest Credit**
