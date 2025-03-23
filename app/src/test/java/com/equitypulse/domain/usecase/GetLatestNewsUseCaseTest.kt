package com.equitypulse.domain.usecase

import com.equitypulse.domain.model.News
import com.equitypulse.domain.repository.NewsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetLatestNewsUseCaseTest {

    @Mock
    private lateinit var newsRepository: NewsRepository

    private lateinit var getLatestNewsUseCase: GetLatestNewsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getLatestNewsUseCase = GetLatestNewsUseCase(newsRepository)
    }

    @Test
    fun `test get latest news returns news from repository`() = runBlocking {
        // Given: a repository that returns a list of news
        val expectedNews = listOf(
            News(
                id = "1",
                title = "Test News",
                content = "This is a test news article about stocks",
                originalUrl = "https://example.com/news/1",
                imageUrl = "https://example.com/images/1.jpg",
                publishDate = System.currentTimeMillis(),
                source = "Test Source",
                relatedStockSymbols = listOf("AAPL", "GOOG"),
                category = "Technology",
                isBookmarked = false
            )
        )
        
        `when`(newsRepository.getLatestNews()).thenReturn(flowOf(expectedNews))
        
        // When: the use case is invoked
        val actualNews = getLatestNewsUseCase().first()
        
        // Then: the returned news should match the expected news
        assertEquals(expectedNews, actualNews)
    }
} 