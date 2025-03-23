package com.equitypulse.util

object Constants {
    // Base URLs (you'll need to replace these with actual API endpoints)
    const val BASE_URL = "https://api.example.com/"
    
    // API Keys (store securely in production)
    const val API_KEY = "YOUR_API_KEY"
    
    // Database
    const val DATABASE_VERSION = 1
    
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