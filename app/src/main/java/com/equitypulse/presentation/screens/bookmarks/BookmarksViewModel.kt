package com.equitypulse.presentation.screens.bookmarks

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

class BookmarksViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val bookmarkNewsUseCase: BookmarkNewsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(BookmarksState())
    val state: StateFlow<BookmarksState> = _state.asStateFlow()
    
    init {
        getBookmarkedNews()
    }
    
    fun getBookmarkedNews() {
        viewModelScope.launch {
            _state.value = BookmarksState(isLoading = true)
            
            newsRepository.getBookmarkedNews()
                .catch { exception ->
                    _state.value = BookmarksState(
                        error = exception.message ?: Constants.UNKNOWN_ERROR
                    )
                }
                .collect { bookmarkedNews ->
                    _state.value = BookmarksState(bookmarkedNews = bookmarkedNews)
                }
        }
    }
    
    fun removeBookmark(newsId: String) {
        viewModelScope.launch {
            bookmarkNewsUseCase(newsId, false)
            
            // Refresh the bookmarked news list
            getBookmarkedNews()
        }
    }
}

data class BookmarksState(
    val isLoading: Boolean = false,
    val bookmarkedNews: List<News> = emptyList(),
    val error: String? = null
)
