package com.equitypulse.data.remote.dto

import com.equitypulse.data.local.entity.NewsEntity
import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("summary")
    val summary: String,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("url")
    val originalUrl: String,
    
    @SerializedName("image_url")
    val imageUrl: String,
    
    @SerializedName("publish_date")
    val publishDate: Long,
    
    @SerializedName("source")
    val source: String,
    
    @SerializedName("related_stocks")
    val relatedStockSymbols: List<String>,
    
    @SerializedName("category")
    val category: String
)

fun NewsDto.toEntity(): NewsEntity {
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
        isBookmarked = false
    )
} 