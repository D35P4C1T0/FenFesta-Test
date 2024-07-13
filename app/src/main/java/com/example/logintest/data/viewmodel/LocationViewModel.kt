    package com.example.logintest.data.viewmodel

    import android.content.Context
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.logintest.R
    import com.example.logintest.model.LocationModel
    import com.squareup.moshi.Json
    import com.squareup.moshi.JsonClass
    import com.squareup.moshi.Moshi
    import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch
    import okhttp3.OkHttpClient
    import retrofit2.Response
    import retrofit2.Retrofit
    import retrofit2.converter.moshi.MoshiConverterFactory
    import retrofit2.http.Body
    import retrofit2.http.POST
    import java.util.concurrent.TimeUnit

    class LocationViewModel(
        private val context: Context
    ) : ViewModel() {

        private val _locationData = MutableStateFlow<LocationModel?>(null)
        val locationData: StateFlow<LocationModel?> = _locationData

        private val apiService: ApiService

        private val baseURL = context.getString(R.string.base_url)

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
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            apiService = retrofit.create(ApiService::class.java)

            viewModelScope.launch {
            }
        }

        fun getCoords(address: String) {
            viewModelScope.launch {
                try {
                    val response = apiService.getCoords(GeocodeRequest(address))
                    if (response.isSuccessful) {
                        response.body()?.let { coordsResponse ->
                            println("Geocode response: $coordsResponse")
                            _locationData.value = LocationModel(
                                address = coordsResponse.address,
                                lat = coordsResponse.latitude.toDouble(),
                                lon = coordsResponse.longitude.toDouble(),
                                streetNumber = coordsResponse.streetNumber,
                                city = coordsResponse.city
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Handle error
                    println("Error: $e")
                }
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
            val streetNumber: String,
            val city: String
        )

        interface ApiService {
            @POST("geocode/")
            suspend fun getCoords(@Body geocodeRequest: GeocodeRequest): Response<GeocodeResponse>
        }
    }

