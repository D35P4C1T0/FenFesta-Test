package com.example.logintest.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.logintest.R
import com.example.logintest.data.viewmodel.LocationViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.model.LocationModel
import com.example.logintest.model.UserModel
import com.example.logintest.ui.theme.screens.pickers.MyDatePicker
import com.example.logintest.ui.theme.screens.pickers.MyTimePicker
import com.example.logintest.ui.theme.screens.search.LocationSearch
import com.example.logintest.view.utils.DateTimeHelper.createLocalDateTime
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    creatorUser: UserModel,
    locationViewModel: LocationViewModel,
    onCreateEvent: (EventModel) -> Unit,
) {
    var showLocationPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var eventName by remember { mutableStateOf("") }
    var eventTags by remember { mutableStateOf("") } // controllo se è una lista di tag
    var eventDescription by remember { mutableStateOf("") }
    var eventDay by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventCapacity by remember { mutableStateOf("") }

    var eventLocation by remember { mutableStateOf<LocationModel?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Row 1: Event Name
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Nome Evento") },
            colors = OutlinedTextFieldDefaults.colors()
        )

        // Row 2: Date and Time Pickers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MyDatePicker(Modifier.fillMaxWidth(), onDateSelected = { eventDay = it })
            }
            Box(modifier = Modifier.weight(1f)) {
                MyTimePicker(Modifier.fillMaxWidth(), onTimeSelected = { eventTime = it })
            }
        }

        // Row 4: Description
        OutlinedTextField(
            value = eventDescription,
            onValueChange = { eventDescription = it },
            label = { Text("Descrizione") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5
        )

        // TODO: non va la virgola
        OutlinedTextField(
            value = eventTags,
            onValueChange = { newValue ->
//                val newChar = newValue[eventTags.length]
//                println("New character inserted: '$newChar', ASCII code: ${newChar.code}")
                val processedValue = newValue
                    .replace(
                        Regex("[^a-zA-Z0-9,]"),
                        ""
                    ) // Remove all non-alphanumeric characters except commas
                    .replace(
                        Regex(",{2,}"),
                        ","
                    ) // Replace two or more consecutive commas with a single comma
                    .trimStart(',') // Remove leading commas
//                    .trimEnd(',') // Remove trailing commas

                if (processedValue != eventTags) {
                    eventTags = processedValue
                }
            },
            label = { Text("Tags") },
            placeholder = { Text("Separa i tag con le virgole") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = eventLocation?.toString() ?: "",
                    onValueChange = { /* Read-only, so no change handler */ },
                    readOnly = true,
                    label = { Text("Indirizzo") },
                    trailingIcon = {
                        IconButton(onClick = {
                            showLocationPicker = true
                            // navController.navigateWithDefaultOptions("search_address")
                            // apri dialogo
                        }) {
                            Icon(
                                imageVector = Icons.Default.TravelExplore,
                                contentDescription = "Select Location",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                )
            }
            Box(modifier = Modifier.weight(0.4f)) {
                IntegerOutlinedTextField(
                    value = eventCapacity,
                    onValueChange = { eventCapacity = it },
                    label = "Capacità"
                )
            }
        }

        if (showLocationPicker) {
            LocationPickerDialog(
                viewModel = locationViewModel,
                onDismiss = { showLocationPicker = false; locationViewModel.clearCoords() },
                onLocationSelected = {
                    eventLocation = it
                    showLocationPicker = false
                    println("location confirmed: $eventLocation")
                }
            )
        }

        // Row 5: Image and Location
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Immagine", modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    IconButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { /* TODO: Implement image picker */ }
                    ) {
                        Icon(
                            painter = rememberAsyncImagePainter(R.drawable.logo_fen_festa_monocromo),
                            contentDescription = "Add Image",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }

        // Create Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if (eventName.isEmpty()
                        || eventTags.isEmpty()
                        || eventDescription.isEmpty()
                        || eventDay.isEmpty()
                        || eventTime.isEmpty()
                        || eventCapacity.isEmpty()
//                        ||
                    ) {
                        Toast.makeText(context, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Check if the event date is in the future
                    val eventDateTime = createLocalDateTime(eventDay, eventTime)
                    val currentDateTime = LocalDateTime.now()

                    if (eventDateTime.isBefore(currentDateTime)) {
                        Toast.makeText(
                            context,
                            "La data dell'evento deve essere nel futuro",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    eventLocation?.let {
                        val newEvent = EventModel(
                            name = eventName,
                            description = eventDescription,
                            creator = creatorUser.id,
                            date = eventDateTime,
                            location = eventLocation!!.address,
                            lat = eventLocation!!.lat.toString(),
                            lon = eventLocation!!.lon.toString(),
                            capacity = eventCapacity.toIntOrNull() ?: 0,
                            capacity_left = eventCapacity.toIntOrNull() ?: 0,
                            tags = eventTags.trimEnd(','), // remove trailing comma
                            created_at = LocalDateTime.now().toString(),
                        )
                        println("new event: $newEvent")
                        onCreateEvent(newEvent) // spara in su l'eventp
                    }
                },
                modifier = Modifier.size(150.dp, 50.dp)
            ) {
                Text(text = "Crea", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun IntegerOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    maxDigits: Int = 5
) {

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var internalValue by remember { mutableStateOf(value) }

    OutlinedTextField(
        value = internalValue,
//        onValueChange = { newValue ->
//
//            if (newValue.isEmpty() || (newValue.toIntOrNull() != null && newValue.length <= maxDigits)) {
//                internalValue = newValue
//                onValueChange(newValue)
//            }
//        },
        onValueChange = { newValue: String ->
            if (newValue.isEmpty()) {
                internalValue = newValue
                onValueChange(newValue)
                isError = false
                errorMessage = ""
            } else if (newValue.toIntOrNull() != null) {
                if (newValue.length <= maxDigits) {
                    onValueChange(newValue)
                    internalValue = newValue
                    isError = false
                    errorMessage = ""
                } else {
                    isError = true
                    errorMessage = "Maximum $maxDigits digits allowed"
                    internalValue = ""
                }
            } else {
                isError = true
                errorMessage = "Invalid integer"
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor =
            if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
        ),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        supportingText = @Composable {
            Text(
                text = if (isError) errorMessage else "${value.length}/$maxDigits digits",
                color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
            )
        },
        modifier = modifier
    )
}

@Composable
fun LocationPickerDialog(
    viewModel: LocationViewModel,
    onDismiss: () -> Unit,
    onLocationSelected: (LocationModel) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false // This allows the dialog to be full-width
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LocationSearch(
                modifier = Modifier.fillMaxWidth(),
                locationViewModel = viewModel,
                onLocationConfirmed = onLocationSelected,
                goBackToPreviousPage = onDismiss
            )
        }
    }
}