package com.equitypulse.presentation.screens.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equitypulse.EquityPulseApplication
import com.equitypulse.R
import com.equitypulse.domain.model.News
import androidx.compose.material.icons.filled.Bookmark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    onNewsClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModelFactory = (context.applicationContext as EquityPulseApplication)
        .appComponent.viewModelFactory()
    
    val viewModel: BookmarksViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.bookmarks_screen_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    Text(
                        text = state.error ?: stringResource(id = R.string.network_error),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
                state.bookmarkedNews.isEmpty() -> {
                    Text(
                        text = stringResource(id = R.string.empty_bookmarks),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
                else -> {
                    BookmarkedNewsContent(
                        bookmarkedNews = state.bookmarkedNews,
                        onNewsClick = onNewsClick,
                        onRemoveBookmark = { newsId ->
                            viewModel.removeBookmark(newsId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkedNewsContent(
    bookmarkedNews: List<News>,
    onNewsClick: (String) -> Unit,
    onRemoveBookmark: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(bookmarkedNews) { news ->
            BookmarkedNewsCard(
                news = news,
                onNewsClick = onNewsClick,
                onRemoveBookmark = onRemoveBookmark
            )
        }
    }
}

@Composable
fun BookmarkedNewsCard(
    news: News,
    onNewsClick: (String) -> Unit,
    onRemoveBookmark: (String) -> Unit
) {
    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        onClick = { onNewsClick(news.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = news.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = news.source,
                    style = MaterialTheme.typography.labelMedium
                )
                
                IconButton(
                    onClick = { onRemoveBookmark(news.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.unbookmark),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
