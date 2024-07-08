package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.data.settings.ThemePreferences
import com.example.logintest.data.viewmodel.ThemeOption
import com.example.logintest.data.viewmodel.ThemeViewModel
import com.example.logintest.data.viewmodel.ThemeViewModelFactory
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.ui.theme.AppTheme
import com.example.logintest.ui.theme.navigation.MyApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themePreferences = ThemePreferences(this)
        val themeViewModelFactory = ThemeViewModelFactory(themePreferences)

        setContent {
            DynamicTheme(
                userViewModel = userViewModel,
                themeViewModel = viewModel(factory = themeViewModelFactory)
            )
        }
    }
}

@Composable
fun DynamicTheme(userViewModel: UserViewModel, themeViewModel: ThemeViewModel) {
    val themeOption by themeViewModel.themeOption.collectAsState()
    val darkTheme = when (themeOption) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK -> true
        ThemeOption.SYSTEM -> isSystemInDarkTheme()
    }

    AppTheme(darkTheme = darkTheme) {
        SystemUIController(isDarkTheme = darkTheme)
        MyApp(userViewModel, themeViewModel)
    }
}

@Composable
fun SystemUIController(isDarkTheme: Boolean) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.surface

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isDarkTheme
        )
    }
}