package com.equitypulse.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.equitypulse.domain.repository.NewsRepository
import com.equitypulse.domain.usecase.BookmarkNewsUseCase
import com.equitypulse.domain.usecase.GetLatestNewsUseCase
import com.equitypulse.presentation.screens.bookmarks.BookmarksViewModel
import com.equitypulse.presentation.screens.detail.NewsDetailViewModel
import com.equitypulse.presentation.screens.home.HomeViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val newsRepository: Provider<NewsRepository>,
    private val getLatestNewsUseCase: Provider<GetLatestNewsUseCase>,
    private val bookmarkNewsUseCase: Provider<BookmarkNewsUseCase>
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(getLatestNewsUseCase.get()) as T
            }
            modelClass.isAssignableFrom(NewsDetailViewModel::class.java) -> {
                NewsDetailViewModel(
                    newsRepository.get(),
                    bookmarkNewsUseCase.get()
                ) as T
            }
            modelClass.isAssignableFrom(BookmarksViewModel::class.java) -> {
                BookmarksViewModel(
                    newsRepository.get(),
                    bookmarkNewsUseCase.get()
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
