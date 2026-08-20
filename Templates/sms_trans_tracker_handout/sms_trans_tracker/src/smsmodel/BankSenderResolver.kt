package com.check.bank.balance.banking.tool.smsmodel

/**
 * Resolves a bank name from the SMS sender's DLT header (e.g. "VM-HDFCBK",
 * "AD-HDFCCC"), independent of the ML model. Lookup, not learned — TRAI
 * codes are fixed strings.
 *
 * IMPORTANT: matches on the bank's ROOT ticker, not a full fixed code. A
 * single bank registers many sender IDs — HDFCBK (banking), HDFCCC (credit
 * card), HDFCLN (loans), HDFCUB, HDFCMF... all start with "HDFC". Listing
 * every product suffix is unmaintainable; matching the root once covers
 * all of them, including ones this bank registers in the future with zero
 * table changes.
 *
 * Risk/impact note: this only resolves the DISPLAYED bank name/logo. It
 * never touches amount/account — those have their own regex guardrails
 * (see BankTransactionFilterMl.kt). So a rare root collision costs a wrong
 * bank label, not a wrong balance — low-stakes, tune freely.
 *
 * Known gap, stated plainly: local cooperative banks and many regional
 * rural banks (RRBs) don't follow a consistent DLT code, some send from a
 * plain phone number with zero bank hint. Returns null for those — caller
 * falls back to the model's body-derived BANK span.
 */
object BankSenderResolver {

