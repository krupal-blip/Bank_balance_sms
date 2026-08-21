# 🚀 Android Product Flavors Setup Guide (`india` vs `usa`)

This guide provides the complete, production-ready setup for configuring Gradle `productFlavors` with regional asset isolation and automated runtime configuration.

---

## 1. `app/build.gradle.kts` (Kotlin DSL)

Paste the following inside the `android { ... }` block in your `app/build.gradle.kts`:

```kotlin
android {
    ...

    // 1. Declare Flavor Dimension
    flavorDimensions += "region"

    productFlavors {
        create("india") {
            dimension = "region"
            applicationIdSuffix = ".in"
            versionNameSuffix = "-IN"
            buildConfigField("String", "REGION_CODE", "\"IN\"")
            buildConfigField("String", "DEFAULT_CURRENCY", "\"INR\"")
            buildConfigField("String", "CURRENCY_SYMBOL", "\"₹\"")
            buildConfigField("String", "MODEL_FILE", "\"sms_model_v7.bin\"")
            manifestPlaceholders["appLabel"] = "Bank Balance"
        }

        create("usa") {
            dimension = "region"
            applicationIdSuffix = ".us"
            versionNameSuffix = "-US"
            buildConfigField("String", "REGION_CODE", "\"US\"")
            buildConfigField("String", "DEFAULT_CURRENCY", "\"USD\"")
            buildConfigField("String", "CURRENCY_SYMBOL", "\"$\"")
            buildConfigField("String", "MODEL_FILE", "\"sms_model_us.bin\"")
            manifestPlaceholders["appLabel"] = "Bank Balance USA"
        }
    }
}
```

*(If using Groovy `build.gradle`, replace `create("flavor")` with `flavor { ... }`).*

---

## 2. Directory & Asset Source Sets (Zero-Bloat Architecture)

By splitting assets into flavor source sets, each country APK only downloads its relevant model and channels:

```
app/
├── src/
│   ├── main/                                    <-- Shared Code & Assets
│   │   ├── java/com/yourapp/smstxn/
│   │   │   ├── region/RegionManager.kt
│   │   │   └── smsmodel/
│   │   └── assets/
│   │
│   ├── india/                                   <-- India-Only Assets
│   │   └── assets/
│   │       ├── models/sms_model_v7.bin          (India BiGRU model)
│   │       └── data/
│   │           ├── channels_in.json
│   │           ├── schemes_in.json
│   │           └── holidays_in_2026.json
│   │
│   └── usa/                                     <-- USA-Only Assets
│       └── assets/
│           ├── models/sms_model_us.bin          (US BiGRU model: 237 KB)
│           └── data/
│               ├── channels_us.json
│               ├── schemes_us.json
│               ├── holidays_us_2026.json
│               ├── us_aba_routing_registry.json
│               ├── netbanking_us.json
│               └── us_bank_sms_formats.json
```

---

## 3. Application Initialization (`App.kt`)

In your `Application` subclass, initialize `RegionManager` automatically from `BuildConfig`:

```kotlin
package com.yourapp

import android.app.Application
import com.yourapp.smstxn.region.RegionManager
import com.yourapp.smstxn.region.UsRegionProfile
import com.yourapp.smstxn.region.InRegionProfile

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Automatically bind active region profile based on flavor
        when (BuildConfig.REGION_CODE) {
            "US" -> RegionManager.setActiveProfile(UsRegionProfile(this))
            "IN" -> RegionManager.setActiveProfile(InRegionProfile(this))
            else -> RegionManager.setActiveProfile(InRegionProfile(this))
        }
    }
}
```

---

## 4. Build & Verify Commands

Run these commands in terminal or select from Android Studio's **Build Variants** tab:

```bash
# Build India APK
./gradlew assembleIndiaDebug
./gradlew assembleIndiaRelease

# Build USA APK
./gradlew assembleUsaDebug
./gradlew assembleUsaRelease
```
