package com.yourapp.smstxn.region

import java.util.Locale

/**
 * Global Region Manager & RegionProfile Dispatcher.
 * Provides runtime switching between India ("IN"), United States ("US"), and future markets.
 */
object RegionManager {

    private var activeProfile: RegionProfile = UsRegionProfile

    /**
     * Initializes or switches the global region profile.
     * Guaranteed to preserve existing India logic without mutation.
     */
    fun setRegion(regionCode: String) {
        activeProfile = when (regionCode.uppercase(Locale.ROOT)) {
            "IN" -> IndiaRegionProfile
            "US", "USA" -> UsRegionProfile
            else -> UsRegionProfile
        }
    }

    fun getProfile(): RegionProfile = activeProfile
}
