package com.equitypulse.domain.repository

import com.equitypulse.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getLatestNews(): Flow<List<News>>
    suspend fun getNewsById(id: String): News?
    suspend fun getNewsByCategory(category: String): Flow<List<News>>
    suspend fun getNewsByStock(stockSymbol: String): Flow<List<News>>
    suspend fun getBookmarkedNews(): Flow<List<News>>
    suspend fun bookmarkNews(newsId: String, bookmark: Boolean): Boolean
    suspend fun searchNews(query: String): Flow<List<News>>
} 