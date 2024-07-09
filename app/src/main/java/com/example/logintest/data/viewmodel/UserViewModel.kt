package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.settings.DataStoreUserPreference
import com.example.logintest.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class RegistrationRequest(
    val email: String,
    val password: String,
    val username: String,
    val firstName: String,
    val lastName: String
)

sealed class RegistrationState {
    data object Idle : RegistrationState()
    data object Loading : RegistrationState()
    data class Success(val token: String) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}

class UserViewModel(private val userPreferences: DataStoreUserPreference) : ViewModel() {
    private val _userData = MutableStateFlow<UserModel?>(null)
    val userData: StateFlow<UserModel?> = _userData

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://10.0.0.97:8000/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        viewModelScope.launch {
            userPreferences.authToken.collect { token ->
                if (token != null) {
                    // Token exists, user is logged in
                    _loginState.value = LoginState.Success(token)
                    // Optionally fetch user data here
                }
            }
        }
    }

    fun setUser(user: UserModel) {
        _userData.value = user
    }

    fun getUser(): UserModel {
        return UserModel(
            123,
            "password",
            "Banana333",
            "Flavio",
            "Ranieri",
            "latteconlemani@libero.it",
            true,
            5,
            "https://static.nexilia.it/mangaforever/2022/08/af9011e585d0772b2332ab7d16985672-1280x720.jpg"
        )
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(LoginRequest(email, password))
                val user = UserModel(
                    id = response.userId,
                    password = "",
                    username = response.username,
                    firstName = response.firstName,
                    lastName = response.lastName,
                    email = email,
                    isStaff = false,
                    eventsParticipated = 0,
                )
                _userData.value = user
                userPreferences.saveAuthToken(response.token)
                userPreferences.saveUserInfo(
                    response.userId,
                    email,
                    "${response.firstName} ${response.lastName}"
                )
                _loginState.value = LoginState.Success(response.token)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearUserData()
            _userData.value = null
            _loginState.value = LoginState.Idle
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
                val request = RegistrationRequest(email, password, username, firstName, lastName)
                val response = apiService.register(request)
                val user = UserModel(
                    id = response.userId,
                    password = "",
                    username = response.username,
                    firstName = response.firstName,
                    lastName = response.lastName,
                    email = email,
                    isStaff = false,
                    eventsParticipated = 0,
                )
                _userData.value = user
                userPreferences.saveAuthToken(response.token)
                userPreferences.saveUserInfo(
                    response.userId,
                    email,
                    "${response.firstName} ${response.lastName}"
                )
                _registrationState.value = RegistrationState.Success(response.token)
                _loginState.value =
                    LoginState.Success(response.token) // Auto-login after registration
            } catch (e: Exception) {
                _registrationState.value =
                    RegistrationState.Error("Registration failed: ${e.message}")
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val isVerified: Boolean,
)

interface ApiService {
    @POST("auth/login/")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/register/")
    suspend fun register(@Body registrationRequest: RegistrationRequest): LoginResponse
}