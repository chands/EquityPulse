package com.equitypulse.domain.usecase

import com.equitypulse.domain.repository.NewsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class BookmarkNewsUseCaseTest {

    @Mock
    private lateinit var newsRepository: NewsRepository

    private lateinit var bookmarkNewsUseCase: BookmarkNewsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        bookmarkNewsUseCase = BookmarkNewsUseCase(newsRepository)
    }

    @Test
    fun `test bookmark news returns success when repository succeeds`() = runBlocking {
        // Given: a news ID and bookmark status
        val newsId = "1"
        val bookmark = true
        
        // And: a repository that successfully bookmarks the news
        `when`(newsRepository.bookmarkNews(newsId, bookmark)).thenReturn(true)
        
        // When: the use case is invoked
        val result = bookmarkNewsUseCase(newsId, bookmark)
        
        // Then: the result should be a success and contain true
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
        
        // And: the repository method should be called with the correct parameters
        verify(newsRepository).bookmarkNews(newsId, bookmark)
    }

    @Test
    fun `test bookmark news returns failure when repository throws exception`() = runBlocking {
        // Given: a news ID and bookmark status
        val newsId = "1"
        val bookmark = true
        
        // And: a repository that throws an exception
        val expectedException = RuntimeException("Failed to bookmark news")
        `when`(newsRepository.bookmarkNews(newsId, bookmark)).thenThrow(expectedException)
        
        // When: the use case is invoked
        val result = bookmarkNewsUseCase(newsId, bookmark)
        
        // Then: the result should be a failure with the expected exception
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
        
        // And: the repository method should be called with the correct parameters
        verify(newsRepository).bookmarkNews(newsId, bookmark)
    }
} 