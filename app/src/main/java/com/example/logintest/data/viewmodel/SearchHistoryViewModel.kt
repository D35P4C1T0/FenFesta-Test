package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.settings.SearchHistoryDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchHistoryViewModel(private val searchHistoryDataStore: SearchHistoryDataStore) :
    ViewModel() {
    val searchHistory: StateFlow<List<String>> = searchHistoryDataStore.searchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSearchQuery(query: String) {
        viewModelScope.launch {
            searchHistoryDataStore.addSearchQuery(query)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDataStore.clearSearchHistory()
        }
    }
}