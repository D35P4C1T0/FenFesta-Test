package com.example.logintest.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.ui.calendar.Calendar
import com.example.logintest.ui.screens.AccountInfoScreen
import com.example.logintest.ui.screens.AppInfoScreen
import com.example.logintest.ui.screens.ChangePasswordScreen
import com.example.logintest.ui.screens.DeleteAccountScreen
import com.example.logintest.ui.screens.LightDarkModeScreen
import com.example.logintest.ui.screens.LogoutScreen
import com.example.logintest.ui.screens.ManageSubscriptionScreen
import com.example.logintest.ui.screens.OtherScreen
import com.example.logintest.ui.screens.SettingsScreen
import com.example.logintest.ui.theme.screens.ShareAppScreen
import com.example.logintest.ui.theme.screens.SupportScreen
import com.example.logintest.view.EventDetailsScreen
import com.example.logintest.ui.theme.screens.MapScreen
import com.example.logintest.view.components.BottomNavigationBar
import com.example.logintest.view.utils.FirstLaunch
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

enum class Screen {
    Home,
    Settings
}

@OptIn(MapboxExperimental::class)
@Composable
fun MyApp(userModel: UserViewModel) {
    val navController = rememberNavController()
    val mapViewportState = rememberMapViewportState {}
    var firstLaunch by remember {
        mutableStateOf(FirstLaunch)
    }

    val eventsViewModel = viewModel<EventViewModel>()

    var currentScreen by remember(navController) {
        mutableStateOf(Screen.Home)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarHome(navController = navController, currentScreen = currentScreen) },
        bottomBar = {
            BottomNavigationBar(navController)
        }) { innerPadding ->
        NavHost(navController = navController,
            startDestination = "mapbox",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }) {
            composable("mapbox") {
                currentScreen = Screen.Home
                MapScreen(
                    modifier = Modifier.padding(innerPadding),
                    mapViewportState,
                    firstLaunch,
                    viewModel = eventsViewModel,
                    navController = navController,
                )
            }
            composable("calendar") {
                currentScreen = Screen.Home
                Calendar(modifier = Modifier.padding(innerPadding), onEventClick = { event ->
                    navController.navigate("eventDetails/${event.id}")
                })
            }

            composable(
                "eventDetails/{eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.IntType })
            ) { backStackEntry ->

                currentScreen = Screen.Home

                val eventId = backStackEntry.arguments?.getInt("eventId")
                val viewModel: EventViewModel = viewModel()
                val event by viewModel.selectedEvent.collectAsState()

                LaunchedEffect(eventId) {
                    eventId?.let { viewModel.fetchEventById(it) }
                }

                event?.let {
                    EventDetailsScreen(
                        event = it,
                        onBackPress = { navController.popBackStack() }
                    )
                }
            }

            composable("settings") {
                currentScreen = Screen.Settings
                SettingsScreen(navController)
            }
            composable("account_info") { backStackEntry ->
                AccountInfoScreen(navController, userModel)
            }
            composable("change_password") {
                ChangePasswordScreen(navController)
            }
            composable("delete_account") {
                DeleteAccountScreen(navController, userModel)
            }
            composable("manage_subscription") {
                ManageSubscriptionScreen(navController, userModel)
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
            composable("app_info") {
                AppInfoScreen(navController)
            }
            composable("support") {
                SupportScreen(navController)
            }
            composable("share_app") {
                ShareAppScreen(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(
    currentScreen: Screen,
    navController: NavHostController,
) {
    CenterAlignedTopAppBar(
        title = { Text("My App") },
        navigationIcon = {
            when (currentScreen) {
                Screen.Home -> {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
                Screen.Settings -> {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        }
    )

//    CenterAlignedTopAppBar(
//        title = {
//            Text(
//                text = "FenFesta Alpha",
//                style = MaterialTheme.typography.headlineMedium,
//            )
//        },
//        navigationIcon = {
//            IconButton(onClick = {
//                navController.navigate("settings")
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.Settings,
//                    contentDescription = "Settings",
//                    tint = LocalContentColor.current
//                )
//            }
//        }
//    )
}
