package com.example.logintest.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logintest.data.viewmodel.EventViewModel
import kotlinx.serialization.Serializable


@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() },
        bottomBar = {
            BottomNavigationBar(navController)
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenCalendar
        ) {
            composable<ScreenCalendar> {
                EventList(modifier = Modifier.padding(innerPadding), viewModel = eventsViewModel)
            }
            composable<ScreenMap> {
                // MapScreen()
            }
        }
    }


}

@Serializable
object ScreenCalendar

@Serializable
object ScreenMap