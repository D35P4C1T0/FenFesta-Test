package com.example.logintest.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logintest.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DeleteAccountScreen(modifier: Modifier, navController: NavController, viewModel: UserViewModel) {
    val user = viewModel.getUser()
    DeleteAccountContent(modifier, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountContent(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    val user = viewModel.getUser()
    var confirmationText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState(), enabled = true),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sei sicuro di voler eliminare il tuo account? Questa azione non può essere annullata.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = confirmationText,
            onValueChange = { confirmationText = it },
            label = { Text("Inserisci la tua email per confermare") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White // Cambia il colore di sfondo qui
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (confirmationText == user.email) {
                    showDialog = true
                } else {
                    errorMessage = "L'email inserita non corrisponde a quella dell'account."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Elimina Account", color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = Color.Red)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Conferma Eliminazione", fontSize = 24.sp) },
            text = { Text("Sei sicuro di voler eliminare il tuo account? Questa azione non può essere annullata.", fontSize = 18.sp) },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Annulla", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    TextButton(
                        onClick = {
                            showDialog = false
                            // Handle account deletion logic here
                        }
                    ) {
                        Text("Elimina", color = Color.Red, fontSize = 20.sp)
                    }
                }
            },
            dismissButton = {}
        )
    }
}
