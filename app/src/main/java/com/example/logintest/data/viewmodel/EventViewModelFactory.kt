package com.example.logintest.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logintest.data.settings.DataStoreUserPreference

class EventViewModelFactory(
    private val context: Context,
    private val userPreferences: DataStoreUserPreference,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(context, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}