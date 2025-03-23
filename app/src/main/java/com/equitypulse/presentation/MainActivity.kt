package com.equitypulse.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.equitypulse.EquityPulseApplication
import com.equitypulse.presentation.common.ViewModelFactory
import com.equitypulse.presentation.common.components.BottomNavigationBar
import com.equitypulse.presentation.common.theme.EquityPulseTheme
import com.equitypulse.presentation.navigation.NavGraph
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Inject dependencies
        (application as EquityPulseApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        
        setContent {
            EquityPulseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            NavGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}