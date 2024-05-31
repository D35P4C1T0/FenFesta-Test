package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.remote.RetrofitClient
import com.example.logintest.model.EventModel
import com.example.logintest.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val _eventsData = MutableStateFlow<List<EventModel>>(emptyList())
    val eventsData: StateFlow<List<EventModel>> = _eventsData
    init {
        // Initialize with some dummy data
       /* _eventsData.value = listOf(
            EventModel("Event 1", "Description 1", "Location 1", "Date 1", "Created At 1", 100, 50, "Creator 1"),
            EventModel("Event 2", "Description 2", "Location 2", "Date 2", "Created At 2", 200, 100, "Creator 2")
        )*/
    }

    private fun getAllEvents() {
        viewModelScope.launch {
            val response = RetrofitClient.APIService.getAllEvents()
            _eventsData.value = response
        }
    }
}