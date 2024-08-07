package com.fenfesta.ui.theme.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fenfesta.R
import com.fenfesta.data.viewmodel.EventViewModel
import com.fenfesta.data.viewmodel.LocationViewModel
import com.fenfesta.data.viewmodel.LoginState
import com.fenfesta.data.viewmodel.SearchHistoryViewModel
import com.fenfesta.data.viewmodel.ThemeViewModel
import com.fenfesta.data.viewmodel.UserViewModel
import com.fenfesta.ui.theme.screens.AccountInfoScreen
import com.fenfesta.ui.theme.screens.AppInfoScreen
import com.fenfesta.ui.theme.screens.ChangePasswordScreen
import com.fenfesta.ui.theme.screens.CreateEventScreen
import com.fenfesta.ui.theme.screens.DeleteAccountScreen
import com.fenfesta.ui.theme.screens.LoginPage
import com.fenfesta.ui.theme.screens.LogoutScreen
import com.fenfesta.ui.theme.screens.ManageSubscriptionScreen
import com.fenfesta.ui.theme.screens.MapScreen
import com.fenfesta.ui.theme.screens.OtherScreen
import com.fenfesta.ui.theme.screens.RegistrationScreen
import com.fenfesta.ui.theme.screens.ReservationsListScreen
import com.fenfesta.ui.theme.screens.SettingsScreen
import com.fenfesta.ui.theme.screens.ShareAppScreen
import com.fenfesta.ui.theme.screens.SupportScreen
import com.fenfesta.ui.theme.screens.ThemeSelector
import com.fenfesta.ui.theme.screens.calendar.Calendar
import com.fenfesta.ui.theme.screens.event.EventDetailsScreen
import com.fenfesta.ui.theme.screens.search.EventSearchScreen
import com.fenfesta.ui.utils.AdvLauncher
import com.fenfesta.ui.utils.NavAnimations.enterTransition
import com.fenfesta.ui.utils.NavAnimations.exitTransition
import com.fenfesta.ui.utils.NavAnimations.popEnterTransition
import com.fenfesta.ui.utils.NavAnimations.popExitTransition
import com.fenfesta.view.components.BottomNavigationBar
import com.fenfesta.view.utils.FirstLaunch
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

enum class Screen {
    Home,
    Settings
}


@MapboxExperimental
@OptIn(MapboxExperimental::class)
@Composable
fun MyApp(
    userViewModel: UserViewModel,
    themeViewModel: ThemeViewModel,
    eventsViewModel: EventViewModel,
    searchHistoryViewModel: SearchHistoryViewModel,
    locationViewModel: LocationViewModel,
) {

    Log.d("Compose", "Nav is recomposing")
    val context = LocalContext.current
    val navController = rememberNavController()
    val firstLaunch by remember { mutableStateOf(FirstLaunch) }
    val themeOption by themeViewModel.themeOption.collectAsState()
    val eventsList by eventsViewModel.events.collectAsState()
    val loginState by userViewModel.loginState.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    val mapViewportState = rememberMapViewportState {}

    //println("NAV view port ${mapViewportState.mapViewportStatus}")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarHome(
                currentScreen = currentScreen,
                onSettingsClick = remember(navController) {
                    {
                        navController.navigateWithDefaultOptions(
                            "settings"
                        )
                    }
                },
                onBackClick = remember(navController) { { navController.navigateUp() } },
                onSearchClick = remember(navController) {
                    {
                        navController.navigateWithDefaultOptions(
                            "search_page"
                        )
                    }
                },
            )
        },
        bottomBar = {
            when (currentScreen) {
                Screen.Home -> BottomNavigationBar(navController = navController)
                Screen.Settings -> {}
            }
        },
        floatingActionButton = {
            if (currentScreen != Screen.Settings) {
                when (loginState) {
                    is LoginState.Success -> {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                navController.navigateWithDefaultOptions("create_event")
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Create Event"
                                )
                            }
                        )
                    }

                    else -> {}
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "mapbox",
                enterTransition = { enterTransition },
                exitTransition = { exitTransition },
                popEnterTransition = { popEnterTransition },
                popExitTransition = { popExitTransition }
            ) {
                composable(
                    route = "mapbox",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "settings" -> slideInHorizontally(initialOffsetX = { it })
                            "calendar" -> slideInHorizontally(initialOffsetX = { -it })
                            else -> null
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "settings" -> slideOutHorizontally(targetOffsetX = { it })
                            "calendar" -> slideOutHorizontally(targetOffsetX = { -it })
                            else -> null
                        }
                    }) {
                    currentScreen = Screen.Home
                    MapScreen(
                        modifier = Modifier.padding(innerPadding),
                        mapViewportState = mapViewportState,
                        eventsList = eventsList,
                        updateEvents = { eventsViewModel.fetchEvents() },
                        isFirstLaunch = firstLaunch,
                        onMarkerClick = { eventId ->
                            navController.navigateWithDefaultOptions("eventDetails/$eventId")
                        }
                    )
                }
                composable(
                    route = "calendar",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "settings", "mapbox" -> slideInHorizontally(initialOffsetX = { it })
                            else -> null
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "settings", "mapbox" -> slideOutHorizontally(targetOffsetX = { it })
                            else -> null
                        }
                    }) {
                    currentScreen = Screen.Home
                    Calendar(
                        modifier = Modifier.padding(innerPadding),
                        eventsList = eventsList,
                        onEventClick = { event ->
                            navController.navigateWithDefaultOptions("eventDetails/${event.id}")
                        },
                        updateEvents = { eventsViewModel.fetchEvents() },
                        updateCurrentMonth = { eventsViewModel.updateCurrentMonth(it) },
                    )
                }


                composable(
                    "eventDetails/{eventId}",
                    arguments = listOf(navArgument("eventId") { type = NavType.IntType })
                ) { backStackEntry ->

                    currentScreen = Screen.Settings
                    val eventId = backStackEntry.arguments?.getInt("eventId")
                    val event by eventsViewModel.selectedEvent.collectAsState()

                    val hideJoinButton by eventsViewModel.isEventReserved.collectAsState()

                    LaunchedEffect(eventId) {
                        eventId?.let { eventsViewModel.fetchEventById(it) }
                    }

                    event?.let {
                        EventDetailsScreen(
                            modifier = Modifier.padding(innerPadding),
                            event = it,
                            eventViewModel = eventsViewModel,
                            userViewModel = userViewModel,
                            onBackPress = { navController.popBackStack() },
                            onReserveClick = { },
                            hideJoinButton = hideJoinButton,
                        )
                    }
                }
                composable(
                    route = "settings",
                    enterTransition = {
                        when (initialState.destination.route) {
                            "mapbox", "calendar" -> slideInHorizontally(initialOffsetX = { -it })
                            else -> null
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            "mapbox", "calendar" -> slideOutHorizontally(targetOffsetX = { -it })
                            else -> null
                        }
                    }
                ) {
                    currentScreen = Screen.Settings
                    SettingsScreen(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        userViewModel = userViewModel,
                        currentTheme = themeOption,
                        onThemeChanged = { themeViewModel.setThemeOption(it) },
                    )
                }
                composable("account_info") {
                    AccountInfoScreen(Modifier.padding(innerPadding), userViewModel, navController)
                }
                composable("change_password") {
                    ChangePasswordScreen(
                        Modifier.padding(innerPadding),
                        navController,
                        userViewModel
                    )
                }
                composable("delete_account") {
                    DeleteAccountScreen(
                        Modifier.padding(innerPadding),
                        navController,
                        userViewModel
                    )
                }
                composable("manage_subscription") {
                    ManageSubscriptionScreen(
                        Modifier.padding(innerPadding),
                        navController,
                        userViewModel
                    )
                }
