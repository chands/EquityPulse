package com.equitypulse.presentation.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equitypulse.EquityPulseApplication
import com.equitypulse.R
import com.equitypulse.domain.model.News

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    newsId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModelFactory = (context.applicationContext as EquityPulseApplication)
        .appComponent.viewModelFactory()
    
    val viewModel: NewsDetailViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(newsId) {
        viewModel.getNewsById(newsId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.news?.title?.take(20)?.plus("...") ?: "",
                        style = MaterialTheme.typography.titleMedium
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
                actions = {
                    // Bookmark button
                    IconButton(
                        onClick = { 
                            viewModel.toggleBookmark(newsId) 
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (state.news?.isBookmarked == true) 
                                    R.drawable.ic_bookmark_filled 
                                else 
                                    R.drawable.ic_bookmark_border
                            ),
                            contentDescription = if (state.news?.isBookmarked == true) 
                                stringResource(R.string.unbookmark) else stringResource(R.string.bookmark),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    // Share button
                    IconButton(
                        onClick = { 
                            viewModel.shareNews() 
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
                state.news != null -> {
                    NewsDetailContent(
                        news = state.news!!,
                        onFullStoryClick = {
                            viewModel.openFullStory()
                        }
                    )
                }
            }
        }
    }

    // Handle one-time events
    LaunchedEffect(state.shareNewsEvent) {
        if (state.shareNewsEvent) {
            // In a real app, we would launch a share intent here
            // For now, we'll just reset the event flag
            viewModel.shareNewsHandled()
        }
    }
    
    LaunchedEffect(state.openFullStoryEvent) {
        if (state.openFullStoryEvent) {
            // In a real app, we would launch a browser intent here
            // For now, we'll just reset the event flag
            viewModel.openFullStoryHandled()
        }
    }
}

@Composable
fun NewsDetailContent(
    news: News,
    onFullStoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = news.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        // Source and date
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = news.source,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = news.publishedAt,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Divider()
        
        // Content
        Text(
            text = news.content,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // View full story button
        Button(
            onClick = onFullStoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.view_full_story))
        }
    }
}
