package com.equitypulse.presentation.common.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.equitypulse.EquityPulseApplication
import com.equitypulse.data.local.preferences.UserPreferencesManager

private val LightColorScheme = lightColorScheme(
    primary = PremiumDarkBlue,
    onPrimary = CardLight,
    primaryContainer = LightBlueSecondary,
    onPrimaryContainer = PremiumDarkBlue,
    
    secondary = BullishGreen,
    onSecondary = CardLight,
    secondaryContainer = LightGreenSecondary,
    onSecondaryContainer = BullishGreen,
    
    tertiary = PremiumGold,
    onTertiary = PremiumDarkBlue,
    tertiaryContainer = PremiumSilver,
    onTertiaryContainer = PremiumDarkBlue,
    
    error = BearishRed,
    onError = CardLight,
    errorContainer = LightRedSecondary,
    onErrorContainer = BearishRed,
    
    background = BackgroundLight,
    onBackground = PremiumDarkBlue,
    surface = CardLight,
    onSurface = PremiumDarkBlue
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPremiumBlue,
    onPrimary = CardLight,
    primaryContainer = NeutralBlue,
    onPrimaryContainer = CardLight,
    
    secondary = DarkBullishGreen,
    onSecondary = CardLight,
    secondaryContainer = BullishGreen,
    onSecondaryContainer = CardLight,
    
    tertiary = DarkPremiumGold,
    onTertiary = CardDark,
    tertiaryContainer = PremiumGold.copy(alpha = 0.7f),
    onTertiaryContainer = CardDark,
    
    error = DarkBearishRed,
    onError = CardLight,
    errorContainer = BearishRed,
    onErrorContainer = CardLight,
    
    background = BackgroundDark,
    onBackground = CardLight,
    surface = CardDark.copy(alpha = 0.95f),
    onSurface = CardLight,
    surfaceVariant = CardDark.copy(alpha = 0.90f),
    onSurfaceVariant = CardLight.copy(alpha = 0.85f),
    
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFF757575)
)

@Composable
fun EquityPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as? EquityPulseApplication
    
    // Get preferencesManager from the AppComponent if available
    val preferencesManager = if (application != null) {
        try {
            application.appComponent.getUserPreferencesManager()
        } catch (e: Exception) {
            null
        }
    } else null
    
    // If preferencesManager is available, use the user preference
    // Otherwise, fall back to system dark theme setting
    val isDarkTheme = if (preferencesManager != null) {
        val userThemePreference by preferencesManager.darkThemeFlow.collectAsState(initial = darkTheme)
        userThemePreference
    } else {
        darkTheme
    }
    
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 