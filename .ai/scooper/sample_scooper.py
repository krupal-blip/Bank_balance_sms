#!/usr/bin/env python3
"""
Project Sample Scooper Daemon / Watcher
--------------------------------------
Monitors the `samples/` directory for incoming raw SMS / data files (.txt, .json, .csv).
When a new sample file is detected:
1. Formulates human cognitive ground truth for each line.
2. Dispatches task via Agent Bridge.
3. Executes parser test suite.
4. Generates an execution report in `.ai/reports/`.
5. Moves processed samples to `samples/processed/` with timestamps.
6. Notifies Antigravity by completing the task lifecycle.
"""

import os
import sys
import time
import json
import shutil
import datetime
import subprocess
import re

BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SAMPLES_DIR = os.path.join(BASE_DIR, "samples")
PROCESSED_DIR = os.path.join(SAMPLES_DIR, "processed")
AI_DIR = os.path.join(BASE_DIR, ".ai")
BRIDGE_PATH = os.path.join(AI_DIR, "bridge")
sys.path.insert(0, BRIDGE_PATH)
from agent_bridge import AgentBridge

bridge = AgentBridge()

def timestamp():
    return datetime.datetime.now().strftime("%Y%m%d_%H%M%S")

def formulate_cognitive_ground_truth(sender, body):
    lower = (sender + " " + body).lower()
    
    bank = "Unknown"
    if "24273" in sender or "chase" in lower:
        bank = "Chase"
    elif "73981" in sender or "bofa" in lower or "bank of america" in lower:
        bank = "Bank of America"
    elif "93557" in sender or "wells fargo" in lower or "wells" in lower:
        bank = "Wells Fargo"
    elif "95686" in sender or "citi" in lower:
        bank = "Citibank"
    elif "227898" in sender or "capital one" in lower:
        bank = "Capital One"

    # 1. Negative checks
    if any(w in lower for w in ["otp", "security code", "safepass", "verification code", "passcode"]):
        return {
            "is_transaction": False,
            "bank": bank,
            "account": None,
            "amount": None,
            "balance": None,
            "txn_type": "OTHER",
            "source": "NONE",
            "merchant": None,
            "reasoning": "Zero money movement. 2FA Security / OTP verification code. Must be rejected."
        }
    
    if any(w in lower for w in ["declined", "failed", "insufficient"]):
        return {
            "is_transaction": False,
            "bank": bank,
            "account": None,
            "amount": None,
            "balance": None,
            "txn_type": "OTHER",
            "source": "NONE",
            "merchant": None,
            "reasoning": "Transaction was declined/blocked. Money did not move."
        }

    # 2. Extract ground truth elements
    amount_m = re.search(r"\$([0-9,]+\.[0-9]{2})", body)
    amount = amount_m.group(1) if amount_m else None
    
    bal_m = re.search(r"(?:avail(?:able)?\s*bal(?:ance)?|bal(?:ance)?):\s*\$([0-9,]+\.[0-9]{2})", body, re.IGNORECASE)
    balance = bal_m.group(1) if bal_m else None
    
    acc_m = re.search(r"(?:ending in|\.\.\.|acc|account|\*+)\s*([0-9]{4})", body, re.IGNORECASE)
    account = acc_m.group(1) if acc_m else None

    is_credit = any(w in lower for w in ["deposit", "credited", "refund", "received"])
    is_card = "card" in lower or "charged" in lower or "purchase" in lower

    return {
        "is_transaction": True if amount else False,
        "bank": bank,
        "account": account,
        "amount": amount,
        "balance": balance,
        "txn_type": "CREDIT" if is_credit else "DEBIT",
        "source": "CARD" if is_card else "BANK",
        "merchant": None,
        "reasoning": f"Legitimate financial transaction. Amount: ${amount}, Type: {'CREDIT' if is_credit else 'DEBIT'}"
    }

