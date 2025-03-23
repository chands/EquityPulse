package com.equitypulse.data.remote.dto

import com.equitypulse.data.local.entity.StockEntity
import com.google.gson.annotations.SerializedName

data class StockQuoteResponse(
    @SerializedName("Global Quote")
    val globalQuote: GlobalQuote
)

data class GlobalQuote(
    @SerializedName("01. symbol")
    val symbol: String,
    
    @SerializedName("02. open")
    val open: String,
    
    @SerializedName("03. high")
    val high: String,
    
    @SerializedName("04. low")
    val low: String,
    
    @SerializedName("05. price")
    val price: String,
    
    @SerializedName("06. volume")
    val volume: String,
    
    @SerializedName("07. latest trading day")
    val latestTradingDay: String,
    
    @SerializedName("08. previous close")
    val previousClose: String,
    
    @SerializedName("09. change")
    val change: String,
    
    @SerializedName("10. change percent")
    val changePercent: String
)

fun GlobalQuote.toEntity(name: String = symbol): StockEntity {
    val currentPrice = price.toDoubleOrNull() ?: 0.0
    val priceChange = change.toDoubleOrNull() ?: 0.0
    val priceChangePercentage = changePercent.replace("%", "").toDoubleOrNull() ?: 0.0
    
    return StockEntity(
        symbol = symbol,
        name = name, // Alpha Vantage doesn't provide name in quotes, we'll need to get it from company overview
        isFollowed = false,
        currentPrice = currentPrice,
        priceChange = priceChange,
        priceChangePercentage = priceChangePercentage,
        lastUpdated = System.currentTimeMillis()
    )
} 