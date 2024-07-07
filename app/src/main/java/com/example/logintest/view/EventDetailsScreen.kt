package com.example.logintest.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logintest.model.EventModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable

@Composable
fun EventDetailScreen(event: EventModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = event.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: ${event.date}")
        Text(text = "Location: ${event.location}")
        Text(text = "Description: ${event.description}")
        // Add more event details as needed
    }
}