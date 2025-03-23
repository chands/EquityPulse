package com.equitypulse.domain.usecase

import com.equitypulse.domain.model.News
import com.equitypulse.domain.repository.NewsRepository
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0): List<News> {
        return newsRepository.getLatestNews(limit, offset)
    }
} 