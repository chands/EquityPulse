package com.equitypulse.data.repository

import com.equitypulse.data.local.dao.NewsDao
import com.equitypulse.data.local.entity.toEntity
import com.equitypulse.data.local.entity.toDomainModel
import com.equitypulse.data.remote.datasource.NewsRemoteDataSource
import com.equitypulse.data.remote.dto.toEntity
import com.equitypulse.domain.model.News
import com.equitypulse.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsRemoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    override suspend fun getLatestNews(): Flow<List<News>> {
        try {
            // Fetch latest news from remote source
            val remoteNews = newsRemoteDataSource.getLatestNews()
            
            // Save to database
            newsDao.insertAllNews(remoteNews.map { it.toEntity() })
        } catch (e: Exception) {
            // If remote fetch fails, we'll still return cached data
        }
        
        // Return news from local database
        return newsDao.getAllNews().map { newsList ->
            newsList.map { it.toDomainModel() }
        }
    }

    override suspend fun getNewsById(id: String): News? {
        try {
            // Try to fetch from remote
            val remoteNews = newsRemoteDataSource.getNewsById(id)
            newsDao.insertNews(remoteNews.toEntity())
        } catch (e: Exception) {
            // If remote fetch fails, we'll use local data
        }
        
        // Return from local database
        return newsDao.getNewsById(id)?.toDomainModel()
    }

    override suspend fun getNewsByCategory(category: String): Flow<List<News>> {
        try {
            // Fetch category news from remote source
            val remoteNews = newsRemoteDataSource.getNewsByCategory(category)
            
            // Save to database
            newsDao.insertAllNews(remoteNews.map { it.toEntity() })
        } catch (e: Exception) {
            // If remote fetch fails, we'll still return cached data
        }
        
        // Return news from local database
        return newsDao.getNewsByCategory(category).map { newsList ->
            newsList.map { it.toDomainModel() }
        }
    }

    override suspend fun getNewsByStock(stockSymbol: String): Flow<List<News>> {
        try {
            // Fetch stock news from remote source
            val remoteNews = newsRemoteDataSource.getNewsByStock(stockSymbol)
            
            // Save to database
            newsDao.insertAllNews(remoteNews.map { it.toEntity() })
        } catch (e: Exception) {
            // If remote fetch fails, we'll still return cached data
        }
        
        // Return news from local database
        return newsDao.getNewsByStock(stockSymbol).map { newsList ->
            newsList.map { it.toDomainModel() }
        }
    }

    override suspend fun getBookmarkedNews(): Flow<List<News>> {
        // Bookmarked news is only stored locally
        return newsDao.getBookmarkedNews().map { newsList ->
            newsList.map { it.toDomainModel() }
        }
    }

    override suspend fun bookmarkNews(newsId: String, bookmark: Boolean): Boolean {
        return newsDao.bookmarkNews(newsId, bookmark) > 0
    }

    override suspend fun searchNews(query: String): Flow<List<News>> {
        try {
            // Search news from remote source
            val remoteNews = newsRemoteDataSource.searchNews(query)
            
            // Save to database
            newsDao.insertAllNews(remoteNews.map { it.toEntity() })
        } catch (e: Exception) {
            // If remote fetch fails, we'll still return cached data
        }
        
        // Return search results from local database
        return newsDao.searchNews(query).map { newsList ->
            newsList.map { it.toDomainModel() }
        }
    }
} 