    // root ticker (as it appears at the START of the code, after the 2-char
    // DLT prefix) -> canonical bank name. One entry covers every product
    // variant that bank registers.
    private val ROOTS: List<Pair<String, String>> = listOf(
        // ==========================================
        // 1. NATIONAL & MAJOR PRIVATE BANKS
        // ==========================================
        "HDFC" to "HDFC Bank",
        "ICICI" to "ICICI Bank",
        "AXIS" to "Axis Bank",
        "KOTAK" to "Kotak Mahindra Bank",
        "INDUS" to "IndusInd Bank",
        "YES" to "Yes Bank",
        "IDFC" to "IDFC FIRST Bank",
        "RBL" to "RBL Bank",
        "BANDHN" to "Bandhan Bank",
        "DCB" to "DCB Bank",
        "FED" to "Federal Bank",
        "SIB" to "South Indian Bank",
        "KVB" to "Karur Vysya Bank",
        "TMB" to "Tamilnad Mercantile Bank",
        "CUB" to "City Union Bank",
        "IDBI" to "IDBI Bank",
        "JKB" to "Jammu & Kashmir Bank",
        "DHAN" to "Dhanlaxmi Bank",
        "CSB" to "CSB Bank",
        "KBL" to "Karnataka Bank",
        "DBSS" to "DBS Bank India",

        // ==========================================
        // 2. PUBLIC SECTOR BANKS
        // ==========================================
        "SBI" to "State Bank of India",
        "PNB" to "Punjab National Bank",
        "BOB" to "Bank of Baroda", "BARODA" to "Bank of Baroda",
        "BOI" to "Bank of India",
        "CNRB" to "Canara Bank", "CANARA" to "Canara Bank", "CANBNK" to "Canara Bank",
        "UBIN" to "Union Bank of India", "UNION" to "Union Bank of India",
        "IOB" to "Indian Overseas Bank",
        "CBOI" to "Central Bank of India", "CENT" to "Central Bank of India",
        "INDBK" to "Indian Bank", "INDIAN" to "Indian Bank",
        "UCO" to "UCO Bank",
        "PSB" to "Punjab & Sind Bank",
        "MAHA" to "Bank of Maharashtra",
        "ARYVRT" to "Aryavart Bank",
        "KAGBNK" to "Karnataka Gramin Bank",
        "MAHGRM" to "Maharashtra Gramin Bank",


        // ==========================================
        // 3. SMALL FINANCE & PAYMENTS BANKS
        // ==========================================
        "AUBANK" to "AU Small Finance Bank", "AUSFB" to "AU Small Finance Bank",
        "EQUITB" to "Equitas Small Finance Bank", "EQUITAS" to "Equitas Small Finance Bank",
        "UJJIVN" to "Ujjivan Small Finance Bank", "UJJIVAN" to "Ujjivan Small Finance Bank",
        "ESAF" to "ESAF Small Finance Bank",
        "JANA" to "Jana Small Finance Bank",
        "SURYA" to "Suryoday Small Finance Bank", "SURYOB" to "Suryoday Small Finance Bank",
        "CAPITL" to "Capital Small Finance Bank",
        "UNITY" to "Unity Small Finance Bank",
        "UTKARSH" to "Utkarsh Small Finance Bank",
        "SHIVAL" to "Shivalik Small Finance Bank",
        "PAYTM" to "Paytm Payments Bank",
        "AIRTEL" to "Airtel Payments Bank", "AIRPAY" to "Airtel Payments Bank",
        "FINO" to "Fino Payments Bank",
        "IPPB" to "India Post Payments Bank",
        "NSDL" to "NSDL Payments Bank",
        "JIOPAY" to "Jio Payments Bank",

        // ==========================================
        // 4. FOREIGN BANKS & CREDIT CARDS
        // ==========================================
        "HSBC" to "HSBC Bank",
        "CITI" to "Citibank",
        "SCB" to "Standard Chartered Bank", "STANC" to "Standard Chartered Bank",
        "DEUT" to "Deutsche Bank", "DEUTBK" to "Deutsche Bank",
        "AMEX" to "American Express",
        "BARCLAY" to "Barclays Bank", "BARCLY" to "Barclays Bank",
        "IOBMSG" to "Indian Overseas Bank",
        "CBINBK" to "Central Bank of India",
        "BOMBNK" to "Bank of Maharashtra",
        "EQTSBK" to "Equitas Small Finance Bank",
        "JANABK" to "Jana Small Finance Bank",
        "TMBANK" to "Tamilnad Mercantile Bank",
        "INDBNK" to "Indian Bank",
        "UNIONB" to "Union Bank of India",
        "BGGBNK" to "Baroda Gujarat Gramin Bank",
        "BOIIND" to "Bank of India",
        "ESAFBK" to "ESAF Small Finance Bank",
        "DBSBNK" to "DBS Bank",
        "UCOBNK" to "UCO Bank",
        "BOBSMS" to "Bank of Baroda",
        "FINCRB" to "Fincare Small Finance Bank",
        "UJJVAN" to "Ujjivan Small Finance Bank",
        "RMGBNK" to "Rajasthan Marudhara Gramin Bank",


        // ==========================================
        // 5. URBAN CO-OPERATIVE BANKS (STATEWISE)
        // Lower confidence than sections 1-4 — tickers reconstructed, not
        // verified against the TRAI registry. A wrong match here costs a
        // wrong bank LABEL only (amount/account have separate guardrails,
        // see BankTransactionFilterMl.kt), so kept in, but flagged.
        //
        // "COOP" intentionally dropped: too generic (every entry in this
        // section IS a co-operative bank), would false-positive-match any
        // unrelated co-op sender via the contains() fallback in resolve().
        // "SVC"/"SVCB" merged to one name — Shamrao Vithal Co-op Bank
        // rebranded to "SVC Co-operative Bank"; they're the same bank, not
        // two different results depending on which code matched.
        // ==========================================

        // --- GUJARAT ---
        "VARA" to "The Varachha Co-operative Bank", "VARACB" to "The Varachha Co-operative Bank",
        "KALUPU" to "Kalupur Commercial Co-op Bank",
        "AMCB" to "Ahmedabad Mercantile Co-op Bank",
        "SMCB" to "Surat Merchant Co-operative Bank",
        "CHARA" to "Charotar Nagrik Sahakari Bank",
        "GSCB" to "Gujarat State Co-operative Bank",
        "NUTAN" to "Nutan Nagarik Sahakari Bank",
        "RAJKOT" to "Rajkot Nagarik Sahakari Bank",
        "MEHSAN" to "Mehsana Urban Co-operative Bank",
        "SURAT" to "Surat National Co-operative Bank",

        // --- MAHARASHTRA & GOA ---
        "SARASW" to "Saraswat Bank",
        "KOSAM" to "Cosmos Bank",
        "TJSB" to "TJSB Sahakari Bank",
        "NKGSB" to "NKGSB Co-operative Bank",
        "DNSB" to "Dombivli Nagari Sahakari Bank",
        "ABHY" to "Abhyudaya Co-operative Bank",
        "BASS" to "Bassein Catholic Co-operative Bank",
        "MSCB" to "Maharashtra State Co-operative Bank",
        "GPCB" to "Gopinath Patil Parsik Janata Bank",
        "JANATA" to "Janata Sahakari Bank Pune",
        "SVC" to "SVC Co-operative Bank", "SVCB" to "SVC Co-operative Bank",
        "BOMBA" to "Bombay Mercantile Co-op Bank",
        "GUCB" to "Goa Urban Co-operative Bank",

        // --- KARNATAKA, TAMIL NADU & KERALA ---
        "SUCO" to "SUCO Souharda Sahakari Bank",
        "KSCB" to "Karnataka State Co-operative Apex Bank",
        "TNCB" to "Tamil Nadu State Apex Co-op Bank",
        "REPCO" to "Repco Bank",
        "KGB" to "Kerala Gramin Bank",
        "KADUTH" to "Kaduthuruthy Urban Co-op Bank",

        // --- TELANGANA & ANDHRA PRADESH ---
        "APCOB" to "AP State Co-operative Bank",
        "TSCAB" to "Telangana State Coop Apex Bank",
        "GAYATR" to "Gayatri Co-operative Urban Bank",
        "VISAKH" to "Visakhapatnam Co-operative Bank",

        // --- NORTH & EAST (UP, MP, PB, WB, RJ) ---
        "WBSCB" to "West Bengal State Co-operative Bank",
        "UPCB" to "UP State Co-operative Bank",
        "RSCB" to "Rajasthan State Co-operative Bank",
        "MPSCB" to "MP Rajya Sahakari Bank",
        "CITIZN" to "Citizens Urban Co-operative Bank",
    )

