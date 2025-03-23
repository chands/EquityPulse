package com.equitypulse.data.remote.dto

import com.equitypulse.data.local.entity.StockEntity
import com.google.gson.annotations.SerializedName

data class StockDto(
    @SerializedName("symbol")
    val symbol: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("price")
    val currentPrice: Double,
    
    @SerializedName("change")
    val priceChange: Double,
    
    @SerializedName("percent_change")
    val priceChangePercentage: Double,
    
    @SerializedName("last_updated")
    val lastUpdated: Long
)

fun StockDto.toEntity(isFollowed: Boolean = false): StockEntity {
    return StockEntity(
        symbol = symbol,
        name = name,
        isFollowed = isFollowed,
        currentPrice = currentPrice,
        priceChange = priceChange,
        priceChangePercentage = priceChangePercentage,
        lastUpdated = lastUpdated
    )
} 