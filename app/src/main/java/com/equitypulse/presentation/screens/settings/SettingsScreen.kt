package com.equitypulse.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.equitypulse.R
import com.equitypulse.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var realtimeAnalysisEnabled by remember { mutableStateOf(Constants.ENABLE_REALTIME_ANALYSIS) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_screen_title),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "App Settings",
                style = MaterialTheme.typography.titleMedium
            )
            
            // Theme setting
            SettingItem(
                title = "Dark Theme",
                subtitle = "Enable dark theme for the app",
                checked = darkThemeEnabled,
                onCheckedChange = { checked ->
                    darkThemeEnabled = checked
                    // In a real app, this would update app theme preference
                }
            )
            
            Divider()
            
            // Real-time analysis setting
            SettingItem(
                title = "Real-time Market Analysis",
                subtitle = "Enable real-time market analysis (may use more data)",
                checked = realtimeAnalysisEnabled,
                onCheckedChange = { checked ->
                    realtimeAnalysisEnabled = checked
                    // In a real app, this would update app preference
                }
            )
            
            Divider()
            
            // Notifications setting
            SettingItem(
                title = "Notifications",
                subtitle = "Receive notifications for important market updates",
                checked = notificationsEnabled,
                onCheckedChange = { checked ->
                    notificationsEnabled = checked
                    // In a real app, this would update app preference
                }
            )
            
            Divider()
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "App Version: 1.0.0",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
