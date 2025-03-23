package com.equitypulse.data.remote.datasource

import com.equitypulse.data.remote.api.NewsApiService
import com.equitypulse.data.remote.dto.NewsDto
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService
) {
    suspend fun getLatestNews(limit: Int = 20, offset: Int = 0): List<NewsDto> {
        return newsApiService.getLatestNews(limit, offset)
    }

    suspend fun getNewsById(id: String): NewsDto {
        return newsApiService.getNewsById(id)
    }

    suspend fun getNewsByCategory(category: String, limit: Int = 20, offset: Int = 0): List<NewsDto> {
        return newsApiService.getNewsByCategory(category, limit, offset)
    }

    suspend fun getNewsByStock(stockSymbol: String, limit: Int = 20, offset: Int = 0): List<NewsDto> {
        return newsApiService.getNewsByStock(stockSymbol, limit, offset)
    }

    suspend fun searchNews(query: String, limit: Int = 20, offset: Int = 0): List<NewsDto> {
        return newsApiService.searchNews(query, limit, offset)
    }
} 