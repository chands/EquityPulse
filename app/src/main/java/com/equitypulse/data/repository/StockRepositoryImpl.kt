package com.equitypulse.data.repository

import com.equitypulse.data.local.dao.StockDao
import com.equitypulse.data.local.entity.StockEntity
import com.equitypulse.data.local.entity.toStock
import com.equitypulse.data.remote.api.StockApiService
import com.equitypulse.data.remote.dto.toEntity
import com.equitypulse.domain.model.Stock
import com.equitypulse.domain.repository.StockRepository
import com.equitypulse.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDao: StockDao,
    private val stockApiService: StockApiService
) : StockRepository {
    
    override suspend fun getAllStocks(): List<Stock> {
        try {
            // For Alpha Vantage Free API, we'll use a list of popular stock symbols
            // since we can't fetch all stocks at once
            val popularSymbols = listOf("AAPL", "MSFT", "GOOGL", "AMZN", "META", "TSLA", "NVDA", "JPM", "V", "WMT")
            
            val stockEntities = mutableListOf<StockEntity>()
            
            for (symbol in popularSymbols) {
                try {
                    // Get quote for basic price info
                    val quoteResponse = stockApiService.getStockQuote(symbol = symbol)
                    
                    // Get company overview for additional info
                    val overviewResponse = stockApiService.getCompanyOverview(symbol = symbol)
                    
                    // Combine data from both responses
                    val stockEntity = overviewResponse.toEntity(
                        currentPrice = quoteResponse.globalQuote.price.toDoubleOrNull() ?: 0.0,
                        priceChange = quoteResponse.globalQuote.change.toDoubleOrNull() ?: 0.0,
                        priceChangePercentage = quoteResponse.globalQuote.changePercent.replace("%", "").toDoubleOrNull() ?: 0.0,
                        lastUpdated = System.currentTimeMillis()
                    )
                    
                    stockEntities.add(stockEntity)
                    
                    // Store in local database
                    stockDao.insertStock(stockEntity)
                } catch (e: Exception) {
                    // If API call fails for a stock, continue with the next one
                    continue
                }
            }
            
            return stockEntities.map { it.toStock() }
        } catch (e: Exception) {
            // Return stocks from local database if API fails
            val entities = stockDao.getAllStocks().firstOrNull() ?: emptyList()
            return entities.map { it.toStock() }
        }
    }
    
    override suspend fun getStockBySymbol(symbol: String): Stock? {
        try {
            // Try to fetch fresh data
            val quoteResponse = stockApiService.getStockQuote(symbol = symbol)
            val overviewResponse = stockApiService.getCompanyOverview(symbol = symbol)
            
            val stockEntity = overviewResponse.toEntity(
                currentPrice = quoteResponse.globalQuote.price.toDoubleOrNull() ?: 0.0,
                priceChange = quoteResponse.globalQuote.change.toDoubleOrNull() ?: 0.0,
                priceChangePercentage = quoteResponse.globalQuote.changePercent.replace("%", "").toDoubleOrNull() ?: 0.0,
                lastUpdated = System.currentTimeMillis()
            )
            
            stockDao.insertStock(stockEntity)
            
            return stockEntity.toStock()
        } catch (e: Exception) {
            // Return from local database if API fails
            return stockDao.getStockBySymbol(symbol)?.toStock()
        }
    }
    
    override suspend fun searchStocks(query: String): List<Stock> {
        try {
            val searchResponse = stockApiService.searchStocks(keywords = query)
            
            // Parse the search results
            val matches = searchResponse["bestMatches"] as? List<Map<String, String>> ?: emptyList()
            
            val stockEntities = matches.mapNotNull { match ->
                val symbol = match["1. symbol"] ?: return@mapNotNull null
                val name = match["2. name"] ?: return@mapNotNull null
                
                StockEntity(
                    symbol = symbol,
                    name = name,
                    currentPrice = 0.0, // We'll need to fetch quotes separately to get current prices
                    priceChange = 0.0,
                    priceChangePercentage = 0.0,
                    lastUpdated = System.currentTimeMillis(),
                    isFollowed = false
                )
            }
            
            // Store search results in local DB
            stockEntities.forEach { stockDao.insertStock(it) }
            
            return stockEntities.map { it.toStock() }
        } catch (e: Exception) {
            // Perform a local search if API fails
            val entities = stockDao.searchStocks(query).firstOrNull() ?: emptyList()
            return entities.map { it.toStock() }
        }
    }
    
    override fun getFollowedStocks(): Flow<List<Stock>> {
        return stockDao.getFollowedStocks().map { entities ->
            entities.map { it.toStock() }
        }
    }
    
    override suspend fun followStock(symbol: String, follow: Boolean) {
        stockDao.updateFollowStatus(symbol, follow)
    }
    
    override suspend fun isStockFollowed(symbol: String): Boolean {
        return stockDao.isStockFollowed(symbol) ?: false
    }
    
    override suspend fun refreshStockPrices(): Boolean {
        try {
            // Get all stocks from local database
            val stocks = stockDao.getAllStocks().firstOrNull() ?: return false
            
            for (stock in stocks) {
                try {
                    // Update price for each stock
                    val quoteResponse = stockApiService.getStockQuote(symbol = stock.symbol)
                    val currentPrice = quoteResponse.globalQuote.price.toDoubleOrNull() ?: continue
                    val priceChange = quoteResponse.globalQuote.change.toDoubleOrNull() ?: continue
                    val priceChangePercentage = quoteResponse.globalQuote.changePercent.replace("%", "").toDoubleOrNull() ?: continue
                    
                    stockDao.updateStockPrice(
                        symbol = stock.symbol,
                        price = currentPrice,
                        change = priceChange,
                        changePercent = priceChangePercentage,
                        lastUpdated = System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    // If updating one stock fails, continue with others
                    continue
                }
            }
            
            return true
        } catch (e: Exception) {
            return false
        }
    }
} 