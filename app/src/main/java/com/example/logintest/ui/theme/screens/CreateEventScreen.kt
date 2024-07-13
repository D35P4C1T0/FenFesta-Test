package com.example.logintest.ui.theme.screens

//import android.app.DatePickerDialog

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.logintest.MainActivity
import com.example.logintest.R
import com.example.logintest.data.viewmodel.LocationViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.model.UserModel
import com.example.logintest.ui.theme.navigation.navigateWithDefaultOptions
import com.example.logintest.ui.theme.screens.pickers.MyDatePicker
import com.example.logintest.ui.theme.screens.pickers.MyTimePicker
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    creatorUser: UserModel,
    locationViewModel: LocationViewModel,
    onCreateEvent: (EventModel) -> Unit,
) {
    val context = LocalContext.current

    var eventName by remember { mutableStateOf("") }
    var eventTags by remember { mutableStateOf("") } // controllo se è una lista di tag
    var eventDescription by remember { mutableStateOf("") }
    var eventDay by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventCapacity by remember { mutableStateOf("") }

    val eventLocation by locationViewModel.locationData.collectAsState()
    // LocalDateTime
    var eventFullDate by remember { mutableStateOf(LocalDateTime.now()) }

    //festa,bar,concerto,teatro,mostra,corso,conferenza,altro
    // formato dataora: 2024-06-06T15:32:00Z
    // formato dataora input: 13/07/2024 | 17:00

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
                val processedValue = newValue
                    .replace(
                        Regex("[^a-zA-Z0-9,]"),
                        ""
                    ) // Remove all non-alphanumeric characters except commas
                    .replace(
                        Regex(",,+"),
                        ","
                    ) // Replace multiple consecutive commas with a single comma
                    .replace(Regex("^,|,$"), "") // Remove leading and trailing commas

                if (processedValue != eventTags) {
                    eventTags = processedValue
                }
            },
            label = { Text("Tags") },
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
                    value = eventLocation?.address ?: "",
                    onValueChange = { /* Read-only, so no change handler */ },
                    readOnly = true,
                    label = { Text("Indirizzo") },
                    trailingIcon = {
                        IconButton(onClick = { navController.navigateWithDefaultOptions("search_address") }) {
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
                    /*
                    Backend
                    name = models.CharField(max_length=100)
                    description = models.TextField()
                    creator = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
                    date = models.DateTimeField()
                    location = models.CharField(max_length=100)
                    lat = models.DecimalField(max_digits=9, decimal_places=6)
                    lon = models.DecimalField(max_digits=9, decimal_places=6)
                    capacity = models.IntegerField()
                    capacity_left = models.IntegerField()
                    created_at = models.DateTimeField(auto_now_add=True)
                    tags = models.CharField(max_length=200, blank=True)

                    App's Model
                    val capacity: Int,
                    val capacity_left: Int,
                    val created_at: String,
                    val creator: Int,
                    val date: LocalDateTime,
                    val description: String,
                    val id: Int,
                    val location: String,
                    val name: String,
                    val lat: String,
                    val lon: String,
                    val tags: String,
                    */


                    // check if all fields are filled
                    // ...

                    if (eventName.isEmpty() || eventTags.isEmpty() || eventDescription.isEmpty() || eventDay.isEmpty() || eventTime.isEmpty()) {
                        Toast.makeText(context, "Compila tutti i campi", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newEvent = EventModel(
                        name = eventName,
                        description = eventDescription,
                        creator = creatorUser.id,
                        date = createLocalDateTime(eventDay, eventTime),
                        location = eventLocation?.address ?: "",
                        lat = eventLocation?.lat.toString(),
                        lon = eventLocation?.lon.toString(),
                        capacity = eventCapacity.toIntOrNull() ?: 0,
                        capacity_left = eventCapacity.toIntOrNull() ?: 0,
                        tags = eventTags,
                        created_at = LocalDateTime.now().toString(),
                    )

                    onCreateEvent(newEvent) // spara in su l'eventp
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

fun createLocalDateTime(dateString: String, timeString: String): LocalDateTime {
    // Define input formatters
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Parse the date and time
    val date = LocalDateTime.parse(dateString, dateFormatter)
    val time = LocalDateTime.parse(timeString, timeFormatter)

    // Combine date and time
    val combinedDateTime = date.withHour(time.hour).withMinute(time.minute)

    // Format to the desired output
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val formattedString = combinedDateTime.atOffset(ZoneOffset.UTC).format(outputFormatter)

    // Parse the formatted string back to LocalDateTime
    return LocalDateTime.parse(formattedString, outputFormatter)
}