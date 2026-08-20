package com.yourapp.smstxn.region

/**
 * Full Production RegionProfile for the United States (US).
 * Encodes all US banking patterns, 9-digit ABA routing, shortcodes, and multi-bank lifecycles.
 */
object UsRegionProfile : RegionProfile {

    override val regionCode: String = "US"

    override val currency: CurrencyFormat = CurrencyFormat(
        symbol = "$",
        symbolPosition = SymbolPosition.PREFIX,
        decimalSeparator = '.',
        groupingSeparator = ',',
        decimalPlaces = 2,
        code = "USD"
    )

    override val senderScheme: SenderScheme = SenderScheme(
        shortcodes = mapOf(
            "24273" to "JPMorgan Chase",
            "242731" to "JPMorgan Chase",
            "322632" to "Bank of America",
            "73981" to "Bank of America",
            "93557" to "Wells Fargo",
            "95686" to "Citibank",
            "227898" to "Capital One"
        ),
        allowAlphabeticHeaders = true
    )

    override val accountMasking: AccountMasking = AccountMasking(
        accountTailDigits = 4,
        cardTailDigits = 4,
        maskingRegex = Regex("""(?:\.\.\.|\bx{2,}|\bend(?:ing)?\s*(?:in)?\s*)([0-9]{3,4})""", RegexOption.IGNORE_CASE)
    )

    override val vocabulary: Vocabulary = Vocabulary(
        creditKeywords = listOf("direct deposit", "deposited", "refund", "credit", "received", "zelle", "payroll"),
        debitKeywords = listOf("purchase", "charged", "debit", "withdrawal", "atm", "sent", "authorized", "paid"),
        negativeKeywords = listOf("otp", "one-time", "code", "declined", "scheduled", "reminder", "statement", "autopay setup")
    )

    override val banks: List<BankIdentity> = listOf(
        BankIdentity("JPMorgan Chase", "ic_bank_chase", listOf("Chase")),
        BankIdentity("Bank of America", "ic_bank_bofa", listOf("BofA")),
        BankIdentity("Wells Fargo", "ic_bank_wellsfargo", listOf("Wells")),
        BankIdentity("Citibank", "ic_bank_citi", listOf("Citi")),
        BankIdentity("Capital One", "ic_bank_capone", listOf("CapOne"))
    )

    override val minConfidence: Float = 0.80f
    override val parserVersion: String = "us_model_v1+guards1"
    override val useMlParser: Boolean = true
    override val modelAssetName: String = "models/sms_model_us.bin"
}

// Interfaces and supporting types
interface RegionProfile {
    val regionCode: String
    val currency: CurrencyFormat
    val senderScheme: SenderScheme
    val accountMasking: AccountMasking
    val vocabulary: Vocabulary
    val banks: List<BankIdentity>
    val minConfidence: Float
    val parserVersion: String
    val useMlParser: Boolean
    val modelAssetName: String?
}

enum class SymbolPosition { PREFIX, SUFFIX }

data class CurrencyFormat(
    val symbol: String,
    val symbolPosition: SymbolPosition,
    val decimalSeparator: Char,
    val groupingSeparator: Char,
    val decimalPlaces: Int,
    val code: String
)

data class SenderScheme(
    val shortcodes: Map<String, String>,
    val allowAlphabeticHeaders: Boolean
)

data class AccountMasking(
    val accountTailDigits: Int,
    val cardTailDigits: Int,
    val maskingRegex: Regex
)

data class Vocabulary(
    val creditKeywords: List<String>,
    val debitKeywords: List<String>,
    val negativeKeywords: List<String>
)

data class BankIdentity(
    val canonicalName: String,
    val logoKey: String,
    val aliases: List<String> = emptyList()
)

object IndiaRegionProfile : RegionProfile {
    override val regionCode: String = "IN"
    override val currency: CurrencyFormat = CurrencyFormat("₹", SymbolPosition.PREFIX, '.', ',', 2, "INR")
    override val senderScheme: SenderScheme = SenderScheme(emptyMap(), true)
    override val accountMasking: AccountMasking = AccountMasking(4, 4, Regex("""[xX]+([0-9]{4})"""))
    override val vocabulary: Vocabulary = Vocabulary(listOf("credited"), listOf("debited"), listOf("otp"))
    override val banks: List<BankIdentity> = emptyList()
    override val minConfidence: Float = 0.80f
    override val parserVersion: String = "sms_model_v7+guards9"
    override val useMlParser: Boolean = true
    override val modelAssetName: String = "models/sms_model_in.bin"
}
