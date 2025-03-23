package com.equitypulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.equitypulse.domain.model.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val originalUrl: String,
    val imageUrl: String,
    val publishDate: Long,
    val source: String,
    val relatedStockSymbols: String, // Stored as comma-separated
    val category: String,
    val isBookmarked: Boolean
)

fun NewsEntity.toDomainModel(): News {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(publishDate))
    
    return News(
        id = id,
        title = title,
        summary = summary,
        content = content,
        originalUrl = originalUrl,
        imageUrl = imageUrl,
        publishDate = publishDate,
        publishedAt = formattedDate,
        source = source,
        relatedStockSymbols = relatedStockSymbols.split(",").filter { it.isNotBlank() },
        category = category,
        isBookmarked = isBookmarked
    )
}

fun News.toEntity(): NewsEntity {
    return NewsEntity(
        id = id,
        title = title,
        summary = summary,
        content = content,
        originalUrl = originalUrl,
        imageUrl = imageUrl,
        publishDate = publishDate,
        source = source,
        relatedStockSymbols = relatedStockSymbols.joinToString(","),
        category = category,
        isBookmarked = isBookmarked
    )
} 