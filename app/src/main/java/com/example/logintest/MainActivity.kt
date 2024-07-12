package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.data.settings.DataStoreUserPreference
import com.example.logintest.data.settings.SearchHistoryDataStore
import com.example.logintest.data.settings.ThemePreferences
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.data.viewmodel.EventViewModelFactory
import com.example.logintest.data.viewmodel.SearchHistoryViewModel
import com.example.logintest.data.viewmodel.ThemeOption
import com.example.logintest.data.viewmodel.ThemeViewModel
import com.example.logintest.data.viewmodel.ThemeViewModelFactory
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.data.viewmodel.UserViewModelFactory
import com.example.logintest.ui.theme.AppTheme
import com.example.logintest.ui.theme.navigation.MyApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themePreferences = ThemePreferences(this)
        val themeViewModelFactory = ThemeViewModelFactory(themePreferences)

        setContent {
            DynamicTheme(
                themeViewModel = viewModel(factory = themeViewModelFactory),
            )
        }
    }
}

@Composable
fun DynamicTheme(themeViewModel: ThemeViewModel) {
    // Theme
    val themeOption by themeViewModel.themeOption.collectAsState()
    val darkTheme = when (themeOption) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK -> true
        ThemeOption.SYSTEM -> isSystemInDarkTheme()
    }

    // Login
    val context = LocalContext.current
    val userPreferences = remember { DataStoreUserPreference(context) }
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userPreferences, context)
    )

    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory()
    )

    val searchHistoryDataStore = remember { SearchHistoryDataStore(context) }
    val searchHistoryViewModel: SearchHistoryViewModel =
        viewModel { SearchHistoryViewModel(searchHistoryDataStore) }

    AppTheme(darkTheme = darkTheme) {
        SystemUIController(isDarkTheme = darkTheme)
        MyApp(
            userModel = userViewModel,
            themeViewModel = themeViewModel,
            searchHistoryViewModel = searchHistoryViewModel,
            eventsViewModel = eventViewModel,
        )
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