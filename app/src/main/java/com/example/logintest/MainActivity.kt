package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.ui.theme.LoginTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel: EventViewModel by viewModels()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopAppBar() }, content = {
                    EventList(vm = viewModel, contentPadding = it)
                })
            }
        }
    }

    @Composable
    fun EventList(
        vm: EventViewModel,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        modifier: Modifier = Modifier
    ) {
        val eventsData by vm.eventsData.collectAsState()

        LazyColumn(contentPadding = contentPadding, modifier = modifier) {
            items(eventsData) { event ->
                // Card with event details
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBar(modifier: Modifier = Modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "FenFesta Alpha",
                    style = MaterialTheme.typography.headlineMedium,
                )
            },
            modifier = modifier
        )
    }
}

