package com.example.logintest.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
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

    private val apiService: ApiService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(userPreferences))
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
            val accessToken = userPreferences.getAccessToken()
            if (accessToken.isNotEmpty()) {
                _loginState.value = LoginState.Success
                // Optionally fetch user data here
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

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "access") val accessToken: String,
    @Json(name = "refresh") val refreshToken: String,
    @Json(name = "user") val user: JsonToUser
)

@JsonClass(generateAdapter = true)
data class RegistrationRequest(
    val email: String,
    val password: String,
    val username: String,
    val firstName: String,
    val lastName: String
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
data class LoginRequest(val email: String, val password: String)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest): Response<Unit>

    @POST("auth/register")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<AuthResponse>
}

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