package com.example.logintest.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.remote.RetrofitClient
import com.example.logintest.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _userData = MutableStateFlow<UserModel?>(null)
    val userData: StateFlow<UserModel?> = _userData

    // Funzione per impostare i dati dell'utente
    fun setUser(user: UserModel) {
        _userData.value = user
    }

    // Funzione per ottenere i dati dell'utente
    fun getUser(): UserModel {
        return UserModel(123, "password", "Banana333", "Flavio", "Ranieri", "sessoconlemani@libero.it", true, 5, "https://static.nexilia.it/mangaforever/2022/08/af9011e585d0772b2332ab7d16985672-1280x720.jpg") // Valori di esempio per UserModel
        //return _userData.value
    }
}
