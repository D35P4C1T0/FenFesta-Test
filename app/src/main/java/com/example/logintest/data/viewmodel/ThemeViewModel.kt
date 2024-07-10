package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.settings.ThemePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ThemeOption {
    LIGHT, DARK, SYSTEM
}

class ThemeViewModel(private val themePreferences: ThemePreferences) : ViewModel() {
    val themeOption = themePreferences.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ThemeOption.SYSTEM
    )

    fun setThemeOption(option: ThemeOption) {
        viewModelScope.launch {
            themePreferences.saveThemeOption(option)
        }
    }
}