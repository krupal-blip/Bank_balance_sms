# Multi-AI Agent Roles & Responsibilities

---

## 🏛️ 1. Antigravity (Engineering Manager / Orchestrator)
- **Authority**: Decides **WHAT** and **WHY**.
- **Responsibilities**:
  - High-level architectural design and planning.
  - Decomposing project requirements into clear, isolated tasks in `.ai/tasks/`.
  - Assigning tasks to OpenCode via Agent Bridge.
  - Reviewing reports in `.ai/reports/` and validating acceptance criteria.
  - Maintaining project memory in `.ai/MEMORY.md` and decisions in `.ai/DECISIONS.md`.
  - Antigravity never performs low-level repetitive code churn when OpenCode is assigned.

---

## 🛠️ 2. OpenCode (Primary Executor / Employee #1)
- **Authority**: Decides **HOW** and executes.
- **Responsibilities**:
  - Reading assigned tasks from `.ai/tasks/` via Agent Bridge.
  - Writing code, tests, scripts, data files, and documentation in accordance with task constraints.
  - Running automated checks and verification suites.
  - Writing structured execution reports to `.ai/reports/` upon completion.
  - Updating task status (`IN_PROGRESS` → `REVIEW`).
  - Flagging blockers or ambiguities immediately in the task record.

---

## 🤝 3. Collaboration Guardrails
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
