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
    val publishDate: String, // Changed from Long to String to accommodate Alpha Vantage format
    val source: String,
    val relatedStockSymbols: String, // Stored as comma-separated
    val category: String,
    val isBookmarked: Boolean
)

fun NewsEntity.toNews(): News {
    // Parse the Alpha Vantage date format (e.g., "20230615T123000")
    val formattedDate = try {
        val inputFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(publishDate)
        date?.let { outputFormat.format(it) } ?: publishDate
    } catch (e: Exception) {
        // Fallback to original string if parsing fails
        publishDate
    }
    
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