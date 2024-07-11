package com.example.logintest.ui.theme.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.data.viewmodel.LocationViewModel
import com.example.logintest.model.LocationModel

@Composable
fun SearchBarWithResults(
    modifier: Modifier,
    locationViewModel: LocationViewModel = viewModel(),
    onLocationConfirmed: (LocationModel) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    val locationData by locationViewModel.locationData.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isSearchFocused = it.isFocused },
            placeholder = { Text("Enter address") },
            leadingIcon = {
                IconButton(onClick = {
                    if (isSearchFocused) {
                        // Handle back button action
                        isSearchFocused = false
                    }
                }) {
                    Icon(
                        imageVector = if (isSearchFocused) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Search,
                        contentDescription = if (isSearchFocused) "Back" else "Search"
                    )
                }
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Perform search action here
                    if (searchText.isNotBlank()) {
                        locationViewModel.getCoords(searchText)
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the results in an info box
        locationData?.let { location ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)  // Card takes 70% of screen width
                    .padding(end = 28.dp)  // Make space for half of the FAB
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Location Details", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Address: ${location.address} ${location.streetNumber}")
                        Text("City: ${location.city}")
                        Text("Latitude: ${location.lat}")
                        Text("Longitude: ${location.lon}")
                    }
                }

                // Confirmation button
                FloatingActionButton(
                    onClick = { onLocationConfirmed(location) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 28.dp)  // Move half of the FAB outside the card
                        .zIndex(1f)  // Ensure the FAB is drawn on top of the card
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm Location"
                    )
                }
            }
        }
    }
}