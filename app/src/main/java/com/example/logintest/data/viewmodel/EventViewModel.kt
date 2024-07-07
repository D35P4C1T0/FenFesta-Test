package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.remote.ApiService
import com.example.logintest.data.remote.EventModelListAdapter
import com.example.logintest.data.remote.LocalTimeAdapter
import com.example.logintest.data.remote.RetrofitClient
import com.example.logintest.model.EventModel
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class EventViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<EventModel>>(emptyList())
    val events: StateFlow<List<EventModel>> = _events

    private val _selectedEvent = MutableStateFlow<EventModel?>(null)
    val selectedEvent: StateFlow<EventModel?> = _selectedEvent

    private val apiService: ApiService

    init {
        val moshi = Moshi.Builder()
            .add(EventModelListAdapter())
            .add(LocalTimeAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://10.0.0.97:8000/") // Replace with your actual base URL
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }


    fun fetchEvents() {
        viewModelScope.launch {
            try {
                println("Fetching events...")
                val fetchedEvents = apiService.getEvents()
                _events.value = fetchedEvents
                println("Fetched ${fetchedEvents.size} events")
                println("First event: ${fetchedEvents.firstOrNull()}")
            } catch (e: Exception) {
                println("Error fetching events: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun fetchEventById(id: Int) {
        viewModelScope.launch {
            try {
                println("Fetching event with id $id...")
                val fetchedEvent = apiService.getEvent(id)
                _selectedEvent.value = fetchedEvent
                println("Fetched event: $fetchedEvent")
            } catch (e: Exception) {
                println("Error fetching event with id $id: ${e.message}")
                e.printStackTrace()
                _selectedEvent.value = null
            }
        }
    }

    interface ApiService {
        @GET("events") // Replace with your actual endpoint
        suspend fun getEvents(): List<EventModel>

        @GET("events/{id}") // Replace with your actual endpoint
        suspend fun getEvent(@Path("id") id: Int): EventModel
    }
}