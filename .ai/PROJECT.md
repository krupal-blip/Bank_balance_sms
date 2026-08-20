# Project Overview — Bank Balance Global Expansion

## Mission
Expand **Bank Balance Checker** from an India-centric SMS banking application into a multi-country, privacy-first, high-eCPM and high-PPP personal finance platform without requiring user bank login credentials.

## Architecture
- **Templates**: Base models, regex patterns, schemas, and pipelines (`Templates/sms_trans_tracker_handout/` and `Templates/app_region_templates/`).
- **Countries**: Country-specific data modules (`Countries/United_States/`, etc.).
- **Engine**: Zero-login dual-engine (Opt-in SMS + Android `NotificationListenerService`).
- **Orchestration**: Multi-AI Agent Framework (Antigravity ↔ Agent Bridge ↔ OpenCode).

## Repository Source of Truth
- Repository Root: `/Volumes/Extra/backup/R&D/Bank_balance`
- Orchestration & State: `.ai/`
- Git is the absolute source of truth for code and version control.
