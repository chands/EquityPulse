package com.equitypulse.domain.usecase

import com.equitypulse.domain.model.News
import com.equitypulse.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Flow<List<News>> {
        return newsRepository.getLatestNews()
    }
} 