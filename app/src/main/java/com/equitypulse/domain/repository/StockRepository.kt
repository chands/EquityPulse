package com.equitypulse.domain.repository

import com.equitypulse.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getAllStocks(): Flow<List<Stock>>
    suspend fun getStockBySymbol(symbol: String): Stock?
    suspend fun getFollowedStocks(): Flow<List<Stock>>
    suspend fun followStock(symbol: String, follow: Boolean): Boolean
    suspend fun searchStocks(query: String): Flow<List<Stock>>
    suspend fun refreshStockPrices(): Boolean
} 