package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.ui.theme.navigation.MyApp
import com.example.logintest.ui.theme.AppTheme


class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MyApp(userViewModel)
            }
        }
    }
}