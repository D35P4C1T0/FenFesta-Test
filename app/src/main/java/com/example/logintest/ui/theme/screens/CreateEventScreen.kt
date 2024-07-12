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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

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
import androidx.wear.compose.material.Text
import coil.compose.rememberAsyncImagePainter
import com.example.logintest.MainActivity
import com.example.logintest.R
import com.example.logintest.ui.theme.screens.pickers.MyDatePicker
import com.example.logintest.ui.theme.screens.pickers.MyTimePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var eventName by remember { mutableStateOf("") }
    var eventTags by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Row 1: Event Name
        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Nome Evento") },
            modifier = Modifier.fillMaxWidth()
        )

        // Row 2: Date and Time Pickers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MyDatePicker(Modifier.fillMaxWidth())
            }
            Box(modifier = Modifier.weight(1f)) {
                MyTimePicker(Modifier.fillMaxWidth())
            }
        }

        // Row 3: Tags
        OutlinedTextField(
            value = eventTags,
            onValueChange = { eventTags = it },
            label = { Text("Tag") },
            modifier = Modifier.fillMaxWidth()
        )

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
            OutlinedTextField(
                value = eventLocation,
                onValueChange = { eventLocation = it },
                label = { Text("Luogo") },
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)  // Match the height of the image box
            )
        }

        // Create Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    // Mostra l'annuncio quando l'utente preme "Crea"
                    val activity = context as MainActivity
                    activity.showInterstitialAd {
                        // Codice da eseguire dopo che l'annuncio è stato chiuso
                        Toast.makeText(context, "Evento creato", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.size(150.dp, 50.dp)
            ) {
                Text(text = "Crea", fontSize = 18.sp)
            }
        }
    }
}
