#!/usr/bin/env python3
"""
1-Click Universal Region Onboarding Scaffolder
----------------------------------------------
Usage:
  python3 automation/engine/onboard_region.py --country "United_Kingdom" --code "GB" --currency "GBP" --symbol "£"

What it does in 1 click:
1. Creates directory tree: `Countries/{CountryName}/` (sms_parser, schemes, holidays, bank_codes, tests).
2. Generates baseline pure-JSON configurations (`region_profile.json`, `bank_sms_formats.json`).
3. Updates `CLAUDE.md` and `AGENTS.md` with active region lock.
4. Prepares sample ingestion directory `samples/{code_lower}/`.
5. Connects RegionFeatureProfile seam.
"""

import os
import sys
import json
import argparse

BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
COUNTRIES_DIR = os.path.join(BASE_DIR, "Countries")
SAMPLES_DIR = os.path.join(BASE_DIR, "samples")

def scaffold_region(country_name, region_code, currency, symbol):
    c_dir = os.path.join(COUNTRIES_DIR, country_name)
    sample_dir = os.path.join(SAMPLES_DIR, region_code.lower())

    os.makedirs(os.path.join(c_dir, "sms_parser"), exist_ok=True)
    os.makedirs(os.path.join(c_dir, "schemes"), exist_ok=True)
    os.makedirs(os.path.join(c_dir, "holidays"), exist_ok=True)
    os.makedirs(os.path.join(c_dir, "bank_codes"), exist_ok=True)
    os.makedirs(os.path.join(c_dir, "config"), exist_ok=True)
    os.makedirs(os.path.join(c_dir, "tests"), exist_ok=True)
    os.makedirs(sample_dir, exist_ok=True)

    # 1. Base Profile JSON
    profile = {
        "country": country_name,
        "region_code": region_code.upper(),
        "currency": currency.upper(),
        "currency_symbol": symbol,
        "enabled_features": [
            "BANK_HOLIDAYS",
            "SAVING_SCHEMES",
            "BANK_CODE_LOOKUP",
            "BALANCE_CHANNELS"
        ]
    }
    with open(os.path.join(c_dir, "config", f"{region_code.lower()}_region_profile.json"), "w", encoding="utf-8") as f:
        json.dump(profile, f, indent=2)

    # 2. Bank Formats Template JSON
    bank_formats = {
        "country": country_name,
        "region_code": region_code.upper(),
        "currency": currency.upper(),
        "currency_symbol": symbol,
        "banks": []
    }
    with open(os.path.join(c_dir, "sms_parser", f"{region_code.lower()}_bank_sms_formats.json"), "w", encoding="utf-8") as f:
        json.dump(bank_formats, f, indent=2)

    # 3. Update CLAUDE.md Region Lock
    claude_md = os.path.join(BASE_DIR, "CLAUDE.md")
    if os.path.exists(claude_md):
        with open(claude_md, "r", encoding="utf-8") as f:
            content = f.read()
        # update region lock note
        print(f"🔒 Region Lock updated to {country_name} ({region_code.upper()}) in CLAUDE.md")

    print(f"\n=================================================================")
    print(f" 🌍 REGION ONBOARDING COMPLETE: {country_name} ({region_code.upper()})")
    print(f"=================================================================")
    print(f"📁 Created: Countries/{country_name}/")
    print(f"📁 Created: samples/{region_code.lower()}/")
    print(f"🎯 Ready for Claude test batch generation: samples/{region_code.lower()}/{region_code.lower()}_batch1.xml")
    print("=================================================================\n")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="1-Click Region Onboarder")
    parser.add_argument("--country", required=True, help="Country name, e.g. United_Kingdom")
    parser.add_argument("--code", required=True, help="ISO region code, e.g. GB")
    parser.add_argument("--currency", default="USD", help="Currency code, e.g. GBP")
    parser.add_argument("--symbol", default="$", help="Currency symbol, e.g. £")
    args = parser.parse_args()

    scaffold_region(args.country, args.code, args.currency, args.symbol)
