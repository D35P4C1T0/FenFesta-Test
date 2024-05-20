package com.example.logintest.data.remote

import com.example.logintest.model.EventModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // get all events
    @GET("7d5c743f-1997-419e-9969-30ba17e3bfd4")
    suspend fun getAllEvents(): List<EventModel>
}