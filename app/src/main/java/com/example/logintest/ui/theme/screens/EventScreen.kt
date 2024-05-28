package com.example.logintest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel

@Composable
fun EventListScreen(
    vm: EventViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val eventsData by vm.eventsData.collectAsState()

    LazyColumn(contentPadding = contentPadding, modifier = modifier) {
        items(eventsData) { event ->
            EventCard(
                event = event
            )
        }
    }
}

@Composable
fun EventCard(event: EventModel) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Event Name: ${event.name}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Description: ${event.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Location: ${event.location}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(text = "Date: ${event.date}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Created At: ${event.created_at}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Capacity: ${event.capacity}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Capacity Left: ${event.capacity_left}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Creator ID: ${event.creator}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
