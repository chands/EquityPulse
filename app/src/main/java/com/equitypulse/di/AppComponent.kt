package com.equitypulse.di

import android.content.Context
import com.equitypulse.data.local.preferences.UserPreferencesManager
import com.equitypulse.presentation.MainActivity
import com.equitypulse.presentation.common.ViewModelFactory
import com.equitypulse.presentation.screens.settings.SettingsViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, NetworkModule::class])
interface AppComponent {
    
    fun inject(activity: MainActivity)
    
    fun viewModelFactory(): ViewModelFactory
    
    fun getSettingsViewModelFactory(): SettingsViewModel.Factory
    
    fun getUserPreferencesManager(): UserPreferencesManager
    
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
} 