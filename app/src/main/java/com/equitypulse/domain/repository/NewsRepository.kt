package com.equitypulse.domain.repository

import com.equitypulse.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getLatestNews(limit: Int, offset: Int): List<News>
    suspend fun getNewsById(id: String): News?
    suspend fun getNewsByCategory(category: String, limit: Int, offset: Int): List<News>
    suspend fun getNewsByStock(stockSymbol: String, limit: Int, offset: Int): List<News>
    fun getBookmarkedNews(): Flow<List<News>>
    suspend fun bookmarkNews(newsId: String, bookmark: Boolean)
    suspend fun isNewsBookmarked(newsId: String): Boolean
    suspend fun searchNews(query: String, limit: Int, offset: Int): List<News>
} 