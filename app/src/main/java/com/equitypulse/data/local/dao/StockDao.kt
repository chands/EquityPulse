package com.equitypulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.equitypulse.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks ORDER BY symbol ASC")
    fun getAllStocks(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStockBySymbol(symbol: String): StockEntity?

    @Query("SELECT * FROM stocks WHERE isFollowed = 1 ORDER BY symbol ASC")
    fun getFollowedStocks(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' ORDER BY symbol ASC")
    fun searchStocks(query: String): Flow<List<StockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStocks(stocks: List<StockEntity>)

    @Update
    suspend fun updateStock(stock: StockEntity)

    @Query("UPDATE stocks SET isFollowed = :follow WHERE symbol = :symbol")
    suspend fun updateFollowStatus(symbol: String, follow: Boolean)

    @Query("SELECT isFollowed FROM stocks WHERE symbol = :symbol")
    suspend fun isStockFollowed(symbol: String): Boolean?

    @Query("UPDATE stocks SET currentPrice = :price, priceChange = :change, priceChangePercentage = :changePercent, lastUpdated = :lastUpdated WHERE symbol = :symbol")
    suspend fun updateStockPrice(
        symbol: String,
        price: Double,
        change: Double,
        changePercent: Double,
        lastUpdated: Long
    )

    @Query("DELETE FROM stocks WHERE symbol = :symbol")
    suspend fun deleteStock(symbol: String)
} 