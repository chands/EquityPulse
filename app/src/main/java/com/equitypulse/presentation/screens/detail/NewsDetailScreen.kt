package com.equitypulse.presentation.screens.detail

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    
    LaunchedEffect(newsId) {
        viewModel.getNewsById(newsId)
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // Show the topbar on tap
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
            state.news?.let { news ->
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TITLE, news.title)
                    putExtra(Intent.EXTRA_TEXT, "${news.title}\n\n${news.summary}\n\nRead more: ${news.originalUrl}")
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share News"))
            }
            
            viewModel.shareNewsHandled()
        }
    }
    
    LaunchedEffect(state.openFullStoryEvent) {
        if (state.openFullStoryEvent) {
            state.news?.let { news ->
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.originalUrl))
                    context.startActivity(browserIntent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Cannot open URL: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            viewModel.openFullStoryHandled()
        }
    }
}

@Composable
fun NewsDetailContent(
    news: News,
    onFullStoryClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
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
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = news.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Source and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = news.source,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = news.publishedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            
            // Content
            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // View full story button
            Button(
                onClick = onFullStoryClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(id = R.string.view_full_story))
            }
        }
    }
}
