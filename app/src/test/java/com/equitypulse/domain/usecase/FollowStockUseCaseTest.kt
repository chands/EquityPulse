package com.equitypulse.domain.usecase

import com.equitypulse.domain.repository.StockRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FollowStockUseCaseTest {

    @Mock
    private lateinit var stockRepository: StockRepository

    private lateinit var followStockUseCase: FollowStockUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        followStockUseCase = FollowStockUseCase(stockRepository)
    }

    @Test
    fun `test follow stock returns success when repository succeeds`() = runBlocking {
        // Given: a stock symbol and follow status
        val symbol = "AAPL"
        val follow = true
        
        // And: a repository that successfully follows the stock
        `when`(stockRepository.followStock(symbol, follow)).thenReturn(true)
        
        // When: the use case is invoked
        val result = followStockUseCase(symbol, follow)
        
        // Then: the result should be a success and contain true
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
        
        // And: the repository method should be called with the correct parameters
        verify(stockRepository).followStock(symbol, follow)
    }

    @Test
    fun `test follow stock returns failure when repository throws exception`() = runBlocking {
        // Given: a stock symbol and follow status
        val symbol = "AAPL"
        val follow = true
        
        // And: a repository that throws an exception
        val expectedException = RuntimeException("Failed to follow stock")
        `when`(stockRepository.followStock(symbol, follow)).thenThrow(expectedException)
        
        // When: the use case is invoked
        val result = followStockUseCase(symbol, follow)
        
        // Then: the result should be a failure with the expected exception
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
        
        // And: the repository method should be called with the correct parameters
        verify(stockRepository).followStock(symbol, follow)
    }
} 