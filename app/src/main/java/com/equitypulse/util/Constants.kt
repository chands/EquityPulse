package com.equitypulse.util

object Constants {
    const val BASE_URL = "https://www.alphavantage.co/"
    const val API_KEY = "P901XKSWZKVICW5T"
    
    // Alpha Vantage API Functions
    const val FUNCTION_STOCK_QUOTE = "GLOBAL_QUOTE"
    const val FUNCTION_TIME_SERIES_DAILY = "TIME_SERIES_DAILY"
    const val FUNCTION_COMPANY_OVERVIEW = "OVERVIEW"
    const val FUNCTION_NEWS_SENTIMENT = "NEWS_SENTIMENT"
    const val FUNCTION_EARNINGS = "EARNINGS"
    
    // Database
    const val DATABASE_VERSION = 2
    
    // Network
    const val TIMEOUT_SECONDS = 30L
    
    // Error messages
    const val UNKNOWN_ERROR = "An unknown error occurred"
    const val NETWORK_ERROR = "Network error. Please check your connection"
    const val SERVER_ERROR = "Server error. Please try again later"
    
    // UI
    const val MAX_NEWS_SUMMARY_WORDS = 60
    
    // Feature flags
    const val ENABLE_REALTIME_ANALYSIS = false
} 