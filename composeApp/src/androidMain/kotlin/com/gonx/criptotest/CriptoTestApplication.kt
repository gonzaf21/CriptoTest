package com.gonx.criptotest

import android.app.Application
import com.gonx.criptotest.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class CriptoTestApplication: Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@CriptoTestApplication)
        }
    }
}