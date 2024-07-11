package com.example.logintest.ui.theme.screens.search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.data.viewmodel.LocationViewModel
import com.example.logintest.model.LocationModel
import com.example.logintest.ui.theme.screens.minimap.MiniMap
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@OptIn(MapboxExperimental::class)
@Composable
fun SearchBarWithResultsScreen(
    modifier: Modifier,
    locationViewModel: LocationViewModel = viewModel(),
    onLocationConfirmed: (LocationModel) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    val locationData by locationViewModel.locationData.collectAsState()

    val mapViewportState = rememberMapViewportState {}

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
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text("Location Details", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Address: ${location.address} ${location.streetNumber}")
                            Text("City: ${location.city}")
                            Text("Latitude: ${location.lat}")
                            Text("Longitude: ${location.lon}")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            IconButton(
                                modifier = Modifier.size(55.dp),
                                onClick = { onLocationConfirmed(location) },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Confirm Location"
                                )
                            }
                        }
                    }
                    MiniMap(
                        modifier = Modifier.clip(MaterialTheme.shapes.medium).padding(16.dp).border(4.dp, MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium),
                        mapViewportState = mapViewportState,
                        locationViewModel = locationViewModel
                    )

                }
            }
        }
    }
}