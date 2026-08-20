#!/usr/bin/env python3
"""
US SMS Transaction Parser & Reasoning Test Engine
------------------------------------------------
Architecture based on:
1. sms_trans_tracker/template/src/pipeline/TransactionFilter.kt
2. US_DATA/sms_parser/UsProfile.kt
3. US_DATA/sms_parser/us_bank_sms_formats.json

Dual-Validation Strategy:
- Step 1: LLM/Human Cognitive Ground Truth (What the message ACTUALLY means in reality)
- Step 2: US Template & Pipeline Parser Output (What the algorithmic parser extracts)
- Step 3: Automated Discrepancy & Verification Analysis (Exact field-by-field match)
"""

import json
import re
import os
import sys

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
US_DATA_DIR = os.path.dirname(BASE_DIR)
FORMATS_PATH = os.path.join(US_DATA_DIR, "sms_parser", "us_bank_sms_formats.json")

with open(FORMATS_PATH, "r") as f:
    US_FORMATS = json.load(f)

# Comprehensive US Bank Shortcodes Map (including 322632 for BofA, 24273 for Chase, etc.)
SHORTCODE_MAP = {
    "24273": "Chase",
    "242731": "Chase",
    "CHASE": "Chase",
    "73981": "Bank of America",
    "322632": "Bank of America",
    "34343": "Bank of America",
    "BOFA": "Bank of America",
    "93557": "Wells Fargo",
    "93748": "Wells Fargo",
    "95686": "Citibank",
    "692484": "Citibank",
    "CITI": "Citibank",
    "227898": "Capital One",
    "227767": "Capital One",
    "227373": "Capital One",
    "872265": "U.S. Bank",
    "762265": "PNC Bank",
    "90742": "PNC Bank",
    "878478": "Truist Bank",
    "832265": "TD Bank",
    "266226": "BMO Bank"
}

OTP_WORDS = ["otp", "verification code", "security code", "passcode", "safepass", "one-time code", "temp code", "is your code"]
DECLINED_WORDS = ["declined", "payment failed", "insufficient funds", "card blocked", "unauthorized", "reversed"]
REMINDER_WORDS = ["payment due", "minimum payment due", "low balance alert", "statement ready", "bill reminder", "below your", "ebill reminder", "due on", "updated successfully"]
MANDATE_CREATION_WORDS = ["scheduled", "set up", "enrolled", "activated", "created", "will be debited", "is scheduled"]

def resolve_sender(sender, body=""):
    if sender:
        cleaned = sender.strip().upper().replace("-", "").replace(" ", "")
        if cleaned in SHORTCODE_MAP:
            return SHORTCODE_MAP[cleaned]
    
    # Body-level fallback lookup
    lower_body = body.lower()
    if "chase" in lower_body:
        return "Chase"
    if "bank of america" in lower_body or "bofa" in lower_body:
        return "Bank of America"
    if "wells fargo" in lower_body or "wells" in lower_body:
        return "Wells Fargo"
    if "citibank" in lower_body or "citi" in lower_body:
        return "Citibank"
    if "capital one" in lower_body:
        return "Capital One"
    if "u.s. bank" in lower_body or "us bank" in lower_body:
        return "U.S. Bank"
    if "pnc" in lower_body:
        return "PNC Bank"
    return "Unknown"

def is_negative_notice(body):
    lower_body = body.lower()
    for word in OTP_WORDS:
        if word in lower_body:
            return True, "OTP_VERIFICATION_CODE"
    for word in DECLINED_WORDS:
        if word in lower_body:
            return True, "DECLINED_TRANSACTION"
    for word in MANDATE_CREATION_WORDS:
        if "autopay" in lower_body and word in lower_body:
            return True, "MANDATE_CREATION_NOTICE"
    for word in REMINDER_WORDS:
        if word in lower_body and not any(w in lower_body for w in ["purchase", "charged", "debited", "credited", "deposited", "withdrawn", "payment of", "used for"]):
            return True, "REMINDER_NOTICE"
    return False, None

def convert_regex_to_python(pattern_str):
    return re.sub(r"\(\?<([a-zA-Z0-9_]+)>", r"(?P<\1>", pattern_str)

