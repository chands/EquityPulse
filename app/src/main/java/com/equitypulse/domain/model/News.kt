package com.equitypulse.domain.model

data class News(
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val originalUrl: String,
    val imageUrl: String,
    val publishDate: String, // Changed from Long to String to accommodate Alpha Vantage format
    val publishedAt: String, // Formatted date string for display
    val source: String,
    val relatedStockSymbols: List<String>,
    val category: String,
    val isBookmarked: Boolean
) 