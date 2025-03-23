package com.equitypulse.domain.repository

import com.equitypulse.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getAllStocks(): List<Stock>
    suspend fun getStockBySymbol(symbol: String): Stock?
    fun getFollowedStocks(): Flow<List<Stock>>
    suspend fun followStock(symbol: String, follow: Boolean)
    suspend fun isStockFollowed(symbol: String): Boolean
    suspend fun searchStocks(query: String): List<Stock>
    suspend fun refreshStockPrices(): Boolean
} 