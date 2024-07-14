package com.example.logintest.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.R
import com.example.logintest.data.remote.EventModelListAdapter
import com.example.logintest.data.remote.LocalTimeAdapter
import com.example.logintest.data.settings.DataStoreUserPreference
import com.example.logintest.model.EventModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.YearMonth
import java.util.concurrent.TimeUnit

class EventViewModel(
    private val context: Context,
    private val userPreferences: DataStoreUserPreference
) : ViewModel() {

    private val _events = MutableStateFlow<List<EventModel>>(emptyList())
    val events: StateFlow<List<EventModel>> = _events

    private val _selectedEvent = MutableStateFlow<EventModel?>(null)
    val selectedEvent: StateFlow<EventModel?> = _selectedEvent

    private val _monthEvents = MutableStateFlow<List<EventModel>>(emptyList())
    val monthEvents: StateFlow<List<EventModel>> = _monthEvents

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _searchResults = MutableStateFlow<List<EventModel>>(emptyList())
    val searchResults: StateFlow<List<EventModel>> = _searchResults

    private val apiService: ApiService

    private val baseURL = context.getString(R.string.base_url)

    init {
        Log.d("EventViewModel", "ViewModel created: ${this.hashCode()}")
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
            .addInterceptor(AuthInterceptor(userPreferences))
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL) // Replace with your actual base URL
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("EventViewModel", "ViewModel cleared: ${this.hashCode()}")
    }

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                val response = apiService.getEvents()
                _events.value = response
            } catch (e: Exception) {
                println("Error fetching events: ${e.message}")
                e.printStackTrace()
                // Print the stack trace to get more details about the error
                e.stackTrace.forEach { println(it) }
            }
        }
    }

    suspend fun fetchEventById(id: Int) {
        viewModelScope.launch {
            try {
                val fetchedEvent = apiService.getEvent(id)
                _selectedEvent.value = fetchedEvent
            } catch (e: Exception) {
                println("Error fetching event with id $id: ${e.message}")
                e.printStackTrace()
                _selectedEvent.value = null
            }
        }
    }

    fun fetchEventsByMonth(month: Int) {
        viewModelScope.launch {
            try {
                val fetchedEvents = apiService.getEventsByMonth(month)
                _monthEvents.value = fetchedEvents
            } catch (e: Exception) {
                println("Error fetching events for month $month: ${e.message}")
                e.printStackTrace()
                // Print the stack trace to get more details about the error
                e.stackTrace.forEach { println(it) }
            }
        }
    }

    fun updateCurrentMonth(yearMonth: YearMonth) {
        _currentMonth.value = yearMonth
        fetchEventsByMonth(yearMonth.monthValue)
    }

    fun searchEvents(keyword: String) {
        viewModelScope.launch {
            try {
                val results = apiService.searchEvents(keyword)
                _searchResults.value = results
            } catch (e: Exception) {
                println("Error searching events: ${e.message}")
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
    }

    fun createEvent(event: EventModel) {
        println("Asking to create event: $event")
        viewModelScope.launch {
            try {
                val results = apiService.createEvent(event)
                println("Event created result: $results")
            } catch (e: Exception){
                println("Error creating event: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /*
    name = models.CharField(max_length=100)
    description = models.TextField()
    creator = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    date = models.DateTimeField()
    location = models.CharField(max_length=100)
    lat = models.DecimalField(max_digits=9, decimal_places=6)
    lon = models.DecimalField(max_digits=9, decimal_places=6)
    capacity = models.IntegerField()
    capacity_left = models.IntegerField()
    created_at = models.DateTimeField(auto_now_add=True) // AUTOMATICO
    tags = models.CharField(max_length=200, blank=True)
     */



    interface ApiService {
        @GET("events") // Replace with your actual endpoint
        suspend fun getEvents(): List<EventModel>

        @GET("events/{id}") // Replace with your actual endpoint
        suspend fun getEvent(@Path("id") id: Int): EventModel

        @GET("events/month/{month}")
        suspend fun getEventsByMonth(@Path("month") month: Int): List<EventModel>

        @GET("events/search")
        suspend fun searchEvents(@Query("keyword") keyword: String): List<EventModel>

        @POST("events/new")
        suspend fun createEvent(@Body event: EventModel): EventModel
    }
}