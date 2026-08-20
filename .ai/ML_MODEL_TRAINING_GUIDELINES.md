# 🧠 ML Model Architecture & Training Guidelines: USA Regional Model (`sms_model_us.bin`)

This document details the neural Natural Language Understanding (NLU) architecture of `sms_model_v7` and provides explicit instructions for training the **USA Regional Model (`sms_model_us.bin`)**.

---

## 🔬 Core Neural Architecture (BiGRU + CRF Multi-Task NLU)

The engine does **NOT** rely on rigid regex rules for classification. It employs a **multi-task neural sequence labeling & classification pipeline**:

```
[Raw Incoming SMS String]
           │
           ▼
┌─────────────────────────────────────────────────────────────┐
│ 1. Tokenizer + FNV-1a Trigram Hasher                        │
│    • Word segmentation + Character Trigram Bucketing        │
│    • Handles word variations (debit/debited/withdrawn/spent)│
│    • Injects `futuremarkertoken` for scheduled vs executed  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Bidirectional GRU (BiGRU Encoder)                        │
│    • Forward & Backward context-aware sequence modeling     │
│    • Disambiguates incoming vs outgoing financial grammar   │
└──────────────────────────────┬──────────────────────────────┘
                               │
          ┌────────────────────┼────────────────────┬────────────────────┐
          ▼                    ▼                    ▼                    ▼
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│ Head 1: isBank   │ │ Head 2: ttype    │ │ Head 3: src      │ │ Head 4: CRF Tags │
│ (2 classes)      │ │ (3 classes)      │ │ (3 classes)      │ │ (11 classes)     │
│ Genuine Txn vs   │ │ CREDIT / DEBIT / │ │ VIA_BANK /       │ │ Token Spans:     │
│ Noise/2FA/Declines│ │ OTHER            │ │ VIA_CARD / NONE  │ │ B/I-AMOUNT,      │
│                  │ │                  │ │                  │ │ B/I-BALANCE,     │
│                  │ │                  │ │                  │ │ B/I-ACCOUNT,     │
│                  │ │                  │ │                  │ │ B/I-MERCHANT,    │
│                  │ │                  │ │                  │ │ B/I-BANK, O      │
└──────────────────┘ └──────────────────┘ └──────────────────┘ └──────────────────┘
```

---

## 🎯 Key Training Instructions for Claude (Trainer Agent)

1. **Training Dataset**:
   * Input file: [`Countries/United_States/sms_parser/us_training_corpus_v1.csv`](file:///Volumes/Extra/backup/R&D/Bank_balance/Countries/United_States/sms_parser/us_training_corpus_v1.csv)
   * Size: **1,041 verified USA SMS messages** (742 Positive Executed Transactions, 299 Negative Samples).

2. **Tokenizer Hyperparameters**:
   * `VOCAB_SIZE`: `4096`
   * `MAX_TRI`: `8` (average trigrams per token)
   * `FUTURE_MARKERS`: `listOf("will be", "is scheduled to", "scheduled for", "scheduled on")`

3. **Binary Tensor Layout (`DataInputStream` Contract)**:
   The exported `.bin` file must write tensors in the exact sequence expected by `ModelWeights.kt`:
   * Embedding Matrix `[VOCAB_SIZE x embDim]`
   * Forward GRU: `wIhF`, `wHhF`, `bIhF`, `bHhF`
   * Backward GRU: `wIhB`, `wHhB`, `bIhB`, `bHhB`
   * Tag Projection Head: `tagW`, `tagB` (11 tags)
   * isBank Head: `isBankW`, `isBankB` (2 classes)
   * Transaction Type Head: `ttypeW`, `ttypeB` (3 classes: CREDIT, DEBIT, OTHER)
   * Source Head: `srcW`, `srcB` (3 classes: VIA_BANK, VIA_CARD, NONE)
   * CRF Transition Matrix: `crfTrans` `[11 x 11]`

4. **Target Binary Export**:
   * Save binary weights to: `app/src/main/assets/models/sms_model_us.bin`
   * Keep India's legacy model in: `app/src/main/assets/models/sms_model_in.bin`
