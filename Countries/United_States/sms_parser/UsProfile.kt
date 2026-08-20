package com.yourapp.smstxn.region

import java.math.BigDecimal

/**
 * UNITED STATES (US) Production RegionProfile Implementation.
 * Conforms strictly to sms_trans_tracker production architecture.
 */
object UsProfile : RegionProfile {

    override val regionCode = "US"

    // -------------------------------------------------------------------------
    // 1. Money Configuration (USD)
    // -------------------------------------------------------------------------
    override val currency = CurrencyFormat(
        symbol = "$",
        symbolPosition = SymbolPosition.PREFIX,
        decimalSeparator = '.',
        groupingSeparator = ',',
        decimalPlaces = 2,
        grouping = Grouping.UNIFORM_3,
        maxIntegerDigits = 9, // Sub-billion US transactions
        currencyWords = listOf("$", "usd", "dollars", "dollar")
    )

    // -------------------------------------------------------------------------
    // 2. Sender Identity Scheme (US 5-6 digit Shortcodes & Alphanumeric)
    // -------------------------------------------------------------------------
    override val senderScheme = object : SenderScheme {

        // Official US Registered Shortcodes and Sender IDs
        private val SHORTCODE_MAP: Map<String, String> = mapOf(
            "24273" to "Chase",
            "242731" to "Chase",
            "CHASE" to "Chase",
            "73981" to "Bank of America",
            "34343" to "Bank of America",
            "BOFA" to "Bank of America",
            "93557" to "Wells Fargo",
            "93748" to "Wells Fargo",
            "95686" to "Citibank",
            "692484" to "Citibank",
            "CITI" to "Citibank",
            "227898" to "Capital One",
            "227767" to "Capital One",
            "227373" to "Capital One",
            "872265" to "U.S. Bank",
            "762265" to "PNC Bank",
            "90742" to "PNC Bank",
            "878478" to "Truist Bank",
            "832265" to "TD Bank",
            "266226" to "BMO Bank"
        )

        private val ROOTS: List<Pair<String, String>> = listOf(
            "CHASE" to "Chase",
            "BOFA" to "Bank of America",
            "WELLS" to "Wells Fargo",
            "CITI" to "Citibank",
            "CAPONE" to "Capital One",
            "USBANK" to "U.S. Bank",
            "PNC" to "PNC Bank",
            "TRUIST" to "Truist Bank",
            "TDBANK" to "TD Bank",
            "BMO" to "BMO Bank"
        )

        // Only long 10-digit/E.164 phone numbers count as personal numbers.
        // 5-6 digit shortcodes are VALID US bank senders.
        private val BARE_PERSONAL_PHONE_RE = Regex("^\\+?1?\\d{10,}$")

        override fun resolve(address: String): String? {
            if (address.isBlank()) return null
            val cleaned = address.trim().uppercase().replace("-", "").replace(" ", "")
            
            // Check direct 5-6 digit shortcode lookup
            SHORTCODE_MAP[cleaned]?.let { return it }

            // Reject if bare personal phone number
            if (BARE_PERSONAL_PHONE_RE.matches(cleaned.removePrefix("+"))) return null

            // Match root ticker for alphanumeric senders
            ROOTS.filter { cleaned.startsWith(it.first) }
                .maxByOrNull { it.first.length }
                ?.let { return it.second }

            return ROOTS.filter { cleaned.contains(it.first) }
                .maxByOrNull { it.first.length }?.second
        }

        override fun canonicalName(name: String): String {
            if (name.isBlank()) return ""
            val cleaned = name.trim().uppercase()
            resolve(cleaned)?.let { return it }
            val norm = cleaned.filter(Char::isLetter)
            ROOTS.firstOrNull { it.first == norm }?.let { return it.second }
            ROOTS.firstOrNull { (_, canonical) ->
                canonical.uppercase().filter(Char::isLetter) == norm
            }?.let { return it.second }
            return name
        }
    }

    // -------------------------------------------------------------------------
    // 3. Account Masking Configuration (US Format)
    // -------------------------------------------------------------------------
    override val accountMasking = AccountMasking(
        // Covers: ...4321, (...4321), ending in 4321, *4321, XXXX4321, bare 4321
        accountRegex = Regex("""(?:\.{2,}|\*{2,}|x{2,}|\b)(?<tail>\d{4})\b""", RegexOption.IGNORE_CASE),
        cardEndingRegex = Regex(
            """(?:credit|debit)?\s*card\s*(?:no\.?|number)?\s*(?:ending\s*(?:with|in)?|x|\*+)\s*[Xx*]*(\d{4,})""",
            RegexOption.IGNORE_CASE
        ),
        minAccountDigits = 4 // US industry standard is 4-digit tails
    )

