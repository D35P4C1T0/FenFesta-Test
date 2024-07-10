package com.example.logintest.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.data.viewmodel.SearchHistoryViewModel
import com.example.logintest.model.EventModel

@Composable
fun SearchScreen(
    modifier: Modifier,
    viewModel: EventViewModel,
    searchHistoryViewModel: SearchHistoryViewModel,
    onEventClick: (EventModel) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val searchHistory by searchHistoryViewModel.searchHistory.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSearchBarFocused by remember { mutableStateOf(false) }
    var isHistoryExpanded by remember { mutableStateOf(true) }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            placeholder = { Text("Search events...") },
            singleLine = true,
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
//                .clickable {
//                    println("Search bar clicked")
//                    isHistoryExpanded = true
//                }
                .onFocusChanged {
                    isSearchBarFocused = it.isFocused
                },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchQuery.isNotEmpty()) {
                        searchHistoryViewModel.addSearchQuery(searchQuery)
                        searchQuery = ""
                        keyboardController?.hide()
                        isSearchBarFocused = false
                    }
                }
            )
        )

        if (isSearchBarFocused) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ricerche recenti", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { isHistoryExpanded = !isHistoryExpanded }) {
                    Icon(
                        if (isHistoryExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isHistoryExpanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = isHistoryExpanded) {
                LazyColumn {
                    items(searchHistory) { query ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchQuery = query
                                    viewModel.searchEvents(query)
                                    keyboardController?.hide()
                                    isSearchBarFocused = false
                                }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(query)
                        }
                        Divider()
                    }
                }
            }

            TextButton(
                onClick = { searchHistoryViewModel.clearSearchHistory() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Scorda tutti")
            }
        }

        // results
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