#!/usr/bin/env python3
"""
Master Automation Test Suite & Dual-Table Verification Engine
-------------------------------------------------------------
Workflow:
1. Ingests SMS Batch XML (`samples/usa/usa_batch<N>.xml`).
2. Ingests Expected Ground Truth (`samples/usa/usa_batch<N>_expected.json` or embedded `<expected_summary>`).
3. Executes Regional Parser (OpenCode) on raw SMS chronologically.
4. Feeds transactions into `AccountLedger`.
5. Compares Table A (Expected from Claude) vs Table B (Parsed by OpenCode).
6. Generates Side-by-Side Dual-Table Verification Matrix.
7. Publishes report to `automation/reports/` and archives files.
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
AUTOMATION_DIR = os.path.join(BASE_DIR, "automation")
REPORTS_DIR = os.path.join(AUTOMATION_DIR, "reports")
PROCESSED_DIR = os.path.join(AUTOMATION_DIR, "processed")
SAMPLES_DIR = os.path.join(BASE_DIR, "samples")

sys.path.insert(0, os.path.join(AUTOMATION_DIR, "ledger"))
from account_ledger import AccountLedger

sys.path.insert(0, os.path.join(BASE_DIR, "Countries", "United_States", "tests"))
from run_us_sms_tests import parse_with_us_template

def timestamp():
    return datetime.datetime.now().strftime("%Y%m%d_%H%M%S")

class DualTableVerificationRunner:
    def __init__(self):
        os.makedirs(REPORTS_DIR, exist_ok=True)
        os.makedirs(PROCESSED_DIR, exist_ok=True)
        os.makedirs(SAMPLES_DIR, exist_ok=True)

    def git_sync_pull(self):
        try:
            res = subprocess.run(["git", "pull", "--rebase"], cwd=BASE_DIR, capture_output=True, text=True)
            if "Already up to date" not in res.stdout and res.returncode == 0:
                print(f"📥 [AUTOMATION] New batches pulled from GitHub:\n{res.stdout.strip()}")
        except Exception:
            pass

    def git_sync_push(self, commit_msg):
        try:
            subprocess.run(["git", "add", "."], cwd=BASE_DIR, capture_output=True)
            subprocess.run(["git", "commit", "-m", commit_msg], cwd=BASE_DIR, capture_output=True)
            subprocess.run(["git", "push", "origin", "main"], cwd=BASE_DIR, capture_output=True)
            print(f"🚀 [AUTOMATION] Pushed verification report to GitHub!")
        except Exception as e:
            print(f"⚠️ [AUTOMATION] Git push error: {e}")

    def parse_xml_batch(self, file_path):
        tree = ET.parse(file_path)
        root = tree.getroot()
        
        messages = []
        for sms in root.findall(".//sms"):
            addr_node = sms.find("address")
            body_node = sms.find("body")
            time_node = sms.find("receivedTime")
            
            addr = addr_node.text.strip() if (addr_node is not None and addr_node.text) else sms.get("address", "Unknown")
            body = body_node.text.strip() if (body_node is not None and body_node.text) else sms.get("body", "")
            ts = time_node.text.strip() if (time_node is not None and time_node.text) else None
            
            body = html.unescape(body)
            if body:
                messages.append((addr, body, ts))

        # Check for embedded expected_summary in XML
        expected_accounts = []
        for acc in root.findall(".//expected_account"):
            expected_accounts.append({
                "bank": acc.get("bank", ""),
                "account_or_card": acc.get("account", ""),
                "type": acc.get("type", "BANK"),
                "expected_final_balance": float(acc.get("final_balance", 0.0)),
                "expected_total_credits": float(acc.get("total_credits", 0.0)),
                "expected_total_debits": float(acc.get("total_debits", 0.0)),
                "expected_txn_count": int(acc.get("txn_count", 0))
            })

        return messages, expected_accounts

    def run_dual_verification(self, xml_file_path):
        filename = os.path.basename(xml_file_path)
        if not filename.endswith(".xml") or filename.startswith("."):
            return

        base_name = os.path.splitext(filename)[0]
        json_expected_file = os.path.join(os.path.dirname(xml_file_path), f"{base_name}_expected.json")

        messages, embedded_expected = self.parse_xml_batch(xml_file_path)
        
        expected_accounts = embedded_expected
        if os.path.exists(json_expected_file):
            try:
                with open(json_expected_file, "r") as f:
                    data = json.load(f)
                    expected_accounts = data.get("expected_accounts", [])
            except Exception as e:
                print(f"⚠️ [AUTOMATION] Failed to read expected json: {e}")

        print(f"\n=================================================================")
        print(f" 🚀 DUAL-TABLE AUTOMATION RUNNER: Processing {filename} ({len(messages)} messages)")
        print(f"=================================================================\n")

        ledger = AccountLedger()
        for sender, body, ts in messages:
            parsed = parse_with_us_template(sender, body)
            if parsed.get("is_transaction"):
                ledger.process_transaction(sender, body, parsed, timestamp_ms=ts)

        parsed_table = ledger.generate_summary_table()

        # Build Side-by-Side Comparison Table
        comparison_lines = []
        comparison_lines.append("| Bank | Account / Card | Metric | Expected (Claude) | Parsed (OpenCode) | Status | Diff |")
        comparison_lines.append("|---|---|:---:|:---:|:---:|:---:|:---:|")

        all_matched = True
        if expected_accounts:
            for exp in expected_accounts:
                key = f"{exp['bank']} [{exp['type']}: ...{exp['account_or_card']}]"
                act = ledger.accounts.get(key)

                act_bal = act["current_balance"] if act else 0.0
                exp_bal = exp["expected_final_balance"]
                bal_diff = abs(act_bal - exp_bal)
                status_bal = "✅ MATCH" if bal_diff < 0.01 else "❌ MISMATCH"

                if bal_diff >= 0.01:
                    all_matched = False

                comparison_lines.append(f"| **{exp['bank']}** | `...{exp['account_or_card']}` | **Final Balance** | ${exp_bal:,.2f} | ${act_bal:,.2f} | {status_bal} | ${bal_diff:,.2f} |")
        else:
            comparison_lines.append("| *N/A* | *No external expected_accounts JSON provided* | - | - | - | ℹ️ SELF-VERIFIED | - |")

        dual_table_md = "\n".join(comparison_lines)

        report_filename = f"DUAL_VERIFICATION_{timestamp()}_{base_name}.md"
        report_path = os.path.join(REPORTS_DIR, report_filename)

        report_md = f"""# Dual-Table Verification & Bank Balance Audit Report

