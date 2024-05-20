//package com.example.logintest.data.repository.requests
//
//import com.example.logintest.data.remote.ApiClient
//import com.example.logintest.data.remote.ApiService
//import com.example.logintest.model.EventModel
//
//class EventsRequest {
//    private val apiClient = ApiService.apiService
//
//    public suspend fun getAllEvents(): List<EventModel> {
//        return apiClient.getAllEvents()
//    }
//
//    public suspend fun getEvent(id: Int): EventModel {
//        return apiClient.getEvent(id)
//    }
//}