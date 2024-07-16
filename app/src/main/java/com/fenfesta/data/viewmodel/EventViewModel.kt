package com.fenfesta.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fenfesta.R
import com.fenfesta.data.notifications.NotificationScheduler
import com.fenfesta.data.remote.EventModelListAdapter
import com.fenfesta.data.remote.LocalTimeAdapter
import com.fenfesta.data.settings.DataStoreUserPreference
import com.fenfesta.data.settings.NotificationPreferences
import com.fenfesta.model.EventModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
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

    private val _eventsReserved = MutableStateFlow<List<EventModel>>(emptyList())
    val eventsReserved: StateFlow<List<EventModel>> = _eventsReserved

    private val _isEventReserved = MutableStateFlow(false)
    val isEventReserved: StateFlow<Boolean> = _isEventReserved

    private val _eventCreator = MutableStateFlow("")
    val eventCreator: StateFlow<String> = _eventCreator

    private val notificationScheduler = NotificationScheduler(context)
    private val notificationPreferences = NotificationPreferences(context)

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
                //println("Error fetching events: ${e.message}")
                e.printStackTrace()
                // Print the stack trace to get more details about the error
                e.stackTrace.forEach { println(it) }
            }
        }
    }

    fun fetchEventById(id: Int) {
        viewModelScope.launch {
            try {
                isEventReserved(id)
                val fetchedEvent = apiService.getEvent(id)
                _selectedEvent.value = fetchedEvent
            } catch (e: Exception) {
                //println("Error fetching event with id $id: ${e.message}")
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
                //println("Error fetching events for month $month: ${e.message}")
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
                //println("Error searching events: ${e.message}")
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
    }

    fun createEvent(event: EventModel) {
        //println("Asking to create event: $event")
        viewModelScope.launch {
            try {
                val results = apiService.createEvent(event)
                //println("Event created result: $results")
            } catch (e: Exception) {
                //println("Error creating event: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun listAllReservedEvents() {
        viewModelScope.launch {
            try {
                val response = apiService.listAllReservedEvents()
                //println("Reservation response ${response.body()}")
                if (response.isSuccessful) {
                    _eventsReserved.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                //println("Error fetching reserved events: ${e.message}")
            }
        }
    }

    fun clearEventsReservedList() {
        _eventsReserved.value = emptyList()
    }

    fun isEventReserved(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.isEventReserved(id)
                //println("Is event reserved response ${response.body()}")
                if (response.isSuccessful) {
                    _isEventReserved.value = response.body()?.isReserved ?: false
                }
            } catch (e: Exception) {
                //println("Error checking if event is reserved: ${e.message}")
            }
        }
    }

    fun fetchEventCreatorInfo(eventId: Int) {
        viewModelScope.launch {
            try {
                val info = apiService.getEventCreatorInfo(eventId)
                //println("Event creator info response ${info.body()}")
                _eventCreator.value = info.body()?.let {
                    "${it.creatorFirstName} ${it.creatorLastName}"
                } ?: ""
            } catch (e: Exception) {
                //println("Error fetching event creator info: ${e.message}")
            }
        }
    }

    // Notifications
//    val notifiedEvents = notificationPreferences.notifiedEvents.asLiveData()
    val notifiedEvents: Flow<Set<String>> = notificationPreferences.notifiedEvents


    fun toggleEventNotification(event: EventModel) {
        viewModelScope.launch {
            val eventId = event.id.toString()
            val isCurrentlyNotified = notificationPreferences.isEventNotified(eventId)

            if (isCurrentlyNotified) {
                notificationPreferences.removeNotifiedEvent(eventId)
                event.id?.let { notificationScheduler.cancelNotification(it) }
            } else {
                notificationPreferences.addNotifiedEvent(eventId)
                event.id?.let {
                    notificationScheduler.scheduleNotification(
                        it,
                        event.name,
                        event.date
                    )
                }
            }
        }
    }

    fun isEventNotified(eventId: String): Flow<Boolean> {
        return notificationPreferences.notifiedEvents.map { it.contains(eventId) }
    }


    data class ReservedResponse(@Json(name = "is_reserved") val isReserved: Boolean)

    @JsonClass(generateAdapter = true)
    data class CreatorInfo(
        @Json(name = "username") val creatorUserName: String,
        @Json(name = "first_name") val creatorFirstName: String,
        @Json(name = "last_name") val creatorLastName: String,
    )


    interface ApiService {
        @GET("events/upcoming") // Replace with your actual endpoint
        suspend fun getEvents(): List<EventModel>

        @GET("events/{id}") // Replace with your actual endpoint
        suspend fun getEvent(@Path("id") id: Int): EventModel

        @GET("events/month/{month}")
        suspend fun getEventsByMonth(@Path("month") month: Int): List<EventModel>

        @GET("events/search")
        suspend fun searchEvents(@Query("keyword") keyword: String): List<EventModel>

        @POST("events/new")
        suspend fun createEvent(@Body event: EventModel): EventModel

        @GET("users/reserved_events")
        suspend fun listAllReservedEvents(): Response<List<EventModel>>

        @GET("reservations/{id}/is_reserved")
        suspend fun isEventReserved(@Path("id") id: Int): Response<ReservedResponse>

        @GET("events/{eventId}/creator-info/")
        suspend fun getEventCreatorInfo(@Path("eventId") eventId: Int): Response<CreatorInfo>

    }
}