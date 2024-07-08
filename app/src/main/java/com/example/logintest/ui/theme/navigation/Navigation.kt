package com.example.logintest.ui.theme.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.logintest.ui.screens.AccountInfoScreen
import com.example.logintest.data.viewmodel.ThemeOption
import com.example.logintest.data.viewmodel.ThemeViewModel
import com.example.logintest.ui.theme.screens.AppInfoScreen
import com.example.logintest.ui.theme.screens.ChangePasswordScreen
import com.example.logintest.ui.theme.screens.DeleteAccountScreen
import com.example.logintest.ui.theme.screens.LogoutScreen
import com.example.logintest.ui.theme.screens.ManageSubscriptionScreen
import com.example.logintest.ui.theme.screens.OtherScreen
import com.example.logintest.ui.theme.screens.SettingsScreen
import com.example.logintest.ui.theme.screens.MapScreen
import com.example.logintest.ui.theme.screens.SearchScreen
import com.example.logintest.ui.theme.screens.ShareAppScreen
import com.example.logintest.ui.theme.screens.SupportScreen
import com.example.logintest.ui.theme.screens.ThemeSelector
import com.example.logintest.ui.theme.screens.calendar.Calendar
import com.example.logintest.ui.theme.screens.event.EventDetailsScreen
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
fun MyApp(userModel: UserViewModel, themeViewModel: ThemeViewModel) {

    val navController = rememberNavController()
    val mapViewportState = rememberMapViewportState {}
    var firstLaunch by remember {
        mutableStateOf(FirstLaunch)
    }

    val eventsViewModel = viewModel<EventViewModel>()
    val themeOption by themeViewModel.themeOption.collectAsState()

    var currentScreen by remember(navController) {
        mutableStateOf(Screen.Home)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarHome(navController = navController, currentScreen = currentScreen) },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { innerPadding ->
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

                    currentScreen = Screen.Settings

                    val eventId = backStackEntry.arguments?.getInt("eventId")
                    val viewModel: EventViewModel = viewModel()
                    val event by viewModel.selectedEvent.collectAsState()

                    LaunchedEffect(eventId) {
                        eventId?.let { viewModel.fetchEventById(it) }
                    }

                    event?.let {
                        EventDetailsScreen(
                            modifier = Modifier.padding(innerPadding),
                            event = it,
                            onBackPress = { navController.popBackStack() }
                        )
                    }
                }

                composable("settings") {
                    currentScreen = Screen.Settings
                    SettingsScreen(navController)
                }
                composable("account_info") {
                    AccountInfoScreen(Modifier.padding(innerPadding), navController, userModel)
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
                    ThemeSelector(
                        modifier = Modifier.padding(innerPadding),
                        currentTheme = themeOption,
                        onThemeSelected = { themeViewModel.setThemeOption(it) }
                    )
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
                composable("search_page") {
                    currentScreen = Screen.Settings
                    SearchScreen(
                        Modifier.padding(innerPadding),
                        eventsViewModel,
                        onEventClick = { event ->
                            navController.navigate("eventDetails/${event.id}")
                        })
                }
            }
        })
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
        navigationIcon = @Composable {
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
        },
        actions = @Composable {
            when (currentScreen) {
                Screen.Home -> {
                    IconButton(onClick = { navController.navigate("search_page") }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Settings"
                        )
                    }
                }

                Screen.Settings -> {} // no icon
            }
        }
    )
}
