package com.equitypulse.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.equitypulse.data.local.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val darkThemeEnabled: Boolean = false,
    val realtimeAnalysisEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = true
)

class SettingsViewModel @Inject constructor(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesManager.darkThemeFlow,
                preferencesManager.realtimeAnalysisFlow,
                preferencesManager.notificationsFlow
            ) { darkTheme, realtimeAnalysis, notifications ->
                SettingsState(
                    darkThemeEnabled = darkTheme,
                    realtimeAnalysisEnabled = realtimeAnalysis,
                    notificationsEnabled = notifications,
                    isLoading = false
                )
            }.collectLatest { newState ->
                _state.value = newState
            }
        }
    }

    fun toggleDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(enabled)
        }
    }

    fun toggleRealtimeAnalysis(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setRealtimeAnalysis(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setNotifications(enabled)
        }
    }

    class Factory @Inject constructor(
        private val preferencesManager: UserPreferencesManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(preferencesManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 