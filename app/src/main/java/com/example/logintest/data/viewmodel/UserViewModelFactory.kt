package com.example.logintest.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logintest.data.settings.DataStoreUserPreference

class UserViewModelFactory(
    private val userPreferences: DataStoreUserPreference,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userPreferences, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}