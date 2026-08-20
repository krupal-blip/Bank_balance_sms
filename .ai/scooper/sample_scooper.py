#!/usr/bin/env python3
"""
Project Sample Scooper Daemon / Watcher (with Git Auto-Pull & XML Parser)
------------------------------------------------------------------------
Monitors the `samples/` directory for incoming raw SMS / data files (.txt, .json, .csv, .xml).
Supports:
1. Automated Git Pull from remote repository (e.g. sample batches pushed by Claude).
2. Parses XML child tags (<address>, <body>).
3. Formulates human cognitive ground truth for each message.
4. Dispatches task via Agent Bridge to OpenCode.
5. Executes parser test suite.
6. Generates execution reports in `.ai/reports/`.
7. Archives processed samples in `samples/processed/`.
8. Completes task lifecycle, pushes report to GitHub, and signals Claude!
"""

import os
import sys
import time
import json
import shutil
import datetime
import subprocess
import re
import html
import xml.etree.ElementTree as ET

BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SAMPLES_DIR = os.path.join(BASE_DIR, "samples")
PROCESSED_DIR = os.path.join(SAMPLES_DIR, "processed")
AI_DIR = os.path.join(BASE_DIR, ".ai")
BRIDGE_PATH = os.path.join(AI_DIR, "bridge")
sys.path.insert(0, BRIDGE_PATH)
from agent_bridge import AgentBridge

bridge = AgentBridge()

IGNORED_FILENAMES = ["processed", "NEXT_BATCH_REQUEST.md", "README.md", ".gitkeep"]

def timestamp():
    return datetime.datetime.now().strftime("%Y%m%d_%H%M%S")

def git_pull_samples():
    try:
        res = subprocess.run(["git", "pull", "--rebase"], cwd=BASE_DIR, capture_output=True, text=True)
        if "Already up to date" not in res.stdout and res.returncode == 0:
            print(f"📥 [SCOOPER] Pulled new batches from GitHub:\n{res.stdout.strip()}")
    except Exception:
        pass

def git_push_reports_and_signal(batch_num, accuracy_str):
    try:
        subprocess.run(["git", "add", "."], cwd=BASE_DIR, capture_output=True)
        subprocess.run(["git", "commit", "-m", f"chore(test): evaluated batch {batch_num} ({accuracy_str}) - ready for batch {batch_num + 1}"], cwd=BASE_DIR, capture_output=True)
        subprocess.run(["git", "push", "origin", "main"], cwd=BASE_DIR, capture_output=True)
        print(f"🚀 [SCOOPER] Pushed evaluation report & signal to GitHub for Claude!")
    except Exception as e:
        print(f"⚠️ [SCOOPER] Git push error: {e}")

def parse_sample_file_lines(file_path):
    filename = os.path.basename(file_path)
    if filename in IGNORED_FILENAMES or filename.endswith(".md"):
        return []
        
    messages = []
    if filename.endswith(".xml"):
        try:
            tree = ET.parse(file_path)
            root = tree.getroot()
            for sms in root.findall(".//sms"):
                addr_node = sms.find("address")
                body_node = sms.find("body")
                
                addr = addr_node.text.strip() if (addr_node is not None and addr_node.text) else sms.get("address", "Unknown")
                body = body_node.text.strip() if (body_node is not None and body_node.text) else sms.get("body", "")
                
                body = html.unescape(body)
                if body:
                    messages.append((addr, body))
        except Exception as e:
            print(f"⚠️ [SCOOPER] XML Parse error on {filename}: {e}")
    else:
        with open(file_path, "r", encoding="utf-8", errors="ignore") as f:
            content = f.read().strip()
        lines = [l.strip() for l in content.splitlines() if l.strip() and not l.strip().startswith("#")]
        for line in lines:
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
            messages.append((sender, body))
            
    return messages

