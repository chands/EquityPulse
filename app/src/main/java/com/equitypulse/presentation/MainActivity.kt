package com.equitypulse.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.equitypulse.EquityPulseApplication
import com.equitypulse.di.AppComponent
import com.equitypulse.presentation.common.components.BottomNavigationBar
import com.equitypulse.presentation.common.theme.EquityPulseTheme
import com.equitypulse.presentation.navigation.NavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var appComponent: AppComponent
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        (application as EquityPulseApplication)
            .appComponent
            .inject(this)
        
        setContent {
            MainContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val coroutineScope = rememberCoroutineScope()
    
    // State to control bottom nav visibility
    var isBottomNavVisible by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Auto-hide the bottom nav in landscape mode after delay
    LaunchedEffect(isLandscape, currentRoute) {
        isBottomNavVisible = true
        
        if (isLandscape) {
            delay(3000) // 3 seconds
            isBottomNavVisible = false
        }
    }
    
    EquityPulseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = !isLandscape || isBottomNavVisible,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 500)
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(durationMillis = 500)
                        )
                    ) {
                        BottomNavigationBar(navController = navController)
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                // Show the bottom nav on tap
                                if (isLandscape && !isBottomNavVisible) {
                                    isBottomNavVisible = true
                                    // Hide it again after delay
                                    coroutineScope.launch {
                                        delay(3000)
                                        isBottomNavVisible = false
                                    }
                                }
                            }
                        }
                ) {
                    NavGraph(navController = navController)
                
                    // Add tap detector at the bottom of the screen to show bottom nav
                    if (isLandscape && !isBottomNavVisible) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .height(40.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { 
                                            isBottomNavVisible = true
                                            // Hide it again after delay
                                            coroutineScope.launch {
                                                delay(3000)
                                                isBottomNavVisible = false
                                            }
                                        }
                                    )
                                }
                        ) {
                            // Empty box for detecting taps at the bottom
                        }
                    }
                }
            }
        }
    }
}