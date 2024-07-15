package com.example.logintest.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.R
import com.example.logintest.data.settings.DataStoreUserPreference
import com.example.logintest.model.UserModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

class UserViewModel(
    private val userPreferences: DataStoreUserPreference,
    private val context: Context,
) : ViewModel() {
    private val _userData = MutableStateFlow<UserModel?>(null)
    val userData: StateFlow<UserModel?> = _userData

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState: StateFlow<LogoutState> = _logoutState

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    private val _passwordChangeState =
        MutableStateFlow<PasswordChangeState>(PasswordChangeState.Initial)
    val passwordChangeState: StateFlow<PasswordChangeState> = _passwordChangeState

    private val _reservationState = MutableStateFlow<ReservationState>(ReservationState.Initial)
    val reservationState: StateFlow<ReservationState> = _reservationState

    private val _reservationDeletionState =
        MutableStateFlow<ReservationDeletionState>(ReservationDeletionState.Initial)
    val reservationDeletionState: StateFlow<ReservationDeletionState> = _reservationDeletionState

    private val apiService: ApiService

    private val baseURL = context.getString(R.string.base_url)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(userPreferences))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(ApiService::class.java)

        viewModelScope.launch {
            val accessToken = userPreferences.getAccessToken()
            if (accessToken.isNotEmpty()) {
                _loginState.value = LoginState.Success
                // Optionally fetch user data here
                profileInfo()
            }
        }
    }

    // dumb function to get user data
    fun getUser(): UserModel {
        return UserModel(
            id = 123,
//            password="password",
            "Banana333",
            "Flavio",
            "Ranieri",
            "latteconlemani@libero.it",
            true,
            "https://static.nexilia.it/mangaforever/2022/08/af9011e585d0772b2332ab7d16985672-1280x720.jpg"
        )
    }

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _passwordChangeState.value = PasswordChangeState.Loading
            try {
                val response = apiService.changePassword(
                    ChangePasswordRequest(
                        oldPassword,
                        newPassword,
                        confirmPassword
                    )
                )
                if (response.isSuccessful) {
                    _passwordChangeState.value =
                        PasswordChangeState.Success("Password changed successfully")
                } else {
                    _passwordChangeState.value =
                        PasswordChangeState.Error("Password change failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _passwordChangeState.value =
                    PasswordChangeState.Error("Password change failed: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(LoginRequest(email, password))
                println("response: $response")
                handleAuthResponse(response, isLogin = true)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = LogoutState.Loading
            try {
                val refreshToken = userPreferences.getRefreshToken()
                if (refreshToken.isNotEmpty()) {
                    val response = apiService.logout(LogoutRequest(refreshToken))
                    if (response.isSuccessful) {
                        userPreferences.clearUserData()
                        _userData.value = null
                        _loginState.value = LoginState.Idle
                        _registrationState.value = RegistrationState.Idle
                        _logoutState.value = LogoutState.Success
                        println("Logout successful")
                    } else {
                        _logoutState.value =
                            LogoutState.Error("Logout failed: ${response.message()}")
                    }
                } else {
                    _logoutState.value = LogoutState.Error("No refresh token found")
                }
            } catch (e: Exception) {
                _logoutState.value = LogoutState.Error("Logout failed: ${e.message}")
            }
        }
    }

    fun register(
        email: String,
        password: String,
        username: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            try {
                val response = apiService.register(
                    RegistrationRequest(
                        email,
                        password,
                        username,
                        firstName,
                        lastName
                    )
                )
                handleAuthResponse(response, isLogin = false)
            } catch (e: Exception) {
                _registrationState.value =
                    RegistrationState.Error("Registration failed: ${e.message}")
            }
        }
    }

    private suspend fun handleAuthResponse(response: Response<AuthResponse>, isLogin: Boolean) {
        if (response.isSuccessful) {
            response.body()?.let { authResponse ->
                // DataStore
                userPreferences.saveAccessToken(authResponse.accessToken)
                userPreferences.saveRefreshToken(authResponse.refreshToken)
                userPreferences.saveUserName(authResponse.user.username)
                userPreferences.saveUserEmail(authResponse.user.email)

                println("Successful Login: $authResponse")

                // Save user data
                _userData.value = UserModel(
                    id = authResponse.user.id,
                    username = authResponse.user.username,
                    firstName = authResponse.user.firstName,
                    lastName = authResponse.user.lastName,
                    email = authResponse.user.email,
                    isStaff = authResponse.user.isStaff,
                    isActive = authResponse.user.isActive,
                    dateJoined = authResponse.user.dateJoined
                )

                if (isLogin) {
                    _loginState.value = LoginState.Success
                } else {
                    _registrationState.value = RegistrationState.Success
                }
            } ?: run {
                val errorMessage = "Authentication successful, but response body was empty"
                if (isLogin) {
                    _loginState.value = LoginState.Error(errorMessage)
                } else {
                    _registrationState.value = RegistrationState.Error(errorMessage)
                }
            }
        } else {
            val errorMessage = "Authentication failed: ${response.message()}"
            if (isLogin) {
                _loginState.value = LoginState.Error(errorMessage)
            } else {
                _registrationState.value = RegistrationState.Error(errorMessage)
            }
        }
    }

    fun clearPasswordChangeState() {
        _passwordChangeState.value = PasswordChangeState.Initial
    }

    fun profileInfo() {
        viewModelScope.launch {
            try {
                val response = apiService.getProfile()
                println("Profile info response: $response")
                if (response.isSuccessful) {
                    response.body()?.let { profileInfoResponse ->
                        _userData.value = UserModel(
                            id = profileInfoResponse.user.id,
                            username = profileInfoResponse.user.username,
                            firstName = profileInfoResponse.user.firstName,
                            lastName = profileInfoResponse.user.lastName,
                            email = profileInfoResponse.user.email,
                            isStaff = profileInfoResponse.user.isStaff,
                            isActive = profileInfoResponse.user.isActive,
                            dateJoined = profileInfoResponse.user.dateJoined
                        )
                    }
                }
            } catch (e: Exception) {
                println("Error fetching profile info: ${e.message}")
            }
        }
    }

    fun addReservation(eventID: String) {
        viewModelScope.launch {
            _reservationState.value = ReservationState.Loading
            try {
                apiService.createReservation(ReservationRequest(eventID))
                _reservationState.value =
                    ReservationState.Success("Reservation created successfully")
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorAdapter = moshi.adapter(ErrorResponse::class.java)
                        val errorResponse = errorBody?.let { errorAdapter.fromJson(it) }
                        when (errorResponse?.code) {
                            "MISSING_EVENT_ID" -> _reservationState.value =
                                ReservationState.Error("Event ID is missing")

                            "EVENT_FULL" -> _reservationState.value =
                                ReservationState.Error("Evento al completo")

                            "RESERVATION_EXISTS" -> _reservationState.value =
                                ReservationState.ReservationExists("Ti sei già prenotato per questo evento!")

                            else -> _reservationState.value =
                                ReservationState.Error("Impossibile creare prenotazione: ${errorResponse?.error ?: "Unknown error"}")
                        }
                    }

                    else -> _reservationState.value =
                        ReservationState.Error("Failed to create reservation: ${e.message()}")
                }
            } catch (e: Exception) {
                _reservationState.value =
                    ReservationState.Error("Failed to create reservation: ${e.message}")
            }
        }
    }

    fun clearReservationState() {
        _reservationState.value = ReservationState.Initial
    }

    fun clearReservationDeleteState() {
        _reservationDeletionState.value = ReservationDeletionState.Initial
    }

    fun deleteReservation(eventId: Int) {
        viewModelScope.launch {
            _reservationDeletionState.value = ReservationDeletionState.Loading
            try {
                val response = apiService.deleteReservation(eventId)
                if (response.isSuccessful) {
                    _reservationDeletionState.value =
                        ReservationDeletionState.Success("Reservation successfully removed")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = parseErrorResponse(errorBody)
                    when (errorResponse.code) {
                        "EVENT_NOT_FOUND" -> _reservationDeletionState.value =
                            ReservationDeletionState.Error("Event not found")

                        "RESERVATION_NOT_FOUND" -> _reservationDeletionState.value =
                            ReservationDeletionState.Error("No reservation found for this event")

                        "PAST_EVENT" -> _reservationDeletionState.value =
                            ReservationDeletionState.Error("Cannot remove reservation for past events")

                        else -> _reservationDeletionState.value =
                            ReservationDeletionState.Error("Failed to remove reservation: ${errorResponse.error}")
                    }
                }
            } catch (e: Exception) {
                _reservationDeletionState.value =
                    ReservationDeletionState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    private fun parseErrorResponse(errorBody: String?): ErrorResponse {
        return try {
            Moshi.Builder().build().adapter(ErrorResponse::class.java).fromJson(errorBody ?: "")
                ?: ErrorResponse("Unknown error", "UNKNOWN_ERROR")
        } catch (e: Exception) {
            ErrorResponse("Failed to parse error", "PARSE_ERROR")
        }
    }
}

sealed class AuthState
sealed class RegistrationState : AuthState() {
    data object Idle : RegistrationState()
    data object Loading : RegistrationState()
    data object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}

sealed class LoginState : AuthState() {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class LogoutState {
    data object Idle : LogoutState()
    data object Loading : LogoutState()
    data object Success : LogoutState()
    data class Error(val message: String) : LogoutState()
}

sealed class PasswordChangeState {
    data object Initial : PasswordChangeState()
    data object Loading : PasswordChangeState()
    data class Success(val message: String) : PasswordChangeState()
    data class Error(val message: String) : PasswordChangeState()
}

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "access") val accessToken: String,
    @Json(name = "refresh") val refreshToken: String,
    @Json(name = "user") val user: JsonToUser
)


@JsonClass(generateAdapter = true)
data class ProfileInfoResponse(
    @Json(name = "user") val user: JsonToUser
)

@JsonClass(generateAdapter = true)
data class RegistrationRequest(
    val email: String,
    val password: String,
    val username: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String
)

@JsonClass(generateAdapter = true)
data class JsonToUser(
    val id: Int,
    val username: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    val email: String,
    @Json(name = "is_staff") val isStaff: Boolean,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "date_joined") val dateJoined: String
    // Add other fields as needed
)