    // -------------------------------------------------------------------------
    // 4. US Banking Vocabulary & Guardrails
    // -------------------------------------------------------------------------
    override val vocabulary = Vocabulary(
        creditWords = listOf(
            "credited", "credit", "deposit", "deposited", "direct deposit", "refund", "refunded", 
            "received", "cashback", "added to"
        ),
        debitWords = listOf(
            "debited", "debit", "charged", "spent", "purchase", "purchased", "withdrawal", 
            "withdrawn", "paid", "payment to", "sent", "authorized", "authorization"
        ),
        qualifiedBalanceWords = listOf(
            "available balance", "available bal", "avail bal", "avail balance", "current balance",
            "net balance", "closing balance", "new balance", "account balance"
        ),
        bareBalanceWords = listOf("bal", "balance"),

        // US ACH / AutoPay Vocabulary (Equivalent of India NACH)
        mandateWords = listOf(
            "ach", "autopay", "auto-debit", "direct debit", "recurring payment", 
            "bill pay", "automatic payment", "standing order"
        ),
        mandateCreationVerbs = listOf(
            "scheduled", "set up", "enrolled", "activated", "created", "will be debited", "is scheduled"
        ),
        mandateCancelVerbs = listOf(
            "cancelled", "canceled", "stopped", "paused", "deleted", "unsubscribed"
        ),

        otpWords = listOf("otp", "verification code", "security code", "passcode", "safepass", "one-time code", "temp code"),
        declinedWords = listOf("declined", "payment failed", "insufficient funds", "card blocked", "unauthorized", "reversed"),
        reminderWords = listOf("payment due", "minimum payment due", "low balance alert", "statement ready", "bill reminder"),

        creditCardBodyWords = listOf("credit card", "card member", "charge card", "citi double cash", "freedom unlimited", "sapphire"),
        debitCardBodyWords = listOf("debit card", "check card", "atm card"),

        // Future tense markers fed as synthetic token to model
        futureMarkers = listOf(
            "will be", "shall be", "is scheduled to", "is due on", "is going to", "is expected to", "will post"
        ),

        // US Reference & Limit Vocabulary
        noticeMarkers = listOf("ach alert", "autopay notice", "payment confirmation", "trace id", "auth code"),
        referenceLabels = listOf("trace #", "trace number", "ref #", "reference number", "confirmation code", "auth id", "txn id"),
        limitNotBalanceWords = listOf("available credit", "available limit", "credit line", "credit limit")
    )

    // -------------------------------------------------------------------------
    // 5. Canonical Bank Identities
    // -------------------------------------------------------------------------
    override val banks = listOf(
        BankIdentity("Chase", "bl_chase", aliases = listOf("JPMorgan Chase", "Chase Bank")),
        BankIdentity("Bank of America", "bl_bofa", aliases = listOf("BofA", "BOFA")),
        BankIdentity("Wells Fargo", "bl_wellsfargo", aliases = listOf("Wells Fargo Bank", "WFB")),
        BankIdentity("Citibank", "bl_citi", aliases = listOf("Citi", "Citi Cards")),
        BankIdentity("Capital One", "bl_capone", aliases = listOf("CapOne", "Capital One Bank")),
        BankIdentity("U.S. Bank", "bl_usbank", aliases = listOf("US Bank", "US Bancorp")),
        BankIdentity("PNC Bank", "bl_pnc", aliases = listOf("PNC", "PNC Financial")),
        BankIdentity("Truist Bank", "bl_truist", aliases = listOf("Truist", "BB&T", "SunTrust")),
        BankIdentity("TD Bank", "bl_td", aliases = listOf("TD Bank US", "TD")),
        BankIdentity("BMO Bank", "bl_bmo", aliases = listOf("BMO Harris", "BMO"))
    )

    override val minConfidence = 0.80f
    override val parserVersion = "us_model_v1+guards1"
    override val useMlParser = false // Ships safely with 100% regex+guardrails engine first
    override val modelAssetName = null
}
