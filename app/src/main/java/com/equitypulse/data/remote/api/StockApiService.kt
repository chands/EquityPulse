package com.equitypulse.data.remote.api

import com.equitypulse.data.remote.dto.StockDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApiService {
    @GET("stocks")
    suspend fun getAllStocks(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): List<StockDto>

    @GET("stocks/{symbol}")
    suspend fun getStockBySymbol(@Path("symbol") symbol: String): StockDto

    @GET("stocks/search")
    suspend fun searchStocks(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<StockDto>
} 