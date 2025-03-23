package com.equitypulse.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore

    // Preference keys
    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val REALTIME_ANALYSIS = booleanPreferencesKey("realtime_analysis")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
    }

    // Get preferences as flows
    val darkThemeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_THEME] ?: false
    }

    val realtimeAnalysisFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REALTIME_ANALYSIS] ?: false
    }

    val notificationsFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS] ?: true
    }

    // Update preferences
    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = enabled
        }
    }

    suspend fun setRealtimeAnalysis(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REALTIME_ANALYSIS] = enabled
        }
    }

    suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }
} 