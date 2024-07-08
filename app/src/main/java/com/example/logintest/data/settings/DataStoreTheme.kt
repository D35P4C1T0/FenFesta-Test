package com.example.logintest.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.logintest.data.viewmodel.ThemeOption
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemePreferences(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme_option")

    val themeFlow = context.dataStore.data.map { preferences ->
        ThemeOption.valueOf(preferences[themeKey] ?: ThemeOption.SYSTEM.name)
    }

    suspend fun saveThemeOption(option: ThemeOption) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = option.name
        }
    }
}