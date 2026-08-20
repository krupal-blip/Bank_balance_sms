# 🌍 Global Regional Expansion Blueprint: 1-Click Multi-Country Pipeline

This blueprint defines the standardized, zero-friction automation protocol for onboarding any new country (UK, Canada, Australia, Germany, UAE, etc.) from Ground Zero to 100% Verified Production with only **1–2 clicks**.

---

## ⚡ 1-Click Autonomous Architecture

```
                                 [USER ACTION 1]
                     Run: ./onboard_region.py --country "UK" --currency "GBP"
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ 1. SCAFFOLDING & REGION PROFILE GENERATION (Antigravity PM)                                  │
│    • Creates Countries/{Region}/ directory structure (config, schemes, holidays, banks)     │
│    • Initializes region JSON profile & registers RegionFeatureProfile seam                  │
│    • Generates initial bank shortcodes & regex templates from official clearing specs       │
└──────────────────────────────────────┬──────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ 2. CLAUDE QA & GROUND TRUTH SYNTHESIS (Continuous Loop)                                     │
│    • Claude generates 10 batches (100–120 SMS each, 40% negatives, local banking lifecycles)│
│    • Pushes samples/{region}/{region}_batch{N}.xml + expected.json to main                  │
└──────────────────────────────────────┬──────────────────────────────────────────────────────┘
                                       │ (GitHub Auto-Sync)
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ 3. AUTONOMOUS AUDIT & OPENCODE AUTO-FIX DAEMON                                              │
│    • Ingests 1-by-1 SMS chronologically ➔ Simulates Passbook Ledger                         │
│    • Dual-Table Audit (OpenCode Parsed vs Claude Ground Truth)                               │
│    • IF MISMATCH: OpenCode auto-patches parser regex/formulas until 100% MATCH               │
│    • IF MATCH: Auto-signs off & triggers next batch signal                                   │
└──────────────────────────────────────┬──────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│ 4. 100% PRODUCTION GREEN-FLAG CERTIFICATION                                                 │
│    • Compiles Master 11-Column ML Training Corpus (`{region}_training_corpus_v1.csv`)        │
│    • Emits Certification Audit Report (`automation/reports/{REGION}_READINESS_REPORT.md`)   │
└──────────────────────────────────────┬──────────────────────────────────────────────────────┘
                                       │
                                 [USER ACTION 2]
                     Trigger Model Training for {region} (`sms_model_{region}.bin`)
```

---

## 📋 Mandatory vs. Autonomous Action Breakdown

| Step | Pipeline Stage | Who Executes? | User Action Required? | Description |
|:---:|---|:---:|:---:|---|
| **1** | **Region Initializer** | **User (Action 1)** | **YES (1 Command)** | Run `./onboard_region.py --country "UK"` to scaffold directory & registry |
| **2** | **Test Data & QA** | **Claude Code** | ❌ None (Autonomous) | Synthesizes 10 batches of realistic SMS + Ground Truth JSON |
| **3** | **Ingestion & Replay** | **OpenCode** | ❌ None (Autonomous) | Replays SMS, parses fields, maintains simulated ledger balances |
| **4** | **Dual-Table Verification** | **Antigravity PM** | ❌ None (Autonomous) | Audits table match to the exact penny ($0.00 diff) |
| **5** | **Parser Auto-Fix** | **OpenCode** | ❌ None (Autonomous) | Patches regexes & ledger formulas on mismatches |
| **6** | **Corpus Compilation** | **Engine** | ❌ None (Autonomous) | Compiles 1,000+ SMS rows into standard 11-column CSV |
| **7** | **Model Training** | **User (Action 2)** | **YES (1 Click)** | Runs BiGRU+CRF trainer on the generated CSV for that region |

---

## 🛠️ Step-by-Step Execution Guide for Any Next Country:

### 1️⃣ When Ready for Next Region (e.g. UK):
Run the single onboarding command:
```bash
python3 automation/engine/onboard_region.py --country "United_Kingdom" --code "GB" --currency "GBP" --symbol "£"
```

### 2️⃣ Claude CLI Automatically Starts Generation:
In your Claude terminal tab:
```text
Generate UK Batches 1 to 10 as per CLAUDE.md for region GB. Push each batch to main.
```

### 3️⃣ Watch Live on Dashboard:
Open **`http://localhost:8088`** — Table 1, Table 2, and live cognitive reasoning logs will stream autonomously until **`100% GREEN FLAG`** is emitted!
