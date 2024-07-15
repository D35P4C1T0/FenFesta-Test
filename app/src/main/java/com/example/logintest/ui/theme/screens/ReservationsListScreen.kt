package com.example.logintest.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.view.components.EventCard

@Composable
fun ReservationsListScreen(
    modifier: Modifier,
    eventViewModel: EventViewModel,
    onEventClick: (EventModel) -> Unit
) {

    LaunchedEffect(key1 = false) {
        eventViewModel.listAllReservedEvents()
    }

    val reservationsList by eventViewModel.eventsReserved.collectAsState()
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp)) {
        reservationsList.forEach { event ->
            item {
                Box(modifier = Modifier.padding(8.dp)) {
                    EventCard(
                        event = event,
                        onEventClick = { onEventClick(it) }
                    )
                }
            }
        }
    }
}