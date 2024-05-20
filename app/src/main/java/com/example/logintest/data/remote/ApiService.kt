package com.example.logintest.data.remote

import com.example.logintest.model.EventModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // get all events
    @GET("events/")
    suspend fun getAllEvents(): List<EventModel>
}