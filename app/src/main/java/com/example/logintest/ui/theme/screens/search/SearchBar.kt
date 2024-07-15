package com.example.logintest.ui.theme.screens.search

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction


@Composable
fun SearchBar(
    modifier: Modifier,
    unfocusBar: (() -> Unit)? = null,
    perCharacterSearchAction: ((String) -> Unit)? = null,
    clearSearch: (() -> Unit)? = null,
    searchIMEAction: ((String) -> Unit)? = null,
    addSearchToHistory: ((String) -> Unit)? = null,
    placeHolderText: String,
) {

    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = searchQuery,
        placeholder = { Text(placeHolderText) },
        singleLine = true,
        onValueChange = {
            searchQuery = it
            if (it.isNotEmpty()) {
                if (perCharacterSearchAction != null) {
                    perCharacterSearchAction(it)
                }
            } else {
                if (clearSearch != null) {
                    clearSearch()
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.isNotBlank()) {
                    if (searchIMEAction != null) {
                        searchIMEAction(searchQuery)
                    }
                    if (addSearchToHistory != null) {
                        addSearchToHistory(searchQuery)
                    }
                    if (unfocusBar != null) {
                        unfocusBar()
                    }
                }
            }
        ),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}