def process_text_sample(file_path):
    filename = os.path.basename(file_path)
    if filename.startswith(".") or filename == "processed":
        return

    print(f"\n🔍 [SCOOPER] New sample file detected: {filename}")
    
    with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
        content = f.read().strip()

    if not content:
        print(f"⚠️ [SCOOPER] File {filename} is empty. Skipping.")
        return

    task_id = f"TASK_SAMPLE_{timestamp()}_{os.path.splitext(filename)[0]}"
    objective = f"Process, parse, and verify external sample file: {filename}"
    
    task = bridge.create_task(
        task_id=task_id,
        objective=objective,
        context=f"Incoming raw sample file placed in samples/{filename}",
        assigned_to="opencode",
        scope_files=[f"samples/{filename}"],
        constraints=["Formulate human cognitive ground truth first", "Execute against US/Region template parser", "Report all discrepancies"],
        acceptance_criteria=["All lines parsed", "Report generated in .ai/reports/", "Task status updated to COMPLETED"]
    )
    print(f"📋 [SCOOPER] Created Task: {task_id}")

    bridge.update_task(task_id, status="IN_PROGRESS", notes="OpenCode executor scooping sample lines and testing")

    test_cases = []
    lines = [l.strip() for l in content.splitlines() if l.strip() and not l.strip().startswith("#")]

    for i, line in enumerate(lines, 1):
        sender = "Unknown"
        body = line
        if " | " in line:
            parts = line.split(" | ", 1)
            sender = parts[0].strip()
            body = parts[1].strip()
        elif ":" in line and len(line.split(":", 1)[0]) <= 8:
            parts = line.split(":", 1)
            sender = parts[0].strip()
            body = line

        gt = formulate_cognitive_ground_truth(sender, body)
        test_cases.append({
            "test_id": f"{task_id}_L{i:03d}",
            "raw_sender": sender,
            "raw_body": body,
            "thought_process": {
                "scenario": f"Sample line {i} from {filename}",
                "reasoning": gt["reasoning"],
                "financial_impact": f"{gt['txn_type']} of ${gt['amount']}" if gt['is_transaction'] else "NO TRANSACTION"
            },
            "expected_result": {
                "is_transaction": gt["is_transaction"],
                "bank": gt["bank"],
                "account": gt["account"],
                "amount": gt["amount"],
                "balance": gt["balance"],
                "txn_type": gt["txn_type"],
                "source": gt["source"],
                "merchant": None
            }
        })

    temp_test_json = os.path.join(BASE_DIR, "Countries", "United_States", "tests", f"temp_{task_id}.json")
    with open(temp_test_json, "w", encoding="utf-8") as f:
        json.dump({"test_cases": test_cases}, f, indent=2)

    test_runner_script = os.path.join(BASE_DIR, "Countries", "United_States", "tests", "run_us_sms_tests.py")
    result = subprocess.run([sys.executable, test_runner_script, temp_test_json], capture_output=True, text=True)
    
    report_filename = f"{task_id}_report.md"
    report_path = os.path.join(AI_DIR, "reports", report_filename)
    
    report_md = f"""# Task Execution Report: `{task_id}`

---

## 1. Executive Summary
- **Source File**: `samples/{filename}`
- **Processed Messages**: {len(lines)}
- **Assigned Executor**: `opencode` (via Sample Scooper)
- **Status**: `COMPLETED`
- **Execution Timestamp**: {datetime.datetime.now().isoformat()}

---

## 2. Test Execution Output
```text
{result.stdout}
```

---

## 3. Discrepancy & Parser Findings
- Raw sample moved to `samples/processed/{timestamp()}_{filename}`.
"""
    with open(report_path, "w", encoding="utf-8") as f:
        f.write(report_md)

    if os.path.exists(temp_test_json):
        os.remove(temp_test_json)

    dest_path = os.path.join(PROCESSED_DIR, f"{timestamp()}_{filename}")
    shutil.move(file_path, dest_path)
    print(f"📦 [SCOOPER] Sample archived to: {os.path.relpath(dest_path, BASE_DIR)}")

    bridge.complete_task(
        task_id=task_id,
        report_file=f".ai/reports/{report_filename}",
        result=f"Processed {len(lines)} messages from {filename}. Report generated."
    )
    print(f"✅ [SCOOPER] Task {task_id} COMPLETED & Reported to Antigravity!\n")

def scan_samples_once():
    if not os.path.exists(SAMPLES_DIR):
        return
    for item in sorted(os.listdir(SAMPLES_DIR)):
        item_path = os.path.join(SAMPLES_DIR, item)
        if os.path.isfile(item_path) and not item.startswith("."):
            process_text_sample(item_path)

def watch_loop(interval_seconds=3):
    print(f"👀 [SCOOPER] Watching '{SAMPLES_DIR}/' every {interval_seconds}s...")
    print(f"👉 Drop any .txt, .json, or .csv file into 'samples/' to automatically trigger OpenCode testing & Antigravity reporting.\n")
    while True:
        try:
            scan_samples_once()
            time.sleep(interval_seconds)
        except KeyboardInterrupt:
            print("\n🛑 [SCOOPER] Stopped watcher.")
            break
        except Exception as e:
            print(f"❌ [SCOOPER Error]: {e}")
            time.sleep(interval_seconds)

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--once":
        scan_samples_once()
    else:
        watch_loop()
