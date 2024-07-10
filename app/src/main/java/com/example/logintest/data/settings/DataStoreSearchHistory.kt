package com.example.logintest.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history")
        private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")
    }

    val searchHistory: Flow<List<String>> = context.dataStore.data.map { preferences ->
        preferences[SEARCH_HISTORY_KEY]?.split(",") ?: emptyList()
    }

    suspend fun addSearchQuery(query: String) {
        context.dataStore.edit { preferences ->
            val currentHistory =
                preferences[SEARCH_HISTORY_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentHistory.remove(query)
            currentHistory.add(0, query)
            val newHistory = currentHistory.take(10).joinToString(",")
            preferences[SEARCH_HISTORY_KEY] = newHistory
        }
    }

    suspend fun clearSearchHistory() {
        context.dataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY_KEY)
        }
    }
}