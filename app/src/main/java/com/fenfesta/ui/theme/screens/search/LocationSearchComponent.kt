package com.fenfesta.ui.theme.screens.search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fenfesta.data.viewmodel.LocationViewModel
import com.fenfesta.model.LocationModel
import com.fenfesta.ui.theme.screens.minimap.MiniMap
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@OptIn(MapboxExperimental::class)
@Composable
fun LocationSearch(
    modifier: Modifier,
    locationViewModel: LocationViewModel = viewModel(),
    onLocationConfirmed: (LocationModel) -> Unit,
    goBackToPreviousPage: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSearchBarFocused by remember { mutableStateOf(false) }
    val locationData by locationViewModel.locationData.collectAsState()

    val mapViewportState = rememberMapViewportState {}

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isSearchBarFocused = it.isFocused
                },
            perCharacterSearchAction = { searchText = it },
            searchIMEAction = {
                locationViewModel.getCoords(searchText)
                keyboardController?.hide()
                isSearchBarFocused = false
            },
            clearSearch = {},
            addSearchToHistory = {},
            unfocusBar = { isSearchBarFocused = false; },
            placeHolderText = "Inserisci Via, Civico, Città"
        )

        Spacer(modifier = Modifier.height(16.dp))

        locationData?.let { location ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Location Details", style = MaterialTheme.typography.headlineSmall)
                        IconButton(
                            modifier = Modifier.size(48.dp),  // Increased size
                            onClick = {
                                onLocationConfirmed(location)
                                goBackToPreviousPage()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirm Location",
                                modifier = Modifier.size(24.dp)  // Explicit icon size
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Address: ${location.address} ${location.streetNumber}")
                    Text("City: ${location.city}")
                    Text("Latitude: ${location.lat}")
                    Text("Longitude: ${location.lon}")

                    Spacer(modifier = Modifier.height(16.dp))

                    MiniMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)  // Set an explicit height for the map
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                4.dp,
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.shapes.medium
                            ),
                        mapViewportState = mapViewportState,
                        locationData = location
                    )
                }
            }
        }
    }
}