package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.remote.RetrofitClient
import com.example.logintest.model.EventModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val _eventsData = MutableStateFlow<List<EventModel>>(emptyList())
    val eventsData: StateFlow<List<EventModel>> = _eventsData

    init {
        getAllEvents()
    }

    private fun getAllEvents() {
        viewModelScope.launch {
            val response = RetrofitClient.APIService.getAllEvents()
            _eventsData.value = response
        }
    }
}