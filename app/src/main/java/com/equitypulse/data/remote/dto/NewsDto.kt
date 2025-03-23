package com.equitypulse.data.remote.dto

import com.equitypulse.data.local.entity.NewsEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class NewsDto(
    @SerializedName("title") val title: String,
    @SerializedName("url") val originalUrl: String,
    @SerializedName("time_published") val publishDate: String,
    @SerializedName("authors") val authors: List<String>,
    @SerializedName("summary") val summary: String,
    @SerializedName("banner_image") val imageUrl: String? = null,
    @SerializedName("source_domain") val sourceDomain: String? = null,
    @SerializedName("topics") val topics: List<TopicDto>? = null,
    @SerializedName("ticker_sentiment") val tickerSentiment: List<TickerSentimentDto>? = null,
    @SerializedName("overall_sentiment_score") val overallSentimentScore: Double? = null,
    @SerializedName("overall_sentiment_label") val overallSentimentLabel: String? = null
)

data class TopicDto(
    @SerializedName("topic") val topic: String,
    @SerializedName("relevance_score") val relevanceScore: String
)

data class TickerSentimentDto(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("relevance_score") val relevanceScore: String,
    @SerializedName("ticker_sentiment_score") val sentimentScore: String,
    @SerializedName("ticker_sentiment_label") val sentimentLabel: String
)

data class NewsResponse(
    @SerializedName("feed") val feed: List<NewsDto>,
    @SerializedName("items") val items: String
)

fun NewsDto.toEntity(): NewsEntity {
    val id = UUID.randomUUID().toString()
    val category = when {
        topics.isNullOrEmpty() -> "General"
        else -> determineCategory(topics)
    }
    
    val relatedStocks = tickerSentiment?.map { it.ticker }?.joinToString(",") ?: ""
    val source = authors.joinToString(", ") 
    
    return NewsEntity(
        id = id,
        title = title,
        content = summary,
        summary = summary,
        originalUrl = originalUrl,
        imageUrl = imageUrl ?: "",
        publishDate = publishDate,
        source = source,
        relatedStockSymbols = relatedStocks,
        category = category,
        isBookmarked = false
    )
}

private fun determineCategory(topics: List<TopicDto>): String {
    // Sort topics by relevance score and get the most relevant one
    val mostRelevantTopic = topics.sortedByDescending { it.relevanceScore.toDoubleOrNull() ?: 0.0 }.firstOrNull()?.topic ?: return "General"
    
    return when {
        mostRelevantTopic.contains("technology", ignoreCase = true) -> "Technology"
        mostRelevantTopic.contains("finance", ignoreCase = true) -> "Finance"
        mostRelevantTopic.contains("economy", ignoreCase = true) -> "Economy"
        mostRelevantTopic.contains("markets", ignoreCase = true) -> "Markets"
        mostRelevantTopic.contains("blockchain", ignoreCase = true) -> "Crypto"
        mostRelevantTopic.contains("ipo", ignoreCase = true) -> "IPO"
        mostRelevantTopic.contains("merger", ignoreCase = true) -> "Mergers"
        mostRelevantTopic.contains("energy", ignoreCase = true) -> "Energy"
        mostRelevantTopic.contains("science", ignoreCase = true) -> "Healthcare"
        mostRelevantTopic.contains("manufacturing", ignoreCase = true) -> "Business"
        mostRelevantTopic.contains("real_estate", ignoreCase = true) -> "Real Estate"
        else -> "General"
    }
} 