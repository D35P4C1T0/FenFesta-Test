package com.example.logintest.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.R
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.ui.calendar.Calendar
import com.example.logintest.ui.screens.*
import com.example.logintest.ui.theme.screens.CreateEventScreen
import com.example.logintest.ui.theme.screens.MapScreen
import com.example.logintest.ui.theme.screens.ShareAppScreen
import com.example.logintest.ui.theme.screens.SupportScreen
import com.example.logintest.view.EventDetailsScreen
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
    var firstLaunch by remember { mutableStateOf(FirstLaunch) }
    val eventsViewModel = viewModel<EventViewModel>()
    var currentScreen by remember(navController) { mutableStateOf(Screen.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarHome(navController = navController, currentScreen = currentScreen) },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            if (currentScreen != Screen.Settings) {
                FloatingActionButton(
                    onClick = { navController.navigate("create_event") },
                    containerColor = MaterialTheme.colorScheme.primary // Verde
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create Event")
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "mapbox",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable("mapbox") {
                    currentScreen = Screen.Home
                    MapScreen(
                        modifier = Modifier.padding(innerPadding),
                        mapViewportState,
                        firstLaunch,
                        viewModel = eventsViewModel,
                        navController = navController
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
                    SettingsScreen(Modifier.padding(innerPadding), navController)
                }
                composable("account_info") {
                    AccountInfoScreen(Modifier.padding(innerPadding), navController, userModel)
                }
                composable("change_password") {
                    ChangePasswordScreen(Modifier.padding(innerPadding), navController, userModel)
                }
                composable("delete_account") {
                    DeleteAccountScreen(Modifier.padding(innerPadding), navController, userModel)
                }
                composable("manage_subscription") {
                    ManageSubscriptionScreen(Modifier.padding(innerPadding), navController, userModel)
                }
                composable("light_dark_mode") {
                    LightDarkModeScreen(navController, Modifier.padding(innerPadding))
                }
                composable("logout") {
                    LogoutScreen(Modifier.padding(innerPadding), navController)
                }
                composable("other") {
                    OtherScreen(Modifier.padding(innerPadding), navController)
                }
                composable("create_event") {
                    currentScreen = Screen.Settings
                    CreateEventScreen(navController, Modifier.padding(innerPadding))
                }
                composable("app_info") {
                    AppInfoScreen(Modifier.padding(innerPadding), navController)
                }
                composable("support") {
                    SupportScreen(Modifier.padding(innerPadding), navController)
                }
                composable("share_app") {
                    ShareAppScreen(Modifier.padding(innerPadding), navController)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(
    currentScreen: Screen,
    navController: NavHostController,
) {
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo_fen_festa),
                contentDescription = "logo",
                modifier = Modifier.size(72.dp)
            )
        },
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
}