    private val DLT_PREFIX_RE = Regex("^[A-Z0-9]{2}-")
    private val BARE_PHONE_RE = Regex("^\\+?\\d{6,}$")

    /** Returns canonical bank name, or null if this sender gives no bank signal. */
    fun resolve(address: String): String? {
        if (address.isBlank()) return null
        val cleaned = address.trim().uppercase()

        if (BARE_PHONE_RE.matches(cleaned.removePrefix("+"))) return null

        val code = cleaned.replaceFirst(DLT_PREFIX_RE, "")

        // startsWith = the code's own ticker prefix; prefer the LONGEST
        // matching root so e.g. "SCB" doesn't shadow a hypothetical longer,
        // more specific root sharing the same first letters.
        val match = ROOTS.filter { code.startsWith(it.first) }.maxByOrNull { it.first.length }
        if (match != null) return match.second

        // fallback: root appears anywhere in the code, not just at the start
        return ROOTS.filter { code.contains(it.first) }.maxByOrNull { it.first.length }?.second
    }

    /** Collapses any spelling or abbreviation of a bank to its canonical display name. */
    fun canonicalName(name: String): String {
        if (name.isBlank()) return ""
        val cleaned = name.trim().uppercase()
        if (DLT_PREFIX_RE.containsMatchIn(cleaned)) {
            resolve(cleaned)?.let { return it }
        }
        val norm = cleaned.filter(Char::isLetter)
        ROOTS.firstOrNull { it.first == norm }?.let { return it.second }
        ROOTS.firstOrNull { (_, canonical) ->
            canonical.uppercase().filter(Char::isLetter) == norm
        }?.let { return it.second }
        return name
    }
}