package com.equitypulse

import android.app.Application
import com.equitypulse.di.AppComponent
import com.equitypulse.di.DaggerAppComponent

class EquityPulseApplication : Application() {
    
    lateinit var appComponent: AppComponent
        private set
    
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
} 