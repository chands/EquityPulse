package com.equitypulse.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equitypulse.domain.model.News
import com.equitypulse.domain.usecase.GetLatestNewsUseCase
import com.equitypulse.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getLatestNewsUseCase: GetLatestNewsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    init {
        fetchLatestNews()
    }
    
    fun fetchLatestNews() {
        viewModelScope.launch {
            _state.value = HomeState(isLoading = true)
            
            getLatestNewsUseCase()
                .catch { exception ->
                    _state.value = HomeState(
                        error = exception.message ?: Constants.UNKNOWN_ERROR
                    )
                }
                .collect { newsList ->
                    _state.value = HomeState(news = newsList)
                }
        }
    }
    
    fun refreshNews() {
        fetchLatestNews()
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val news: List<News> = emptyList(),
    val error: String? = null
)
