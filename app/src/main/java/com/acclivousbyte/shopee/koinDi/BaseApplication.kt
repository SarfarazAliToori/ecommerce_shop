package com.acclivousbyte.shopee.koinDi

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.stripe.android.PaymentConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        //Below API 29 (for night mode off)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(retrofitModule, utilModule, mainModule, sharePrefModule))
        }

        //
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51JIsjoJDJJJr4FA7cIvpqE0xI607R6eqiHlgHtMKj85WwJ6oP6oOiX1VxTfWXIZd0VBvTv2AFMghssNrM8WG1KiK00HfsuMG1B"
        )

    }
}