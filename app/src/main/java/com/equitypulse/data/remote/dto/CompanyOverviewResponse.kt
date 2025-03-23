package com.equitypulse.data.remote.dto

import com.equitypulse.data.local.entity.StockEntity
import com.google.gson.annotations.SerializedName

data class CompanyOverviewResponse(
    @SerializedName("Symbol")
    val symbol: String,
    
    @SerializedName("Name")
    val name: String,
    
    @SerializedName("Description")
    val description: String,
    
    @SerializedName("Exchange")
    val exchange: String,
    
    @SerializedName("Currency")
    val currency: String,
    
    @SerializedName("Country")
    val country: String,
    
    @SerializedName("Sector")
    val sector: String,
    
    @SerializedName("Industry")
    val industry: String,
    
    @SerializedName("Address")
    val address: String,
    
    @SerializedName("FiscalYearEnd")
    val fiscalYearEnd: String,
    
    @SerializedName("LatestQuarter")
    val latestQuarter: String,
    
    @SerializedName("MarketCapitalization")
    val marketCapitalization: String,
    
    @SerializedName("EBITDA")
    val ebitda: String,
    
    @SerializedName("PERatio")
    val peRatio: String,
    
    @SerializedName("PEGRatio")
    val pegRatio: String,
    
    @SerializedName("BookValue")
    val bookValue: String,
    
    @SerializedName("DividendPerShare")
    val dividendPerShare: String,
    
    @SerializedName("DividendYield")
    val dividendYield: String,
    
    @SerializedName("EPS")
    val eps: String,
    
    @SerializedName("52WeekHigh")
    val fiftyTwoWeekHigh: String,
    
    @SerializedName("52WeekLow")
    val fiftyTwoWeekLow: String
)

fun CompanyOverviewResponse.toEntity(currentPrice: Double = 0.0, priceChange: Double = 0.0, priceChangePercentage: Double = 0.0, lastUpdated: Long = System.currentTimeMillis()): StockEntity {
    return StockEntity(
        symbol = symbol,
        name = name,
        isFollowed = false,
        currentPrice = currentPrice,
        priceChange = priceChange,
        priceChangePercentage = priceChangePercentage,
        lastUpdated = lastUpdated
    )
} 