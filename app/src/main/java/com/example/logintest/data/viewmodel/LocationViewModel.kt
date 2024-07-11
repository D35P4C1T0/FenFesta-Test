package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class LocationViewModel : ViewModel() {

    private val apiService: ApiService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://10.0.0.97:8000/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(ApiService::class.java)

        viewModelScope.launch {
        }
    }

    @JsonClass(generateAdapter = true)
    data class GeocodeRequest(
        val address: String,
    )

    @JsonClass(generateAdapter = true)
    data class GeocodeResponse(
        val address: String,
        val latitude: String,
        val longitude: String,
    )

    interface ApiService {
        @GET("geocode")
        suspend fun getCoords(@Body geocodeRequest: GeocodeRequest): Response<ProfileInfoResponse>
    }
}

