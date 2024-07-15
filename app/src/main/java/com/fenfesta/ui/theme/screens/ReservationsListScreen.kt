package com.fenfesta.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fenfesta.data.viewmodel.EventViewModel
import com.fenfesta.model.EventModel
import com.fenfesta.view.components.EventCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsListScreen(
    modifier: Modifier,
    eventViewModel: EventViewModel,
    onEventClick: (EventModel) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = false) {
        eventViewModel.listAllReservedEvents()
    }

    val reservationsList by eventViewModel.eventsReserved.collectAsState()

    // Separate upcoming and past events
    val currentDateTime = LocalDateTime.now()
    val (upcomingEvents, pastEvents) = reservationsList.partition { event ->
        event.date.isAfter(currentDateTime)
    }

    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            coroutineScope.launch {
                delay(500)
                eventViewModel.listAllReservedEvents()
                isRefreshing = false
            }
        },
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            item {
                Text(
                    "Prossimi Eventi",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
            }

            items(upcomingEvents) { event ->
                Box(modifier = Modifier.padding(8.dp)) {
                    EventCard(
                        event = event,
                        onEventClick = { onEventClick(it) }
                    )
                }
            }

            item {
                Text(
                    "Eventi Passati",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp)
                )
            }

            items(pastEvents) { event ->
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