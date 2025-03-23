package com.equitypulse.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equitypulse.domain.model.News
import com.equitypulse.domain.usecase.GetLatestNewsUseCase
import com.equitypulse.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.equitypulse.data.local.dao.NewsDao
import com.equitypulse.data.local.entity.NewsEntity
import com.equitypulse.domain.repository.NewsRepository
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.delay

class HomeViewModel @Inject constructor(
    private val getLatestNewsUseCase: GetLatestNewsUseCase,
    private val newsRepository: NewsRepository,
    private val newsDao: NewsDao
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    // Pagination parameters
    private var currentPage = 1
    private val pageSize = 10
    private var hasMoreData = true
    private var isLoadingMore = false
    
    init {
        fetchLatestNews()
    }
    
    private fun fetchLatestNews() {
        viewModelScope.launch {
            _state.value = HomeState(isLoading = true)
            currentPage = 1
            hasMoreData = true
            
            try {
                // First try to get news normally
                val newsList = getLatestNewsUseCase()
                Log.d("HomeViewModel", "Fetched ${newsList.size} news items")
                
                if (newsList.isEmpty()) {
                    Log.d("HomeViewModel", "News list is empty, trying to process sample data")
                    processAlphaVantageData()
                    // Try getting the news again after processing sample data
                    val updatedNewsList = getLatestNewsUseCase()
                    _state.value = HomeState(news = updatedNewsList)
                } else {
                    Log.d("HomeViewModel", "First news item: ${newsList.first().title}")
                    _state.value = HomeState(news = newsList)
                }
            } catch (exception: Exception) {
                Log.e("HomeViewModel", "Error fetching news", exception)
                _state.value = HomeState(
                    error = exception.message ?: Constants.UNKNOWN_ERROR
                )
            }
        }
    }
    
    fun loadMoreNews() {
        if (isLoadingMore || !hasMoreData) return
        
        viewModelScope.launch {
            try {
                isLoadingMore = true
                
                // Update UI to show loading state for "Load More"
                _state.update { it.copy(isLoadingMore = true) }
                
                // Simulate API call delay for a more realistic loading experience
                delay(1500)
                
                currentPage++
                
                try {
                    // In a real app, you would call the API with pagination parameters
                    // For example:
                    // val moreNews = newsRepository.getLatestNews(page = currentPage, pageSize = pageSize)
                    val moreNews = generateMoreNews(currentPage, pageSize)
                    
                    if (moreNews.isEmpty()) {
                        _state.update { it.copy(
                            isLoadingMore = false,
                            hasReachedEnd = true,
                            loadMoreError = null
                        )}
                        hasMoreData = false
                    } else {
                        // Add new items to the existing list
                        val updatedList = _state.value.news + moreNews
                        _state.update { it.copy(
                            news = updatedList,
                            isLoadingMore = false,
                            hasReachedEnd = moreNews.size < pageSize,
                            loadMoreError = null
                        )}
                        
                        // Update if we've reached the end
                        hasMoreData = moreNews.size >= pageSize
                    }
                } catch (exception: Exception) {
                    Log.e("HomeViewModel", "Error fetching more news", exception)
                    _state.update { it.copy(
                        isLoadingMore = false,
                        loadMoreError = exception.message ?: Constants.UNKNOWN_ERROR
                    )}
                }
            } finally {
                isLoadingMore = false
            }
        }
    }
    
    private fun generateMoreNews(page: Int, pageSize: Int): List<News> {
        val newsList = mutableListOf<News>()
        
        // Generate sample news for demo purposes
        // In a real app, this would be replaced with API calls
        for (i in 1..pageSize) {
            val index = (page - 1) * pageSize + i
            val newsId = UUID.randomUUID().toString()
            val publishDate = "2025${(index % 12) + 1}${20 + (index % 7)}T${10 + (index % 12)}${index % 60}00"
            
            // Format the date for display
            val formattedDate = try {
                val inputFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(publishDate)
                date?.let { outputFormat.format(it) } ?: publishDate
            } catch (e: Exception) {
                // Fallback to original string if parsing fails
                publishDate
            }
            
            // Create sample news with page number in title to show pagination is working
            val news = News(
                id = newsId,
                title = "Page $page: Financial News #$index",
                summary = "This is sample news item #$index generated for page $page of our pagination demo.",
                content = "Detailed content for news item #$index on page $page. This would typically contain the full article text.",
                originalUrl = "https://example.com/news/$newsId",
                imageUrl = "https://example.com/images/$newsId.jpg",
                publishDate = publishDate,
                publishedAt = formattedDate,
                source = "EquityPulse Demo",
                relatedStockSymbols = listOf("AAPL", "GOOG", "MSFT").shuffled().take(1 + (index % 3)),
                category = listOf("Technology", "Financial Markets", "Cryptocurrencies").random(),
                isBookmarked = false
            )
            
            newsList.add(news)
        }
        
        // Simulate network delay
        Thread.sleep(500)
        
        return newsList
    }
    
    private suspend fun processAlphaVantageData() {
        try {
            // Create a NewsEntity from the sample data
            val newsEntity = NewsEntity(
                id = UUID.randomUUID().toString(),
                title = "SHAREHOLDER ACTION REMINDER: Faruqi & Faruqi, LLP Investigates Claims on Behalf of Investors of Polestar Automotive",
                summary = "Faruqi & Faruqi, LLP Securities Litigation Partner James (Josh) Wilson Encourages Investors Who Suffered Losses Exceeding $100,000 In Polestar To Contact Him Directly To Discuss Their Options",
                content = "Faruqi & Faruqi, LLP Securities Litigation Partner James (Josh) Wilson Encourages Investors Who Suffered Losses Exceeding $100,000 In Polestar To Contact Him Directly To Discuss Their Options",
                originalUrl = "https://www.benzinga.com/pressreleases/25/03/g44449860/shareholder-action-reminder-faruqi-faruqi-llp-investigates-claims-on-behalf-of-investors-of-polest",
                imageUrl = "https://ml.globenewswire.com/media/8628babf-2ea4-4b8a-a60b-260db109bfa2/small/logo-png.png",
                publishDate = "20250323T115500",
                source = "Globe Newswire",
                relatedStockSymbols = "META,PSNY",
                category = "Financial Markets",
                isBookmarked = false
            )

            // Insert the news into the database
            newsDao.insertNews(newsEntity)
            Log.d("HomeViewModel", "Inserted sample news: ${newsEntity.title}")
            
            // Create additional news items
            val newsEntity2 = NewsEntity(
                id = UUID.randomUUID().toString(),
                title = "Tesla unveils new battery technology with 30% higher energy density",
                summary = "Tesla has announced a breakthrough in battery technology that promises to increase range by 30% and reduce costs.",
                content = "Tesla has announced a breakthrough in battery technology that promises to increase range by 30% and reduce costs. The new cells feature a novel chemistry and manufacturing process that could lead to more affordable electric vehicles.",
                originalUrl = "https://example.com/tesla-battery",
                imageUrl = "https://example.com/tesla-image.jpg",
                publishDate = "20250322T093000",
                source = "Tech News",
                relatedStockSymbols = "TSLA",
                category = "Technology",
                isBookmarked = false
            )
            
            val newsEntity3 = NewsEntity(
                id = UUID.randomUUID().toString(),
                title = "Apple's AI integration strategy detailed in new report",
                summary = "A new report details Apple's plans to integrate AI across its product ecosystem in the next major iOS update.",
                content = "A new report details Apple's plans to integrate AI across its product ecosystem in the next major iOS update. The company is leveraging partnerships with leading AI companies to enhance Siri and other core features.",
                originalUrl = "https://example.com/apple-ai",
                imageUrl = "https://example.com/apple-image.jpg",
                publishDate = "20250321T145500",
                source = "Tech Insider",
                relatedStockSymbols = "AAPL,MSFT,GOOG",
                category = "Technology",
                isBookmarked = false
            )
            
            // Insert additional news items
            newsDao.insertNews(newsEntity2)
            newsDao.insertNews(newsEntity3)
            Log.d("HomeViewModel", "Inserted 2 additional sample news items")
            
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error processing sample data", e)
        }
    }
    
    fun refreshNews() {
        fetchLatestNews()
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val news: List<News> = emptyList(),
    val error: String? = null,
    val loadMoreError: String? = null,
    val hasReachedEnd: Boolean = false
)