@JsonClass(generateAdapter = true)
data class LogoutRequest(
    @Json(name = "refresh") val refresh: String
)

@JsonClass(generateAdapter = true)
data class ChangePasswordRequest(
    @Json(name = "old_password") val oldPassword: String,
    @Json(name = "new_password") val newPassword: String,
    @Json(name = "confirm_password") val confirmationPassword: String,
)

@JsonClass(generateAdapter = true)
data class ReservationRequest(
    @Json(name = "event_id") val eventID: String,
)

sealed class ReservationState {
    data object Initial : ReservationState()
    data object Loading : ReservationState()
    data class Success(val message: String) : ReservationState()
    data class Error(val message: String) : ReservationState()
    data class ReservationExists(val message: String) : ReservationState()
}

sealed class ReservationDeletionState {
    data object Initial : ReservationDeletionState()
    data object Loading : ReservationDeletionState()
    data class Success(val message: String) : ReservationDeletionState()
    data class Error(val message: String) : ReservationDeletionState()
}

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val error: String,
    val code: String
)

@JsonClass(generateAdapter = true)
data class LoginRequest(val email: String, val password: String)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest): Response<Unit>

    @POST("auth/register")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<AuthResponse>

    @GET("users/profile")
    suspend fun getProfile(): Response<ProfileInfoResponse>

    @POST("auth/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<Unit>

    @POST("reservation/new")
    suspend fun createReservation(@Body reservation: ReservationRequest): Unit

    @POST("reservations/{id}/remove")
    suspend fun deleteReservation(@Path("id") id: Int): Response<Unit>
}

// adds token to requests if present
class AuthInterceptor(private val userPreferences: DataStoreUserPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        println("Intercepting request: $originalRequest")
        val accessToken = runBlocking { userPreferences.getAccessToken() }
        println("Access token: $accessToken")
        return if (accessToken.isNotEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}