def parse_with_us_template(sender, body):
    bank_name = resolve_sender(sender, body)
    
    is_neg, neg_reason = is_negative_notice(body)
    if is_neg:
        return {
            "is_transaction": False,
            "negative_reason": neg_reason,
            "bank": bank_name,
            "account": None,
            "amount": None,
            "balance": None,
            "txn_type": "OTHER",
            "source": "NONE",
            "merchant": None
        }

    matched_data = None
    for b in US_FORMATS["banks"]:
        if b["short_name"].lower() == bank_name.lower() or bank_name == "Unknown":
            for tmpl in b["sms_templates"]:
                py_regex = convert_regex_to_python(tmpl["regex"])
                pattern = re.compile(py_regex, re.IGNORECASE)
                match = pattern.search(body)
                if match:
                    groups = match.groupdict()
                    is_card = "card" in tmpl.get("type", "").lower() or "card" in body.lower()
                    matched_data = {
                        "is_transaction": True,
                        "negative_reason": None,
                        "bank": b["short_name"],
                        "account": groups.get("account_suffix", None),
                        "amount": groups.get("amount", None),
                        "balance": groups.get("balance", None),
                        "txn_type": tmpl.get("transaction_type", "OTHER"),
                        "source": "CARD" if is_card else "BANK",
                        "merchant": groups.get("merchant", None)
                    }
                    break
            if matched_data:
                break

    if not matched_data:
        amount_match = re.search(r"\$([0-9,]+\.[0-9]{2})", body)
        acc_match = re.search(r"(?:ending\s*(?:in)?|\.\.\.|acc|account|\*+)\s*([0-9]{3,4})", body, re.IGNORECASE)
        bal_match = re.search(r"(?:avail(?:able)?\s*(?:bal(?:ance)?|credit)|bal(?:ance)?):\s*\$([0-9,]+\.[0-9]{2})", body, re.IGNORECASE)
        
        is_credit = any(w in body.lower() for w in ["deposit", "credited", "refund", "received", "payment of"])
        is_card = any(w in body.lower() for w in ["card", "charged", "authorized", "used for", "purchase"])
        
        has_txn_intent = amount_match is not None and not is_neg
        
        matched_data = {
            "is_transaction": True if has_txn_intent else False,
            "negative_reason": None,
            "bank": bank_name,
            "account": acc_match.group(1) if acc_match else None,
            "amount": amount_match.group(1) if amount_match else None,
            "balance": bal_match.group(1) if bal_match else None,
            "txn_type": "CREDIT" if is_credit else ("DEBIT" if has_txn_intent else "OTHER"),
            "source": "CARD" if is_card else ("BANK" if has_txn_intent else "NONE"),
            "merchant": None
        }

    return matched_data

def evaluate_test_case(test_case):
    test_id = test_case.get("test_id", "TEST_UNKNOWN")
    sender = test_case.get("raw_sender", "")
    body = test_case.get("raw_body", "")
    
    thought = test_case.get("thought_process", {})
    expected = test_case.get("expected_result", {})
    parsed = parse_with_us_template(sender, body)
    
    discrepancies = []
    for key in ["is_transaction", "bank", "account", "amount", "balance", "txn_type", "source"]:
        exp_val = expected.get(key)
        act_val = parsed.get(key)
        
        exp_str = str(exp_val).strip() if exp_val is not None else ""
        act_str = str(act_val).strip() if act_val is not None else ""
        
        if exp_str != act_str:
            discrepancies.append(f"Mismatch in '{key}': Expected='{exp_val}', Parsed='{act_val}'")
            
    passed = len(discrepancies) == 0
    
    return {
        "test_id": test_id,
        "sender": sender,
        "body": body,
        "passed": passed,
        "thought_reasoning": thought.get("reasoning", ""),
        "expected": expected,
        "parsed": parsed,
        "discrepancies": discrepancies
    }

def run_all_tests(json_file_path):
    with open(json_file_path, "r") as f:
        data = json.load(f)
        
    test_cases = data.get("test_cases", [])
    results = []
    passed_count = 0
    
    print(f"\n=================================================================")
    print(f"       US SMS TEST SUITE — COGNITIVE VS PARSER EVALUATION        ")
    print(f"=================================================================\n")
    print(f"Running {len(test_cases)} Test Cases from: {os.path.basename(json_file_path)}\n")
    
    for tc in test_cases:
        res = evaluate_test_case(tc)
        results.append(res)
        status_symbol = "✅ PASS" if res["passed"] else "❌ FAIL"
        if res["passed"]:
            passed_count += 1
            
        print(f"[{status_symbol}] {res['test_id']} ({res['sender']})")
        print(f"  SMS: \"{res['body']}\"")
        print(f"  🧠 Human/Cognitive Thought: {res['thought_reasoning']}")
        print(f"  ⚙️ Parser Extracted: Amount=${res['parsed']['amount']}, Acc={res['parsed']['account']}, Type={res['parsed']['txn_type']}, Bal=${res['parsed']['balance']}")
        
        if not res["passed"]:
            print(f"  ⚠️ DISCREPANCIES:")
            for d in res["discrepancies"]:
                print(f"     • {d}")
        print("-" * 65)

    score_pct = (passed_count / len(test_cases) * 100) if test_cases else 0
    print(f"\nFINAL RESULT: {passed_count}/{len(test_cases)} Passed ({score_pct:.1f}% Accuracy)")
    print(f"=================================================================\n")
    return results

if __name__ == "__main__":
    test_file = os.path.join(BASE_DIR, "us_sms_test_cases.json")
    if len(sys.argv) > 1:
        test_file = sys.argv[1]
    run_all_tests(test_file)
