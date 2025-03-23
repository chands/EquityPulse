package com.equitypulse.data.repository

import com.equitypulse.data.local.dao.StockDao
import com.equitypulse.data.local.entity.toDomainModel
import com.equitypulse.data.remote.datasource.StockRemoteDataSource
import com.equitypulse.data.remote.dto.toEntity
import com.equitypulse.domain.model.Stock
import com.equitypulse.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDao: StockDao,
    private val stockRemoteDataSource: StockRemoteDataSource
) : StockRepository {

    override suspend fun getAllStocks(): Flow<List<Stock>> {
        try {
            // Fetch all stocks from remote source
            val remoteStocks = stockRemoteDataSource.getAllStocks()
            
            // Get current followed status from DB to preserve it
            val currentStocks = stockDao.getAllStocks().map { it.map { entity -> entity.toDomainModel() } }
            val followedMap = mutableMapOf<String, Boolean>()
            
            // Collect the first value to get the current followed status
            currentStocks.collect { stocks ->
                stocks.forEach { stock ->
                    followedMap[stock.symbol] = stock.isFollowed
                }
            }
            
            // Save to database with preserved followed status
            stockDao.insertAllStocks(remoteStocks.map { dto ->
                dto.toEntity(followedMap[dto.symbol] ?: false)
            })
        } catch (e: Exception) {
            // If remote fetch fails, we'll still return cached data
        }
        
        // Return stocks from local database
        return stockDao.getAllStocks().map { stockList ->
            stockList.map { it.toDomainModel() }
        }
    }

    override suspend fun getStockBySymbol(symbol: String): Stock? {
        try {
            // Try to fetch from remote
            val remoteStock = stockRemoteDataSource.getStockBySymbol(symbol)
            
            // Get current followed status from DB if it exists
            val existingStock = stockDao.getStockBySymbol(symbol)
            val isFollowed = existingStock?.isFollowed ?: false
            
            // Insert with preserved followed status
            stockDao.insertStock(remoteStock.toEntity(isFollowed))
        } catch (e: Exception) {
            // If remote fetch fails, we'll use local data
        }
        
        // Return from local database
        return stockDao.getStockBySymbol(symbol)?.toDomainModel()
    }

    override suspend fun getFollowedStocks(): Flow<List<Stock>> {
        // First refresh all followed stocks
        try {
            // Get all followed stocks symbols
            val followedStocksFlow = stockDao.getFollowedStocks()
            val symbols = mutableListOf<String>()
            
            // Collect the first value to get the symbols
            followedStocksFlow.collect { stocks ->
                symbols.addAll(stocks.map { it.symbol })
            }
            
            // Fetch updated data for each followed stock
            symbols.forEach { symbol ->
                try {
                    val remoteStock = stockRemoteDataSource.getStockBySymbol(symbol)
                    stockDao.insertStock(remoteStock.toEntity(true))
                } catch (e: Exception) {
                    // Ignore errors for individual stocks
                }
            }
        } catch (e: Exception) {
            // If refresh fails, we'll still return cached data
        }
        
        // Return followed stocks from local database
        return stockDao.getFollowedStocks().map { stockList ->
            stockList.map { it.toDomainModel() }
        }
    }

    override suspend fun followStock(symbol: String, follow: Boolean): Boolean {
        return stockDao.followStock(symbol, follow) > 0
    }

    override suspend fun searchStocks(query: String): Flow<List<Stock>> {
        try {
            // Search stocks from remote source
            val remoteStocks = stockRemoteDataSource.searchStocks(query)
            
            // Get current followed status to preserve it
            val followedStocksFlow = stockDao.getFollowedStocks()
            val followedSymbols = mutableSetOf<String>()
            
            // Collect the first value to get the followed symbols
            followedStocksFlow.collect { stocks ->
                followedSymbols.addAll(stocks.map { it.symbol })
            }
            
            // Save to database with preserved followed status
            stockDao.insertAllStocks(remoteStocks.map { dto ->
                dto.toEntity(followedSymbols.contains(dto.symbol))
            })
        } catch (e: Exception) {
            // If remote fetch fails, we'll still return cached data
        }
        
        // Return search results from local database
        return stockDao.searchStocks(query).map { stockList ->
            stockList.map { it.toDomainModel() }
        }
    }

    override suspend fun refreshStockPrices(): Boolean {
        try {
            // Get all stocks from database
            val allStocksFlow = stockDao.getAllStocks()
            val stocks = mutableListOf<Stock>()
            
            // Collect the first value
            allStocksFlow.collect { stockList ->
                stocks.addAll(stockList.map { it.toDomainModel() })
            }
            
            // Refresh each stock
            stocks.forEach { stock ->
                try {
                    val remoteStock = stockRemoteDataSource.getStockBySymbol(stock.symbol)
                    stockDao.insertStock(remoteStock.toEntity(stock.isFollowed))
                } catch (e: Exception) {
                    // Ignore errors for individual stocks
                }
            }
            
            return true
        } catch (e: Exception) {
            return false
        }
    }
} 