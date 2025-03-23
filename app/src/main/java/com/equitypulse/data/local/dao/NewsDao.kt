package com.equitypulse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.equitypulse.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY publishDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getLatestNews(limit: Int, offset: Int): List<NewsEntity>

    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsById(id: String): NewsEntity?

    @Query("SELECT * FROM news WHERE category = :category ORDER BY publishDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getNewsByCategory(category: String, limit: Int, offset: Int): List<NewsEntity>

    @Query("SELECT * FROM news WHERE relatedStockSymbols LIKE '%' || :stockSymbol || '%' ORDER BY publishDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getNewsByStock(stockSymbol: String, limit: Int, offset: Int): List<NewsEntity>

    @Query("SELECT * FROM news WHERE isBookmarked = 1 ORDER BY publishDate DESC")
    fun getBookmarkedNews(): Flow<List<NewsEntity>>

    @Query("UPDATE news SET isBookmarked = :bookmark WHERE id = :newsId")
    suspend fun updateBookmarkStatus(newsId: String, bookmark: Boolean)

    @Query("SELECT isBookmarked FROM news WHERE id = :newsId")
    suspend fun isNewsBookmarked(newsId: String): Boolean?

    @Query("SELECT * FROM news WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' ORDER BY publishDate DESC LIMIT :limit OFFSET :offset")
    suspend fun searchNews(query: String, limit: Int, offset: Int): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(news: List<NewsEntity>)

    @Update
    suspend fun updateNews(news: NewsEntity)

    @Query("DELETE FROM news WHERE id = :id")
    suspend fun deleteNews(id: String)
} 