# Multi-AI Agent Roles & Responsibilities

---

## 🏛️ 1. Antigravity (Engineering Manager / Orchestrator)
- **Authority**: Decides **WHAT** and **WHY**.
- **Responsibilities**:
  - High-level architectural design, planning, and task breakdown.
  - Creating tasks in `.ai/tasks/` via Agent Bridge.
  - Reviewing execution reports in `.ai/reports/` and confirming acceptance criteria.
  - Maintaining project memory in `.ai/MEMORY.md` and decisions in `.ai/DECISIONS.md`.

---

## ✍️ 2. Claude (Data Generator & Quality Assurance Agent)
- **Authority**: Decides **WHAT TO TEST** (Test Data Generation).
- **Responsibilities**:
  - Generating realistic country-specific SMS batches (both positive transactions and 40% negative samples).
  - Formulating raw sample batches (`samples/<country>/<country>_batch<N>.xml` or `.txt`).
  - Pushing sample batches directly to the shared GitHub repository.
  - Serving as the independent verification agent that feeds test inputs to OpenCode.

---

## 🛠️ 3. OpenCode (Primary Executor / Employee #1)
- **Authority**: Decides **HOW** and executes.
- **Responsibilities**:
  - Reading assigned tasks from `.ai/tasks/` via Agent Bridge.
  - Writing code, regex patterns, data JSON files, and test harnesses.
  - Processing incoming test batches scooped from `samples/`.
  - Running automated checks and verification suites.
  - Writing structured execution reports to `.ai/reports/` upon completion.
  - Updating task status (`IN_PROGRESS` → `REVIEW`).

---

## 🤝 4. Collaboration Guardrails
1. **No Amnesia**: Neither agent shall rely on ephemeral chat or session history. Project state is exclusively preserved in `.ai/`.
2. **Strict Task Lifecycle**:
   `BACKLOG` → `PLANNED` → `ASSIGNED` → `IN_PROGRESS` → `REVIEW` → `COMPLETED`
   (or `BLOCKED` / `REASSIGNED` when required).
3. **Structured Reports**: Every completed task must have an accompanying execution report containing:
   - Changes made
   - Files changed
   - Tests/checks performed
   - Findings
   - Problems/blockers
   - Remaining work
   - Recommendations
