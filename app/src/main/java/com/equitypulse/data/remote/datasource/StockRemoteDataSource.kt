package com.equitypulse.data.remote.datasource

import com.equitypulse.data.remote.api.StockApiService
import com.equitypulse.data.remote.dto.StockDto
import javax.inject.Inject

class StockRemoteDataSource @Inject constructor(
    private val stockApiService: StockApiService
) {
    suspend fun getAllStocks(limit: Int = 100, offset: Int = 0): List<StockDto> {
        return stockApiService.getAllStocks(limit, offset)
    }

    suspend fun getStockBySymbol(symbol: String): StockDto {
        return stockApiService.getStockBySymbol(symbol)
    }

    suspend fun searchStocks(query: String, limit: Int = 20, offset: Int = 0): List<StockDto> {
        return stockApiService.searchStocks(query, limit, offset)
    }
} 