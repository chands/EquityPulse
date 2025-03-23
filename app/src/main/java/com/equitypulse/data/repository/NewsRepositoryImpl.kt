package com.equitypulse.data.repository

import android.util.Log
import com.equitypulse.data.local.dao.NewsDao
import com.equitypulse.data.local.entity.toNews
import com.equitypulse.data.remote.api.NewsApiService
import com.equitypulse.data.remote.dto.NewsDto
import com.equitypulse.data.remote.dto.toEntity
import com.equitypulse.domain.model.News
import com.equitypulse.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService
) : NewsRepository {
    
    override suspend fun getLatestNews(limit: Int, offset: Int): List<News> {
        try {
            // Fetch latest news from Alpha Vantage API
            val response = newsApiService.getNewsSentiment()
            Log.d("NewsRepository", "API response: ${response.items} items")
            
            // Log feed details
            response.feed.forEachIndexed { index, newsDto ->
                Log.d("NewsRepository", "News $index: ${newsDto.title}")
            }
            
            if (response.feed.isEmpty()) {
                Log.w("NewsRepository", "API returned empty feed")
                // Return data from database as fallback
                val dbNews = newsDao.getLatestNews(limit, offset)
                Log.d("NewsRepository", "Database has ${dbNews.size} news items")
                return dbNews.map { it.toNews() }
            }
            
            // Convert to entities and save to database
            val newsEntities = response.feed.map { it.toEntity() }
            Log.d("NewsRepository", "Converted ${newsEntities.size} news entities")
            
            // Insert each item individually to avoid SQLite issues
            newsEntities.forEach { 
                try {
                    newsDao.insertNews(it)
                } catch (e: Exception) {
                    Log.e("NewsRepository", "Error inserting news: ${it.title}", e)
                }
            }
            
            // Return latest news from database
            val result = newsDao.getLatestNews(limit, offset).map { it.toNews() }
            Log.d("NewsRepository", "Returning ${result.size} news items")
            return result
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error fetching news from API", e)
            // If API call fails, return data from local database
            val dbNews = newsDao.getLatestNews(limit, offset)
            Log.d("NewsRepository", "Returning ${dbNews.size} news items from database after error")
            return dbNews.map { it.toNews() }
        }
    }
    
    override suspend fun getNewsById(id: String): News? {
        return newsDao.getNewsById(id)?.toNews()
    }
    
    override suspend fun getNewsByCategory(category: String, limit: Int, offset: Int): List<News> {
        try {
            // For categories, we'll use topics parameter in Alpha Vantage
            val topics = mapCategoryToTopics(category)
            val response = newsApiService.getNewsSentimentWithTopics(topics = topics)
            
            // Convert to entities and save to database
            val newsEntities = response.feed.map { it.toEntity() }
            newsEntities.forEach { newsDao.insertNews(it) }
            
            // Return news by category from database
            return newsDao.getNewsByCategory(category, limit, offset).map { it.toNews() }
        } catch (e: Exception) {
            // If API call fails, return data from local database
            return newsDao.getNewsByCategory(category, limit, offset).map { it.toNews() }
        }
    }
    
    override suspend fun getNewsByStock(stockSymbol: String, limit: Int, offset: Int): List<News> {
        try {
            // Fetch news for specific stock ticker
            val response = newsApiService.getNewsSentimentForSymbol(tickers = stockSymbol)
            
            // Convert to entities and save to database
            val newsEntities = response.feed.map { it.toEntity() }
            newsEntities.forEach { newsDao.insertNews(it) }
            
            // Return news for stock from database
            return newsDao.getNewsByStock(stockSymbol, limit, offset).map { it.toNews() }
        } catch (e: Exception) {
            // If API call fails, return data from local database
            return newsDao.getNewsByStock(stockSymbol, limit, offset).map { it.toNews() }
        }
    }
    
    override suspend fun searchNews(query: String, limit: Int, offset: Int): List<News> {
        // Alpha Vantage doesn't have a direct search endpoint for news
        // We'll fetch all news and filter locally
        try {
            val response = newsApiService.getNewsSentiment()
            
            // Filter news items that contain the query in title or summary
            val filteredNews = response.feed.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.summary.contains(query, ignoreCase = true) 
            }
            
            // Convert to entities and save to database
            val newsEntities = filteredNews.map { it.toEntity() }
            newsEntities.forEach { newsDao.insertNews(it) }
            
            // Return search results from database
            return newsDao.searchNews(query, limit, offset).map { it.toNews() }
        } catch (e: Exception) {
            // If API call fails, return data from local database
            return newsDao.searchNews(query, limit, offset).map { it.toNews() }
        }
    }
    
    override fun getBookmarkedNews(): Flow<List<News>> {
        return newsDao.getBookmarkedNews().map { entities ->
            entities.map { it.toNews() }
        }
    }
    
    override suspend fun bookmarkNews(newsId: String, bookmark: Boolean) {
        newsDao.updateBookmarkStatus(newsId, bookmark)
    }
    
    override suspend fun isNewsBookmarked(newsId: String): Boolean {
        return newsDao.isNewsBookmarked(newsId) ?: false
    }
    
    // Helper function to map app categories to Alpha Vantage topics
    private fun mapCategoryToTopics(category: String): String {
        return when (category.lowercase()) {
            "technology" -> "technology"
            "finance" -> "finance,financial_markets"
            "economy" -> "economy_fiscal,economy_monetary,economy_macro"
            "markets" -> "financial_markets"
            "crypto" -> "blockchain"
            "ipo" -> "ipo"
            "mergers" -> "mergers_and_acquisitions"
            "energy" -> "energy_transportation"
            "healthcare" -> "life_sciences"
            "business" -> "manufacturing,retail_wholesale"
            "real estate" -> "real_estate"
            else -> "economy_macro,financial_markets,technology,finance"
        }
    }
} 