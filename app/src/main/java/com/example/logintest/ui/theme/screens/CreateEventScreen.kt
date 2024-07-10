package com.example.logintest.ui.theme.screens

//import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.logintest.R
import com.example.logintest.ui.theme.screens.pickers.MyDatePicker
import com.example.logintest.ui.theme.screens.pickers.MyTimePicker
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventTags by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventImage by remember { mutableStateOf("") }  // This could be a Uri in a real application

    val calendar = Calendar.getInstance()

    // Function to show DatePicker
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            eventDate = "$dayOfMonth/${month + 1}/$year"
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH)
//    )
//
//    // Function to show TimePicker
//    val timePickerDialog = TimePickerDialog(
//        context,
//        { _, hourOfDay, minute ->
//            eventTime = "$hourOfDay:$minute"
//        },
//        calendar.get(Calendar.HOUR_OF_DAY),
//        calendar.get(Calendar.MINUTE),
//        true
//    )


    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), enabled = true),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Nome Evento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                OutlinedTextField(
//                    value = eventDate,
//                    onValueChange = { eventDate = it },
//                    label = { Text("Data") },
//                    modifier = Modifier.weight(1f),
//                    trailingIcon = {
//                        MyDatePicker()
//                    }
//                )

                MyDatePicker(modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                MyTimePicker(modifier.weight(1f))

//                OutlinedTextField(
//                    value = eventTime,
//                    onValueChange = { eventTime = it },
//                    label = { Text("Ora") },
//                    modifier = Modifier.weight(1f),
//                    trailingIcon = {
//                        MyTimePicker()
//                    }
//                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = eventTags,
                onValueChange = { eventTags = it },
                label = { Text("Tag") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Descrizione") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Immagine", modifier = Modifier.padding(start = 4.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    // Placeholder for image picker
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(50.dp),
                        onClick = { /* TODO: Implement image picker */ }) {
                        Icon(
                            painter = rememberAsyncImagePainter(R.drawable.logo_fen_festa_monocromo),
                            contentDescription = "Add Image",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = eventLocation,
                onValueChange = { eventLocation = it },
                label = { Text("Luogo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    // Handle event creation
                    Toast.makeText(context, "Evento creato", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier.size(150.dp, 50.dp) // Aumenta le dimensioni del pulsante
            ) {
                Text(text = "Crea", fontSize = 18.sp)  // Aumenta la dimensione del testo
            }
        }
    }
}
