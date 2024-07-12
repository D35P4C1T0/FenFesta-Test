package com.example.logintest.ui.theme.screens.pickers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(modifier: Modifier, onDateSelected: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var formattedDate by remember { mutableStateOf("") }

    val themeColors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)

    // Show selected date
    selectedDate?.let {
        val date = Date(it)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formattedDate = format.format(date)
        onDateSelected(formattedDate)
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            colors = themeColors,
            tonalElevation = 2.dp,
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = themeColors,
            )
        }
    }

    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        modifier = modifier,
        label = { Text("Data") },
        trailingIcon = {
            // Button to open the date picker
            TextButton(onClick = { showDatePicker = true }) {
                Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = "calendar")
            }
        }
    )

}