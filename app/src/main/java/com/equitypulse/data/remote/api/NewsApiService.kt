package com.equitypulse.data.remote.api

import com.equitypulse.data.remote.dto.NewsDto
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
} 