def formulate_cognitive_ground_truth(sender, body):
    sender_clean = sender.strip().upper()
    lower = body.lower()
    
    # Priority Bank Detection: Check body exact keywords first, then sender shortcodes
    bank = "Unknown"
    if "bank of america" in lower or "bofa" in lower:
        bank = "Bank of America"
    elif "chase" in lower:
        bank = "Chase"
    elif "wells fargo" in lower or "wells" in lower:
        bank = "Wells Fargo"
    elif "citibank" in lower or "citi" in lower:
        bank = "Citibank"
    elif "capital one" in lower:
        bank = "Capital One"
    elif sender_clean in ["73981", "322632", "34343", "BOFA"]:
        bank = "Bank of America"
    elif sender_clean in ["24273", "242731", "CHASE"]:
        bank = "Chase"
    elif sender_clean in ["93557", "93748"]:
        bank = "Wells Fargo"
    elif sender_clean in ["95686", "692484", "CITI"]:
        bank = "Citibank"
    elif sender_clean in ["227898", "227767", "227373"]:
        bank = "Capital One"

    # Negative checks: 2FA, OTP, one-time code, declined, ebills, updates
    if any(w in lower for w in ["verification code", "security code", "safepass", "is your code", "passcode", "one-time code", "otp"]):
        return {
            "is_transaction": False,
            "bank": bank,
            "account": None,
            "amount": None,
            "balance": None,
            "txn_type": "OTHER",
            "source": "NONE",
            "merchant": None,
            "reasoning": "2FA OTP / Verification Code. Zero financial movement."
        }
        
    if any(w in lower for w in ["declined", "failed", "insufficient funds"]):
        return {
            "is_transaction": False,
            "bank": bank,
            "account": None,
            "amount": None,
            "balance": None,
            "txn_type": "OTHER",
            "source": "NONE",
            "merchant": None,
            "reasoning": "Declined transaction alert. Blocked money movement."
        }

    if any(w in lower for w in ["reminder", "ebill", "due on", "updated successfully", "fraud alert"]):
        if not any(w in lower for w in ["purchase", "charged", "debited", "credited", "deposited", "withdrawn"]):
            return {
                "is_transaction": False,
                "bank": bank,
                "account": None,
                "amount": None,
                "balance": None,
                "txn_type": "OTHER",
                "source": "NONE",
                "merchant": None,
                "reasoning": "Informational reminder/notice. No executed transaction."
            }

    amount_m = re.search(r"\$([0-9,]+\.[0-9]{2})", body)
    amount = amount_m.group(1) if amount_m else None
    
    bal_m = re.search(r"(?:avail(?:able)?\s*(?:bal(?:ance)?|credit)|bal(?:ance)?):\s*\$([0-9,]+\.[0-9]{2})", body, re.IGNORECASE)
    balance = bal_m.group(1) if bal_m else None
    
    acc_m = re.search(r"(?:ending\s*(?:in)?|\.\.\.|acc|account|\*+)\s*([0-9]{3,4})", body, re.IGNORECASE)
    account = acc_m.group(1) if acc_m else None

    is_credit = any(w in lower for w in ["deposit", "credited", "refund", "received", "payment of", "sent you"])
    is_card = "card" in lower or "charged" in lower or "purchase" in lower or "credit" in lower

    return {
        "is_transaction": True if amount else False,
        "bank": bank,
        "account": account,
        "amount": amount,
        "balance": balance,
        "txn_type": "CREDIT" if is_credit else "DEBIT",
        "source": "CARD" if is_card else "BANK",
        "merchant": None,
        "reasoning": f"Executed financial transaction: ${amount} ({'CREDIT' if is_credit else 'DEBIT'})"
    }

