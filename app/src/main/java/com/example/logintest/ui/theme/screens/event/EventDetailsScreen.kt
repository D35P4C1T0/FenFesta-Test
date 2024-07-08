package com.example.logintest.ui.theme.screens.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logintest.model.EventModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.format.DateTimeFormatter

@Composable
fun EventDetailsScreen(event: EventModel, onBackPress: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background color based on event color
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top bar with back button and event name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Event details
            EventDetailItem(
                icon = Icons.Default.DateRange,
                label = "Date",
                value = event.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"))
            )
            EventDetailItem(
                icon = Icons.Default.LocationOn,
                label = "Location",
                value = event.location
            )
            EventDetailItem(
                icon = Icons.Default.Person,
                label = "Capacity",
                value = "${event.capacity_left}/${event.capacity} spots available"
            )
            EventDetailItem(
                icon = Icons.Default.Tag,
                label = "Tags",
                value = event.tags
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Event description
            Text(
                text = "Description",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = event.description)

            Spacer(modifier = Modifier.height(32.dp))

            // Action button
            Button(
                onClick = { /* TODO: Implement join/leave event logic */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Join Event")
            }
        }
    }
}

@Composable
fun EventDetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = label, style = MaterialTheme.typography.titleSmall)
            Text(text = value, style = MaterialTheme.typography.bodySmall)
        }
    }
}