package com.equitypulse.di

import android.content.Context
import com.equitypulse.presentation.MainActivity
import com.equitypulse.presentation.common.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, NetworkModule::class])
interface AppComponent {
    
    fun inject(activity: MainActivity)
    
    fun viewModelFactory(): ViewModelFactory
    
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
} 