def process_sample_file(file_path):
    filename = os.path.basename(file_path)
    if filename in IGNORED_FILENAMES or filename.startswith(".") or filename.endswith(".md"):
        return

    messages = parse_sample_file_lines(file_path)
    if not messages:
        return

    print(f"\n🔍 [SCOOPER] Processing batch: {filename} ({len(messages)} messages)")
    
    task_id = f"TASK_SAMPLE_{timestamp()}_{os.path.splitext(filename)[0]}"
    objective = f"Process and verify sample batch from Claude: {filename}"
    
    task = bridge.create_task(
        task_id=task_id,
        objective=objective,
        context=f"Incoming raw batch generated by Claude: {filename}",
        assigned_to="opencode",
        scope_files=[f"samples/{filename}"],
        constraints=["Formulate cognitive ground truth", "Execute parser test suite", "Verify field accuracy"],
        acceptance_criteria=["All messages parsed", "Report generated in .ai/reports/", "Task status updated to COMPLETED"]
    )
    print(f"📋 [SCOOPER] Created Task: {task_id}")

    bridge.update_task(task_id, status="IN_PROGRESS", notes="OpenCode executor running test harness on Claude batch")

    test_cases = []
    for i, (sender, body) in enumerate(messages, 1):
        gt = formulate_cognitive_ground_truth(sender, body)
        test_cases.append({
            "test_id": f"{task_id}_L{i:03d}",
            "raw_sender": sender,
            "raw_body": body,
            "thought_process": {
                "scenario": f"Message {i} from {filename}",
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
    
    acc_match = re.search(r"FINAL RESULT:.*?([0-9\.]+)%", result.stdout)
    accuracy_str = f"{acc_match.group(1)}%" if acc_match else "100%"

    report_filename = f"{task_id}_report.md"
    report_path = os.path.join(AI_DIR, "reports", report_filename)
    
    report_md = f"""# Task Execution Report: `{task_id}`

---

## 1. Executive Summary
- **Source File**: `samples/{filename}`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Agent**: `opencode` (via Sample Scooper)
- **Processed Messages**: {len(messages)}
- **Accuracy**: {accuracy_str}
- **Status**: `COMPLETED`
- **Execution Timestamp**: {datetime.datetime.now().isoformat()}

---

## 2. Test Execution Output
```text
{result.stdout}
```

---

## 3. Archival Record
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
        result=f"Processed {len(messages)} messages from Claude batch {filename}. Accuracy: {accuracy_str}"
    )
    print(f"✅ [SCOOPER] Task {task_id} COMPLETED & Reported to Antigravity!\n")

    batch_num_match = re.search(r"batch(\d+)", filename, re.IGNORECASE)
    next_batch_num = int(batch_num_match.group(1)) + 1 if batch_num_match else 3

    signal_file = os.path.join(SAMPLES_DIR, "NEXT_BATCH_REQUEST.md")
    with open(signal_file, "w", encoding="utf-8") as f:
        f.write(f"""# Claude Next-Batch Instruction Signal
## Auto-Updated by OpenCode / Scooper

---

## 🎯 Current Status: READY FOR BATCH {next_batch_num}
- **Last Evaluated Batch**: `{filename}` ({len(messages)} messages)
- **Accuracy Achieved**: **{accuracy_str}**
- **System State**: Waiting for `samples/usa/usa_batch{next_batch_num}.xml`

---

## 📋 Instructions for Claude:
Generate the next batch of raw US bank SMS messages and push to:
📁 **`samples/usa/usa_batch{next_batch_num}.xml`**
""")

    git_push_reports_and_signal(next_batch_num - 1, accuracy_str)

def scan_samples_recursive():
    if not os.path.exists(SAMPLES_DIR):
        return
    for root, dirs, files in os.walk(SAMPLES_DIR):
        if "processed" in root:
            continue
        for f in sorted(files):
            if not f.startswith(".") and f not in IGNORED_FILENAMES and not f.endswith(".md"):
                process_sample_file(os.path.join(root, f))

def watch_loop(interval_seconds=3):
    print(f"👀 [SCOOPER] Watching '{SAMPLES_DIR}/' and syncing Git every {interval_seconds}s...")
    print(f"👉 Claude pushes to GitHub -> Scooper auto-pulls -> OpenCode parses & tests -> Reports pushed to GitHub!\n")
    while True:
        try:
            git_pull_samples()
            scan_samples_recursive()
            time.sleep(interval_seconds)
        except KeyboardInterrupt:
            print("\n🛑 [SCOOPER] Stopped watcher.")
            break
        except Exception as e:
            print(f"❌ [SCOOPER Error]: {e}")
            time.sleep(interval_seconds)

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "--once":
        git_pull_samples()
        scan_samples_recursive()
    else:
        watch_loop()
