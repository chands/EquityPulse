package com.equitypulse.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.equitypulse.data.local.dao.NewsDao
import com.equitypulse.data.local.dao.StockDao
import com.equitypulse.data.local.database.EquityPulseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Migration from version 1 to 2
            // Changed publishDate in NewsEntity from Long to String
            
            // Create a temporary table with the new schema
            database.execSQL(
                "CREATE TABLE news_temp (" +
                "id TEXT NOT NULL PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "summary TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "originalUrl TEXT NOT NULL, " +
                "imageUrl TEXT NOT NULL, " +
                "publishDate TEXT NOT NULL, " + // Changed from LONG to TEXT
                "source TEXT NOT NULL, " +
                "relatedStockSymbols TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "isBookmarked INTEGER NOT NULL)"
            )
            
            // Copy data from the old table to the new one, converting publishDate to string
            database.execSQL(
                "INSERT INTO news_temp (id, title, summary, content, originalUrl, imageUrl, " +
                "publishDate, source, relatedStockSymbols, category, isBookmarked) " +
                "SELECT id, title, summary, content, originalUrl, imageUrl, " +
                "CAST(publishDate AS TEXT), source, relatedStockSymbols, category, isBookmarked " +
                "FROM news"
            )
            
            // Drop the old table
            database.execSQL("DROP TABLE news")
            
            // Rename the new table to the original name
            database.execSQL("ALTER TABLE news_temp RENAME TO news")
        }
    }
    
    @Provides
    @Singleton
    fun provideDatabase(context: Context): EquityPulseDatabase {
        return Room.databaseBuilder(
            context,
            EquityPulseDatabase::class.java,
            "equitypulse.db"
        )
        .addMigrations(migration1To2)
        .build()
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