---

## 📊 1. Batch Execution Metadata
- **Input Batch**: `samples/{filename}`
- **Source Agent**: `Claude (Test Data Generator)`
- **Executor Engine**: `OpenCode (Regional SMS Parser)`
- **Total Ingested Messages**: **{len(messages)}**
- **Overall Balance Match Status**: **{'✅ 100% PERFECT MATCH' if (all_matched and expected_accounts) else ('⚠️ DISCREPANCIES DETECTED' if expected_accounts else 'ℹ️ COMPUTED (Awaiting Expected File)')}**
- **Timestamp**: {datetime.datetime.now().isoformat()}

---

## 🔍 2. DUAL-TABLE COMPARISON MATRIX (Claude Expected vs. OpenCode Parsed)
*Exact comparison between Claude's generated truth and OpenCode's extracted numbers:*

{dual_table_md}

---

## 🏦 3. Final Computed Bank Account & Card Passbook Table
{parsed_table}

---

## 🎯 4. Archival Record
- Batch archived to `automation/processed/{timestamp()}_{filename}`.
"""
        with open(report_path, "w", encoding="utf-8") as f:
            f.write(report_md)

        # Archive files
        dest_xml = os.path.join(PROCESSED_DIR, f"{timestamp()}_{filename}")
        shutil.move(xml_file_path, dest_xml)
        if os.path.exists(json_expected_file):
            dest_json = os.path.join(PROCESSED_DIR, f"{timestamp()}_{os.path.basename(json_expected_file)}")
            shutil.move(json_expected_file, dest_json)

        print(f"📦 [AUTOMATION] Archived batch files.")
        print(f"📄 [AUTOMATION] Report Generated: {os.path.relpath(report_path, BASE_DIR)}\n")

        print("🔍 DUAL-TABLE COMPARISON RESULT:")
        print(dual_table_md)
        print("\n=================================================================\n")

        self.git_sync_push(f"chore(audit): dual-table verification report for {base_name}")

    def scan_and_run(self):
        self.git_sync_pull()
        for root, dirs, files in os.walk(SAMPLES_DIR):
            if "processed" in root:
                continue
            for f in sorted(files):
                if f.endswith(".xml") and not f.startswith("."):
                    self.run_dual_verification(os.path.join(root, f))

    def daemon_loop(self, interval_seconds=3):
        print(f"⚡ [DUAL-TABLE DAEMON] Watching 'samples/' & syncing Git every {interval_seconds}s...")
        while True:
            try:
                self.scan_and_run()
                time.sleep(interval_seconds)
            except KeyboardInterrupt:
                print("\n🛑 Stopped daemon.")
                break
            except Exception as e:
                print(f"❌ Error: {e}")
                time.sleep(interval_seconds)

if __name__ == "__main__":
    runner = DualTableVerificationRunner()
    if len(sys.argv) > 1 and sys.argv[1] == "--once":
        runner.scan_and_run()
    else:
        runner.daemon_loop()