//                composable("light_dark_mode") {
//                    ThemeSelector(
//                        currentTheme = themeOption,
//                        onThemeSelected = { themeViewModel.setThemeOption(it) }
//                    )
//                }
                composable("login") {
                    LoginPage(
                        Modifier.padding(innerPadding),
                        userViewModel = userViewModel,
                        onLoginSuccess = { },
                        onNavigateToRegister = { navController.navigateWithDefaultOptions("register") },
                        goBackToHome = { navController.navigateWithDefaultOptions("mapbox") }
                    )
                }

                composable("register") {
                    RegistrationScreen(
                        userViewModel,
                        onRegistrationSuccess = {
                            Toast.makeText(context, "Account creato", Toast.LENGTH_SHORT).show()
                            navController.navigateWithDefaultOptions("login")
                        },
                        onNavigateToLogin = { navController.navigateWithDefaultOptions("login") }
                    )
                }

                composable("logout") {
                    LogoutScreen(Modifier.padding(innerPadding), navController, userViewModel)
                }
                composable("other") {
                    OtherScreen(Modifier.padding(innerPadding), navController)
                }
                composable("create_event") {
                    currentScreen = Screen.Settings
                    userData?.let {
                        CreateEventScreen(
                            creatorUser = userData!!,
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            locationViewModel = locationViewModel,
                            onCreateEvent = {
                                eventsViewModel.createEvent(it)
                                AdvLauncher.launch(
                                    context = context,
                                    onAdLaunched = { navController.popBackStack() }
                                )
                            }
                        )
                    }
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
                composable("search_page") {
                    currentScreen = Screen.Settings
                    EventSearchScreen(
                        Modifier.padding(innerPadding),
                        eventsViewModel,
                        searchHistoryViewModel,
                        onEventClick = { event ->
                            navController.navigateWithDefaultOptions("eventDetails/${event.id}")
                        })
                }
                composable("user_reservations") {
                    ReservationsListScreen(
                        Modifier.padding(innerPadding),
                        eventsViewModel,
                        onEventClick = { navController.navigateWithDefaultOptions("eventDetails/${it.id}") })
                }

//                composable("search_address") {
//                    currentScreen = Screen.Settings
//                    LocationSearch(
//                        Modifier.padding(innerPadding),
//                        locationViewModel,
//                        onLocationConfirmed = {},
//                        goBackToPreviousPage = { navController.popBackStack() })
//                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(
    currentScreen: Screen,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
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
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }

                Screen.Settings -> {
                    IconButton(onClick = onBackClick) {
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
                    IconButton(onClick = onSearchClick) {
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

fun NavController.navigateWithDefaultOptions(route: String) {
    val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setRestoreState(true)
        .build()

    this.navigate(route, navOptions)
}
