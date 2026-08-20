# Multi-AI Agent Orchestration & User Interactive Fix Flow

---

## 🔁 Master Interactive Pipeline Lifecycle

```
[Claude pushes new batch XML + Expected JSON to GitHub]
                         │
                         ▼
           ┌───────────────────────────┐
           │   1. ANTIGRAVITY (PM)     │
           │   • Detects & pulls batch │
           │   • Creates Task in .ai/  │
           │   • Assigns to OpenCode   │
           └─────────────┬─────────────┘
                         │
                         ▼
           ┌───────────────────────────┐
           │   2. OPENCODE (Executor)  │
           │   • Ingests 1-by-1 SMS    │
           │   • Runs Parser Engine    │
           │   • Simulates Live Ledger │
           └─────────────┬─────────────┘
                         │
                         ▼
           ┌───────────────────────────┐
           │   3. LIVE DASHBOARD       │
           │   • Updates Live UI       │
           │   • Logs Discrepancies    │
           │   • Flags Bug Tracker     │
           └─────────────┬─────────────┘
                         │
                         ▼
           ┌───────────────────────────┐
           │   4. USER INTERACTION     │
           │   • User inspects diffs   │
           │   • Clicks "⚡ Fix Code"   │
           └─────────────┬─────────────┘
                         │
                         ▼
           ┌───────────────────────────┐
           │   5. OPENCODE AUTO-FIX    │
           │   • Inspects & fixes code │
           │   • Modifies regex/rules  │
           │   • Re-tests that batch   │
           │   • Reaches 100% MATCH    │
           └───────────────────────────┘
```

---

## 📜 Role Breakdown:

1. **Antigravity (PM)**: Directs task creation, verifies acceptance criteria, maintains `.ai/MEMORY.md`.
2. **OpenCode (Tester & Coder)**:
   - Step A: Ingests raw batch and generates verification logs.
   - Step B: Waits for User's explicit confirmation ("Fix Code").
   - Step C: Patches the underlying parser code and re-runs tests on that exact batch.
3. **User**: Has full visual control via the Dashboard to review discrepancies before triggering fixes.
