package com.equitypulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.equitypulse.domain.model.Stock

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey
    val symbol: String,
    val name: String,
    val isFollowed: Boolean,
    val currentPrice: Double,
    val priceChange: Double,
    val priceChangePercentage: Double,
    val lastUpdated: Long
)

fun StockEntity.toStock(): Stock {
    return Stock(
        symbol = symbol,
        name = name,
        isFollowed = isFollowed,
        currentPrice = currentPrice,
        priceChange = priceChange,
        priceChangePercentage = priceChangePercentage,
        lastUpdated = lastUpdated
    )
}

fun Stock.toEntity(): StockEntity {
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