package com.example.logintest.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.ui.calendar.Calendar
import com.example.logintest.view.components.BottomNavigationBar
import com.example.logintest.view.components.EventList
import com.example.logintest.view.components.TopAppBar
import com.example.logintest.view.utils.FirstLaunch
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun MainScreen() {

    val eventsViewModel = viewModel<EventViewModel>()

//    Scaffold(modifier = Modifier.fillMaxSize(),
//        topBar = { TopAppBar() },
//        bottomBar = {
//            BottomNavigationBar()
//        }) { innerPadding ->
//        EventList(modifier = Modifier.padding(innerPadding), viewModel = eventsViewModel)
//
//    }

    val navController = rememberNavController()

    val mapViewportState = rememberMapViewportState {}

    var firstLaunch by remember {
        mutableStateOf(FirstLaunch)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() },
        bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenMap,
            // no animation for now
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable<ScreenMap> {
                MapScreen(
                    modifier = Modifier.padding(innerPadding),
                    mapViewportState,
                    firstLaunch,
                    viewModel = eventsViewModel
                )
            }
            composable<ScreenCalendar> {
                Calendar(modifier = Modifier.padding(innerPadding), onEventClick = { event ->
                    navController.navigate("eventDetails/${event.id}")
                })
//                EventList(
//                    modifier = Modifier.padding(innerPadding),
//                    viewModel = eventsViewModel,
//                )
            }

            composable(
                "eventDetails/{eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.IntType })
            ) { backStackEntry ->
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
        }
    }
}

//fun getEventById(eventId: Int?): EventModel? {
//    // TODO: Fetch the event details based on the ID
//}

@Serializable
object ScreenCalendar

@Serializable
object ScreenMap
