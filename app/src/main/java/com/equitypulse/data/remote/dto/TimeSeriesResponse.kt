package com.equitypulse.data.remote.dto

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class TimeSeriesResponse(
    @SerializedName("Meta Data")
    val metaData: MetaData,
    
    @SerializedName("Time Series (Daily)")
    val timeSeriesDaily: JsonObject // Using JsonObject as the structure is dynamic with dates as keys
)

data class MetaData(
    @SerializedName("1. Information")
    val information: String,
    
    @SerializedName("2. Symbol")
    val symbol: String,
    
    @SerializedName("3. Last Refreshed")
    val lastRefreshed: String,
    
    @SerializedName("4. Output Size")
    val outputSize: String,
    
    @SerializedName("5. Time Zone")
    val timeZone: String
)

// Helper class to parse daily data points
data class DailyDataPoint(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

// Extension function to convert JsonObject to a list of DailyDataPoint
fun JsonObject.toDailyDataPoints(): List<DailyDataPoint> {
    val result = mutableListOf<DailyDataPoint>()
    
    this.keySet().forEach { date ->
        val dataObject = this.getAsJsonObject(date)
        
        val dataPoint = DailyDataPoint(
            date = date,
            open = dataObject.get("1. open").asString.toDoubleOrNull() ?: 0.0,
            high = dataObject.get("2. high").asString.toDoubleOrNull() ?: 0.0,
            low = dataObject.get("3. low").asString.toDoubleOrNull() ?: 0.0,
            close = dataObject.get("4. close").asString.toDoubleOrNull() ?: 0.0,
            volume = dataObject.get("5. volume").asString.toLongOrNull() ?: 0L
        )
        
        result.add(dataPoint)
    }
    
    return result.sortedByDescending { it.date } // Sort by date descending (newest first)
} 