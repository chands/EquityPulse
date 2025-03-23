package com.equitypulse.presentation.screens.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equitypulse.EquityPulseApplication
import com.equitypulse.R
import com.equitypulse.domain.model.News
import com.equitypulse.presentation.components.NewsItem
import com.equitypulse.presentation.components.ErrorComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewsClick: (String) -> Unit,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val viewModelFactory = (context.applicationContext as EquityPulseApplication)
        .appComponent.viewModelFactory()
    
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    // State to control top bar visibility
    var isTopBarVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    
    // Auto-hide the top bar in landscape mode after delay
    LaunchedEffect(isLandscape) {
        isTopBarVisible = true
        
        if (isLandscape) {
            delay(3000) // 3 seconds
            isTopBarVisible = false
        }
    }
    
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isTopBarVisible || !isLandscape,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.home_screen_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* onSearchClick */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        
                        var showMenu by remember { mutableStateOf(false) }
                        
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Filter options")
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Latest News") },
                                onClick = { /* onFilterClick("latest") */ showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Top Headlines") },
                                onClick = { /* onFilterClick("top") */ showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Market Movers") },
                                onClick = { /* onFilterClick("market") */ showMenu = false }
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (isLandscape && !isTopBarVisible) {
                                coroutineScope.launch {
                                    isTopBarVisible = true
                                    delay(3000)
                                    isTopBarVisible = false
                                }
                            }
                        }
                    )
                }
        ) {
            // Add tap detector at the top of the screen to show the top bar
            if (isLandscape && !isTopBarVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .align(Alignment.TopCenter)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { 
                                    coroutineScope.launch {
                                        isTopBarVisible = true
                                        delay(3000)
                                        isTopBarVisible = false
                                    }
                                }
                            )
                        }
                ) {
                    // Empty box for detecting taps at the top
                }
            }
            
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    ErrorComponent(
                        message = state.error ?: stringResource(id = R.string.network_error),
                        onRetry = { viewModel.loadMoreNews() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.news.isEmpty() -> {
                    Text(
                        text = stringResource(id = R.string.empty_news),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
                else -> {
                    NewsContent(
                        news = state.news,
                        onNewsClick = onNewsClick,
                        onLoadMore = { viewModel.loadMoreNews() },
                        isLoadingMore = state.isLoadingMore,
                        hasReachedEnd = state.hasReachedEnd,
                        loadMoreError = state.loadMoreError
                    )
                }
            }
        }
    }
}

@Composable
fun NewsContent(
    news: List<News>,
    onNewsClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    hasReachedEnd: Boolean,
    loadMoreError: String?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(news) { newsItem ->
            NewsCard(
                news = newsItem,
                onNewsClick = onNewsClick
            )
        }
        
        // Load more section at the end of the list
        item {
            when {
                loadMoreError != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading more news",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Button(onClick = onLoadMore) {
                            Text("Retry")
                        }
                    }
                }
                
                isLoadingMore -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
                
                hasReachedEnd -> {
                    Text(
                        text = "You've reached the end",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                else -> {
                    Button(
                        onClick = onLoadMore,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                            Text("Load More")
                        }
                    }
                }
            }
        }
        
        // Add some bottom spacing after the load more button
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NewsCard(
    news: News,
    onNewsClick: (String) -> Unit
) {
    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        onClick = { onNewsClick(news.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            focusedElevation = 8.dp,
            hoveredElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = news.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = news.source,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = news.publishedAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
