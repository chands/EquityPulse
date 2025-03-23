package com.equitypulse.domain.model

data class Stock(
    val symbol: String,
    val name: String,
    val isFollowed: Boolean,
    val currentPrice: Double,
    val priceChange: Double,
    val priceChangePercentage: Double,
    val lastUpdated: Long
) 