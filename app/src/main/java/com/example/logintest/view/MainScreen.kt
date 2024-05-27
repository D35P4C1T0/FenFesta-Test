package com.example.logintest.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logintest.data.viewmodel.EventViewModel
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

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() },
        bottomBar = {
            BottomNavigationBar(navController)
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenMap
        ) {
            composable<ScreenMap> {
                MapScreen(modifier = Modifier.padding(innerPadding), mapViewportState, firstLaunch, viewModel = eventsViewModel)
            }
            composable<ScreenCalendar> {
                EventList(modifier = Modifier.padding(innerPadding), viewModel = eventsViewModel)
            }
        }
    }
}

@Serializable
object ScreenCalendar

@Serializable
object ScreenMap

