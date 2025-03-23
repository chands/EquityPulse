package com.equitypulse.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equitypulse.domain.model.News
import com.equitypulse.domain.repository.NewsRepository
import com.equitypulse.domain.usecase.BookmarkNewsUseCase
import com.equitypulse.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val bookmarkNewsUseCase: BookmarkNewsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(NewsDetailState())
    val state: StateFlow<NewsDetailState> = _state.asStateFlow()
    
    fun getNewsById(newsId: String) {
        viewModelScope.launch {
            _state.value = NewsDetailState(isLoading = true)
            
            try {
                val news = newsRepository.getNewsById(newsId)
                if (news != null) {
                    _state.value = NewsDetailState(news = news)
                } else {
                    _state.value = NewsDetailState(error = "News not found")
                }
            } catch (e: Exception) {
                _state.value = NewsDetailState(error = e.message ?: Constants.UNKNOWN_ERROR)
            }
        }
    }
    
    fun toggleBookmark(newsId: String) {
        viewModelScope.launch {
            val currentNews = state.value.news
            if (currentNews != null) {
                val isCurrentlyBookmarked = currentNews.isBookmarked
                bookmarkNewsUseCase(newsId, !isCurrentlyBookmarked)
                
                // Update the UI state with the new bookmarked status
                _state.value = state.value.copy(
                    news = currentNews.copy(isBookmarked = !isCurrentlyBookmarked)
                )
            }
        }
    }
    
    fun shareNews() {
        // Implement sharing functionality
        // This would typically involve launching an Intent in Android,
        // but for now we'll just update a flag in the state
        _state.value = state.value.copy(shareNewsEvent = true)
    }
    
    fun shareNewsHandled() {
        _state.value = state.value.copy(shareNewsEvent = false)
    }
    
    fun openFullStory() {
        // This would typically involve launching a browser Intent
        // For now, just update a flag in the state
        _state.value = state.value.copy(openFullStoryEvent = true)
    }
    
    fun openFullStoryHandled() {
        _state.value = state.value.copy(openFullStoryEvent = false)
    }
}

data class NewsDetailState(
    val isLoading: Boolean = false,
    val news: News? = null,
    val error: String? = null,
    val shareNewsEvent: Boolean = false,
    val openFullStoryEvent: Boolean = false
)
