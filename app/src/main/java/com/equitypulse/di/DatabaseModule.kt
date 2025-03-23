package com.equitypulse.di

import android.content.Context
import androidx.room.Room
import com.equitypulse.data.local.dao.NewsDao
import com.equitypulse.data.local.dao.StockDao
import com.equitypulse.data.local.database.EquityPulseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(context: Context): EquityPulseDatabase {
        return Room.databaseBuilder(
            context,
            EquityPulseDatabase::class.java,
            "equitypulse.db"
        ).build()
    }
    
    @Provides
    fun provideNewsDao(database: EquityPulseDatabase): NewsDao {
        return database.newsDao()
    }
    
    @Provides
    fun provideStockDao(database: EquityPulseDatabase): StockDao {
        return database.stockDao()
    }
} 