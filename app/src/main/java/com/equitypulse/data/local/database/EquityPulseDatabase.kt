package com.equitypulse.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.equitypulse.data.local.dao.NewsDao
import com.equitypulse.data.local.dao.StockDao
import com.equitypulse.data.local.entity.NewsEntity
import com.equitypulse.data.local.entity.StockEntity
import com.equitypulse.util.Constants

@Database(
    entities = [NewsEntity::class, StockEntity::class],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class EquityPulseDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun stockDao(): StockDao
} 