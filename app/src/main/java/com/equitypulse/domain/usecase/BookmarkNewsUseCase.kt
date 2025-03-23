package com.equitypulse.domain.usecase

import com.equitypulse.domain.repository.NewsRepository
import javax.inject.Inject

class BookmarkNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(newsId: String, bookmark: Boolean): Result<Boolean> {
        return try {
            val result = newsRepository.bookmarkNews(newsId, bookmark)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 