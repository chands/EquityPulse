package com.equitypulse.di

import com.equitypulse.data.repository.NewsRepositoryImpl
import com.equitypulse.data.repository.StockRepositoryImpl
import com.equitypulse.domain.repository.NewsRepository
import com.equitypulse.domain.repository.StockRepository
import com.equitypulse.domain.usecase.BookmarkNewsUseCase
import com.equitypulse.domain.usecase.FollowStockUseCase
import com.equitypulse.domain.usecase.GetLatestNewsUseCase
import com.equitypulse.presentation.common.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository
    
    @Binds
    @Singleton
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
    
    companion object {
        @Provides
        fun provideGetLatestNewsUseCase(newsRepository: NewsRepository): GetLatestNewsUseCase {
            return GetLatestNewsUseCase(newsRepository)
        }
        
        @Provides
        fun provideBookmarkNewsUseCase(newsRepository: NewsRepository): BookmarkNewsUseCase {
            return BookmarkNewsUseCase(newsRepository)
        }
        
        @Provides
        fun provideFollowStockUseCase(stockRepository: StockRepository): FollowStockUseCase {
            return FollowStockUseCase(stockRepository)
        }
        
        @Provides
        fun provideViewModelFactory(
            newsRepository: NewsRepository,
            getLatestNewsUseCase: GetLatestNewsUseCase,
            bookmarkNewsUseCase: BookmarkNewsUseCase
        ): ViewModelFactory {
            return ViewModelFactory(
                { newsRepository },
                { getLatestNewsUseCase },
                { bookmarkNewsUseCase }
            )
        }
    }
} 