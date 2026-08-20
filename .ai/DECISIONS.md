# Architectural Decisions Log (ADR)

---

## ADR-001: Pure JSON Assets Behind One Single Seam
- **Date**: 2026-08-19
- **Decision**: All regional data (holidays, schemes, banking channels, net banking URLs, routing directories) must be shipped as static JSON assets loaded at runtime via `RegionFeatureProfile` (`_shared/UsFeatureProfile.kt`).
- **Rationale**: Replaces India's legacy anti-pattern where schemes, bank arrays (15 KB), and holiday loops were hardcoded in Kotlin Activities. Adding a new country now requires 0 lines of new Kotlin code.

## ADR-002: Dual-Engine Zero-Login Ingestion
- **Date**: 2026-08-19
- **Decision**: Ingest transactions via `SmsReceiver` for SMS shortcodes + `NotificationListenerService` for push alerts.
- **Rationale**: Bypasses the need for Open Banking APIs / Plaid logins, delivering a 100% on-device, privacy-first user promise.

## ADR-003: Multi-AI Agent Orchestration Framework
- **Date**: 2026-08-20
- **Decision**: Establish Antigravity as Engineering Manager/Orchestrator and OpenCode as Executor #1. State is managed via persistent files under `.ai/` and coordinated via the Agent Bridge CLI/MCP.
- **Rationale**: Enables asynchronous, multi-agent pair programming without session loss, context window pollution, or memory amnesia.
