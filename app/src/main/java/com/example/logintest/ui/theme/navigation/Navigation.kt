package com.example.logintest.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.ui.screens.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.logintest.ui.screens.*


@Composable
fun MyApp(viewModel: EventViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Scaffold(
                topBar = { TopAppBarHome(navController) }
            ) { paddingValues ->
                EventListScreen(vm = viewModel, contentPadding = paddingValues)
            }
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("account_info") {
            AccountInfoScreen(navController)
        }
        composable("change_password") {
            ChangePasswordScreen(navController)
        }
        composable("delete_account") {
            DeleteAccountScreen(navController)
        }
        composable("manage_subscription") {
            ManageSubscriptionScreen(navController)
        }
        composable("light_dark_mode") {
            LightDarkModeScreen(navController)
        }
        composable("logout") {
            LogoutScreen(navController)
        }
        composable("other") {
            OtherScreen(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "FenFesta Alpha",
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("settings")
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = LocalContentColor.current
                )
            }
        }
    )
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { /* Home Screen Content */ }
        composable("settings") { SettingsScreen(navController) }
        composable("account_info") { AccountInfoScreen(navController) }
        composable("change_password") { ChangePasswordScreen(navController) }
        composable("delete_account") { DeleteAccountScreen(navController) }
        composable("manage_subscription") { ManageSubscriptionScreen(navController) }
        composable("light_dark_mode") { LightDarkModeScreen(navController) }
        composable("logout") { LogoutScreen(navController) }
        composable("other") { OtherScreen(navController) }
    }
}

