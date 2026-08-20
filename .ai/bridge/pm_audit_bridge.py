#!/usr/bin/env python3
"""
Antigravity PM Quality Gate & Claude CLI Trigger MCP Tools
----------------------------------------------------------
Exposes:
1. audit_batch_yield(batch_id):
   - Compares Claude's Expected Table vs OpenCode's Parsed Table on the Dashboard.
   - Returns exact match status ($0.00 diff vs mismatch).
   - If mismatch -> Formulates root cause and reassigns task to OpenCode.
2. trigger_claude_next_batch(batch_num, instructions):
   - If audit passes 100% -> Invokes local Claude CLI non-interactively
     to generate the next batch and push to GitHub!
"""

import sys
import os
import json
import subprocess

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
AUTOMATION_DIR = os.path.join(BASE_DIR, "automation")
AI_DIR = os.path.join(BASE_DIR, ".ai")
BRIDGE_PATH = os.path.join(AI_DIR, "bridge")

sys.path.insert(0, BRIDGE_PATH)
from agent_bridge import AgentBridge

bridge = AgentBridge()

def pm_audit_yield(batch_id=None):
    """
    Antigravity PM audits the latest batch between Claude and OpenCode.
    """
    state_file = os.path.join(AUTOMATION_DIR, "dashboard", "live_state.json")
    if not os.path.exists(state_file):
        return {"status": "NO_ACTIVE_BATCH", "message": "No active batch in live_state.json"}

    with open(state_file, "r", encoding="utf-8") as f:
        state = json.load(f)

    dual_table = state.get("dual_table", [])
    discrepancies = state.get("discrepancies", [])
    all_match = state.get("all_match", False)

    audit_result = {
        "batch_id": state.get("task_id", "UNKNOWN"),
        "total_accounts_checked": len(dual_table),
        "all_match": all_match,
        "dual_table_comparison": dual_table,
        "discrepancies": discrepancies
    }

    if not all_match:
        # Reassign to OpenCode via Agent Bridge
        task_id = state.get("task_id", "TASK_AUDIT_MISMATCH")
        bridge.update_task(
            task_id=task_id,
            status="REASSIGNED",
            notes=f"Antigravity PM Audit: {len(discrepancies)} discrepancies found. Reassigned to OpenCode to patch parser/ledger rules."
        )
        audit_result["pm_action"] = "REASSIGNED_TO_OPENCODE"
        audit_result["pm_verdict"] = "❌ REJECTED: Mismatch detected. OpenCode must fix."
    else:
        task_id = state.get("task_id", "TASK_AUDIT_PASS")
        bridge.update_task(
            task_id=task_id,
            status="COMPLETED",
            notes="Antigravity PM Audit: 100% MATCH across all accounts. Batch approved."
        )
        audit_result["pm_action"] = "APPROVED"
        audit_result["pm_verdict"] = "✅ APPROVED: 100% Match. Ready to trigger Claude for next batch."

    return audit_result

def trigger_claude_cli_next_batch(batch_num, instructions=None):
    """
    Invokes Claude Code CLI (`claude -p "..."`) directly to generate and push the next batch.
    """
    prompt = f"Generate USA Batch {batch_num} (samples/usa/usa_batch{batch_num}.xml and samples/usa/usa_batch{batch_num}_expected.json) adhering strictly to CLAUDE.md. Maintain running balances from Batch {batch_num-1}. Then commit and push to main."
    if instructions:
        prompt += f" Specific instructions: {instructions}"

    print(f"🚀 [PM TRIGGER] Launching Claude Code CLI for Batch {batch_num}...")
    
    try:
        # Run non-interactive claude print command
        res = subprocess.run(["claude", "-p", prompt], cwd=BASE_DIR, capture_output=True, text=True, timeout=120)
        return {
            "status": "SUCCESS",
            "batch_num": batch_num,
            "claude_output": res.stdout.strip(),
            "message": f"Claude Code CLI invoked for Batch {batch_num}."
        }
    except Exception as e:
        return {
            "status": "ERROR",
            "error": str(e),
            "fallback": "Please run in Claude terminal: 'Generate USA Batch " + str(batch_num) + " and push to main'"
        }

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "audit":
        print(json.dumps(pm_audit_yield(), indent=2))
    elif len(sys.argv) > 1 and sys.argv[1] == "trigger_claude":
        b_num = int(sys.argv[2]) if len(sys.argv) > 2 else 6
        print(json.dumps(trigger_claude_cli_next_batch(b_num), indent=2))
    else:
        print("Usage: python3 pm_audit_bridge.py [audit | trigger_claude <batch_num>]")
