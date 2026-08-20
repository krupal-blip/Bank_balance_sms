package com.yourapp.region

/**
 * UNITED STATES (US) Production RegionFeatureProfile Implementation.
 * Bridges all 5 region-dependent product features for the US market behind ONE seam.
 * Follows app_region_templates architecture.
 */
object UsFeatureProfile : RegionFeatureProfile {

    override val regionCode = "US"

    // -------------------------------------------------------------------------
    // 01 — Bank Holidays (US Federal Reserve & Financial Markets)
    // -------------------------------------------------------------------------
    override val holidays = HolidayConfig(
        scope = HolidayScope.NATIONAL, // US Federal holidays apply nationwide
        subRegionLabel = null, // No state picker required for federal reserve bank holidays
        recurringClosures = emptyList(), // US Federal holidays are explicit dates, no 2nd/4th Saturday rule
        sourceDateFormat = "yyyy-MM-dd",
        bundledAssetPattern = "holidays_us_2026.json"
    )

    // -------------------------------------------------------------------------
    // 02 — Saving Schemes (US Tax-Advantaged Wealth & Retirement Products)
    // -------------------------------------------------------------------------
    override val schemes = SchemeConfig(
        catalogAsset = "schemes_us.json",
        ratesReviewedLabel = "IRS Annual Publication (Rev. Proc. / Notice for 2026)"
    )

    // -------------------------------------------------------------------------
    // 03 — Bank Code Lookup (ABA Routing Transit Number - 9 Digits)
    // -------------------------------------------------------------------------
    override val bankCodes = BankCodeConfig(
        primaryCodeName = "ABA Routing Number",
        primaryCodeRegex = Regex("^[0-9]{9}$"),
        secondaryCodeNames = listOf("Fedwire Sub-Routing", "SWIFT/BIC"),
        lookupLevels = listOf("BANK", "BRANCH") // Flat national hierarchy (Bank -> Branch/Location)
    )

    // -------------------------------------------------------------------------
    // 04 — Balance Channels (US Automated Telephone & SMS Keyword Channels)
    // -------------------------------------------------------------------------
    override val balanceChannels = BalanceChannelConfig(
        channels = setOf(
            BalanceChannel.IVR,        // 24/7 Automated Phone Banking (Universal in US)
            BalanceChannel.SMS_KEYWORD // On-Demand Text Banking (e.g. Wells Fargo 'BAL' to 93557)
        ),
        channelTableAsset = "channels_us.json"
    )

    // -------------------------------------------------------------------------
    // 05 — Net Banking Portals (Direct HTTPS Online Banking Portals)
    // -------------------------------------------------------------------------
    override val netBanking = NetBankingConfig(
        portalTableAsset = "netbanking_us.json",
        requireHttps = true,
        allowlistHostsOnly = true
    )

    // -------------------------------------------------------------------------
    // Enabled Features in US Market
    // -------------------------------------------------------------------------
    override val enabledFeatures: Set<RegionFeature> = setOf(
        RegionFeature.BANK_HOLIDAYS,
        RegionFeature.SAVING_SCHEMES,
        RegionFeature.BANK_CODE_LOOKUP,
        RegionFeature.BALANCE_CHANNELS, // Supported via IVR & SMS keywords; USSD/Missed-Call disabled
        RegionFeature.NET_BANKING
    )
}
