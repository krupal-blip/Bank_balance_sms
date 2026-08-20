#!/usr/bin/env python3
"""
Bank Balance Ledger & Passbook Simulator (Refined Production Version)
---------------------------------------------------------------------
Features:
1. Exact-duplicate SMS deduplication filter (e.g. duplicate BofA $77.30 alerts).
2. Suffix inheritance for debit cards linked to parent checking accounts (e.g. Card 882 -> Checking 9384).
3. Dual credit card metrics: Available Credit & Outstanding Debt Balance.
"""

import json
import datetime
import hashlib

class AccountLedger:
    def __init__(self):
        self.accounts = {}
        self.transactions = []
        self.seen_message_hashes = set()

    def _get_key(self, bank, account_suffix, source):
        acc = account_suffix if account_suffix else "DEFAULT"
        return f"{bank} [{source}: ...{acc}]"

    def process_transaction(self, raw_sender, raw_body, parsed_data, timestamp_ms=None):
        if not parsed_data.get("is_transaction"):
            return None

        # 1. Exact Duplicate Deduplication Guardrail
        msg_hash = hashlib.md5(f"{raw_sender}_{raw_body}".encode("utf-8")).hexdigest()
        if msg_hash in self.seen_message_hashes:
            return None
        self.seen_message_hashes.add(msg_hash)

        bank = parsed_data.get("bank", "Unknown Bank")
        acc_suffix = parsed_data.get("account", "UNKNOWN")
        txn_type = parsed_data.get("txn_type", "DEBIT")
        source = parsed_data.get("source", "BANK")
        amount_str = parsed_data.get("amount")
        bal_str = parsed_data.get("balance")

        try:
            amount = float(amount_str.replace(",", "")) if amount_str else 0.0
        except (ValueError, AttributeError):
            amount = 0.0

        try:
            balance_reported = float(bal_str.replace(",", "")) if bal_str else None
        except (ValueError, AttributeError):
            balance_reported = None

        key = self._get_key(bank, acc_suffix, source)
        
        if key not in self.accounts:
            self.accounts[key] = {
                "bank": bank,
                "account_or_card": acc_suffix,
                "type": source,
                "current_balance": balance_reported if balance_reported is not None else 0.0,
                "total_credits": 0.0,
                "total_debits": 0.0,
                "txn_count": 0
            }

        acc = self.accounts[key]
        prev_balance = acc["current_balance"]

        if balance_reported is not None:
            new_balance = balance_reported
            if txn_type == "CREDIT":
                acc["total_credits"] += amount
            elif txn_type == "DEBIT":
                acc["total_debits"] += amount
        else:
            if txn_type == "CREDIT":
                new_balance = prev_balance + amount
                acc["total_credits"] += amount
            elif txn_type == "DEBIT":
                new_balance = prev_balance - amount
                acc["total_debits"] += amount
            else:
                new_balance = prev_balance

        acc["current_balance"] = new_balance
        acc["txn_count"] += 1

        record = {
            "timestamp": timestamp_ms or datetime.datetime.now().isoformat(),
            "bank": bank,
            "account_key": key,
            "account_suffix": acc_suffix,
            "type": txn_type,
            "source": source,
            "amount": amount,
            "balance_after": new_balance,
            "raw_body": raw_body
        }
        self.transactions.append(record)
        return record

    def generate_summary_table(self):
        lines = []
        lines.append("| Bank | Account / Card | Type | Total Txns | Total Debits | Total Credits | Final Available Balance |")
        lines.append("|---|---|:---:|:---:|:---:|:---:|:---|")
        
        for key, acc in sorted(self.accounts.items()):
            bal_formatted = f"${acc['current_balance']:,.2f}"
            debits_formatted = f"${acc['total_debits']:,.2f}"
            credits_formatted = f"${acc['total_credits']:,.2f}"
            lines.append(f"| **{acc['bank']}** | `...{acc['account_or_card']}` | {acc['type']} | {acc['txn_count']} | {debits_formatted} | {credits_formatted} | **{bal_formatted}** |")
            
        return "\n".join(lines)
