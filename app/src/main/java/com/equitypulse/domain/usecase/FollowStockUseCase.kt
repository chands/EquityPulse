package com.equitypulse.domain.usecase

import com.equitypulse.domain.repository.StockRepository
import javax.inject.Inject

class FollowStockUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(symbol: String, follow: Boolean): Result<Unit> {
        return try {
            stockRepository.followStock(symbol, follow)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 