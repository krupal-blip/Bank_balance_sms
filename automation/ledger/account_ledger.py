#!/usr/bin/env python3
"""
Bank Balance Ledger & Passbook Simulator
----------------------------------------
Simulates a real Android device's SQLite Database / Room DAO.
Maintains stateful accounts, credit cards, running ledger balances, and transaction histories.

For any parsed batch of SMS/Notifications:
1. Feeds transactions in chronological order.
2. Updates per-account / per-card ledger balances.
3. Generates a comprehensive Account Passbook & Balance Table.
"""

import json
import datetime

class AccountLedger:
    def __init__(self):
        # Key: (bank, account_or_card_suffix, source_type)
        self.accounts = {}
        self.transactions = []

    def _get_key(self, bank, account_suffix, source):
        acc = account_suffix if account_suffix else "DEFAULT"
        return f"{bank} [{source}: ...{acc}]"

    def process_transaction(self, raw_sender, raw_body, parsed_data, timestamp_ms=None):
        if not parsed_data.get("is_transaction"):
            return None

        bank = parsed_data.get("bank", "Unknown Bank")
        acc_suffix = parsed_data.get("account", "UNKNOWN")
        txn_type = parsed_data.get("txn_type", "DEBIT")
        source = parsed_data.get("source", "BANK")
        amount_str = parsed_data.get("amount")
        bal_str = parsed_data.get("balance")

        try:
            amount = float(amount_str.replace(",", "")) if amount_str else 0.0
        except ValueError:
            amount = 0.0

        try:
            balance_reported = float(bal_str.replace(",", "")) if bal_str else None
        except ValueError:
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
        """Generates markdown summary table of all accounts and final balances."""
        lines = []
        lines.append("| Bank | Account / Card | Type | Total Txns | Total Debits | Total Credits | Final Available Balance |")
        lines.append("|---|---|:---:|:---:|:---:|:---:|:---|")
        
        for key, acc in sorted(self.accounts.items()):
            bal_formatted = f"${acc['current_balance']:,.2f}"
            debits_formatted = f"${acc['total_debits']:,.2f}"
            credits_formatted = f"${acc['total_credits']:,.2f}"
            lines.append(f"| **{acc['bank']}** | `...{acc['account_or_card']}` | {acc['type']} | {acc['txn_count']} | {debits_formatted} | {credits_formatted} | **{bal_formatted}** |")
            
        return "\n".join(lines)

    def to_json(self):
        return {
            "accounts": self.accounts,
            "transactions": self.transactions
        }
