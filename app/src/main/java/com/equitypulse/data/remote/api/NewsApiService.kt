package com.equitypulse.data.remote.api

import com.equitypulse.data.remote.dto.NewsDto
import com.equitypulse.data.remote.dto.NewsSentimentResponse
import com.equitypulse.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("news/latest")
    suspend fun getLatestNews(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<NewsDto>

    @GET("news/{id}")
    suspend fun getNewsById(@Path("id") id: String): NewsDto

    @GET("news/category/{category}")
    suspend fun getNewsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<NewsDto>

    @GET("news/stock/{symbol}")
    suspend fun getNewsByStock(
        @Path("symbol") stockSymbol: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<NewsDto>

    @GET("news/search")
    suspend fun searchNews(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<NewsDto>

    @GET("query")
    suspend fun getNewsSentiment(
        @Query("function") function: String = Constants.FUNCTION_NEWS_SENTIMENT,
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): NewsSentimentResponse

    @GET("query")
    suspend fun getNewsSentimentForSymbol(
        @Query("function") function: String = Constants.FUNCTION_NEWS_SENTIMENT,
        @Query("tickers") tickers: String,
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): NewsSentimentResponse

    @GET("query")
    suspend fun getNewsSentimentWithTopics(
        @Query("function") function: String = Constants.FUNCTION_NEWS_SENTIMENT,
        @Query("topics") topics: String, // Comma separated list of topics: blockchain,earnings,ipo,mergers_and_acquisitions,financial_markets,economy_fiscal,economy_monetary,economy_macro,energy_transportation,finance,life_sciences,manufacturing,real_estate,retail_wholesale,technology
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): NewsSentimentResponse

    @GET("query")
    suspend fun getNewsSentimentWithTimeRange(
        @Query("function") function: String = Constants.FUNCTION_NEWS_SENTIMENT,
        @Query("time_from") timeFrom: String, // Format: YYYYMMDDTHHMM, e.g., 202201011200
        @Query("time_to") timeTo: String, // Format: YYYYMMDDTHHMM
        @Query("limit") limit: Int = 50,
        @Query("apikey") apiKey: String = Constants.API_KEY
    ): NewsSentimentResponse
} 