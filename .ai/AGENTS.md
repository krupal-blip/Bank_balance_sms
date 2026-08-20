# Multi-AI Agent Roles & Responsibilities

---

## 🏛️ 1. Antigravity (Engineering Manager / Lead Product Manager)
- **Authority**: Decides **WHAT**, **WHY**, and **APPROVES / REASSIGNS RESULTS**.
- **Responsibilities**:
  - High-level architectural design, planning, and task breakdown.
  - Creating tasks in `.ai/tasks/` via Agent Bridge.
  - **Yield & Quality Gate Audit**: Inspects OpenCode's test yield against Claude's Ground Truth table.
  - **Reassignment / Rejection Logic**: If testing yield has discrepancies, Antigravity formulates the root-cause diagnosis and reassigns the task back to OpenCode with strict correction constraints.
  - **Acceptance Sign-Off**: Once a 100% Dual-Table match is achieved, Antigravity signs off, marks the task `COMPLETED`, and updates project memory in `.ai/MEMORY.md`.

---

## ✍️ 2. Claude (Data Generator & Quality Assurance Agent)
- **Authority**: Decides **WHAT TO TEST** (Test Data Generation).
- **Responsibilities**:
  - Generating realistic country-specific SMS batches (positive transactions + 40% negative samples).
  - Formulating the Expected Ground Truth Table (`samples/<country>/<country>_batch<N>_expected.json`).
  - Pushing sample batches directly to the shared GitHub repository.

---

## 🛠️ 3. OpenCode (Primary Executor / Employee #1)
- **Authority**: Decides **HOW** and executes.
- **Responsibilities**:
  - Reading assigned tasks from `.ai/tasks/` via Agent Bridge.
  - Ingesting SMS batches 1-by-1, parsing fields, and simulating live account ledgers.
  - Generating the actual parsed results table and streaming to the Live Dashboard.
  - Receiving fix assignments from Antigravity / User, patching parser regexes & ledger formulas, and re-testing that exact batch until 100% verified.
  - Updating task status (`IN_PROGRESS` → `REVIEW`).

---

## 🤝 4. Interactive Collaboration Guardrails
1. **Strict PM Review Gate**: No task is closed automatically if there is a discrepancy. Antigravity audits the diff, diagnoses the cause, and either reassigns with instructions or prompts for user confirmation.
2. **Dual-Table Verification**: Claude's Expected Table vs. OpenCode's Parsed Table must reach a verified `$0.00` diff before batch sign-off.
3. **No Amnesia**: Every yield audit, bug diagnosis, and code patch is persistently logged in `.ai/reports/` and `.ai/MEMORY.md`.
