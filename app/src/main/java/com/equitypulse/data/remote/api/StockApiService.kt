package com.equitypulse.data.remote.api

import com.equitypulse.data.remote.dto.StockQuoteResponse
import com.equitypulse.data.remote.dto.TimeSeriesResponse
import com.equitypulse.data.remote.dto.CompanyOverviewResponse
import com.equitypulse.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("query")
    suspend fun getStockQuote(
        @Query("function") function: String = Constants.FUNCTION_STOCK_QUOTE,
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): StockQuoteResponse

    @GET("query")
    suspend fun getTimeSeriesDaily(
        @Query("function") function: String = Constants.FUNCTION_TIME_SERIES_DAILY,
        @Query("symbol") symbol: String,
        @Query("outputsize") outputSize: String = "compact", // compact returns the latest 100 data points
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): TimeSeriesResponse

    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function: String = Constants.FUNCTION_COMPANY_OVERVIEW,
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): CompanyOverviewResponse

    @GET("query")
    suspend fun searchStocks(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): Map<String, Any>
} 