package com.example.logintest.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    viewModel: EventViewModel,
    onEventClick: (EventModel) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = modifier) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.isNotEmpty()) {
                    viewModel.searchEvents(it)
                } else {
                    viewModel.clearSearch()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search events...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = ""
                        viewModel.clearSearch()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true
        )

        LazyColumn {
            items(searchResults) { event ->
                ListItem(
                    headlineContent = { Text(event.name) },
                    supportingContent = { Text("${event.date} at ${event.location}") },
                    modifier = Modifier
                        .clickable { onEventClick(event) }
                        .fillMaxWidth()
                )
                Divider()
            }
        }
    }
}