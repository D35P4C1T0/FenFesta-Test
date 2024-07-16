package com.fenfesta.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fenfesta.data.viewmodel.UserViewModel
import com.fenfesta.model.UserModel
import com.fenfesta.ui.theme.navigation.navigateWithDefaultOptions
import com.fenfesta.ui.theme.screens.search.SearchBar

@Composable
fun DeleteAccountScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: UserViewModel
) {
    val user by viewModel.userData.collectAsState()
    user?.let {
        DeleteAccountContent(modifier,
            user = it,
            deleteAccount = {
                viewModel.deleteAccount()
                navController.navigateWithDefaultOptions("mapbox")
            })
    }
}

@Composable
fun DeleteAccountContent(
    modifier: Modifier = Modifier,
    user: UserModel,
    deleteAccount: () -> Unit
) {

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
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            perCharacterSearchAction = { confirmationText = it },
            placeHolderText = "Inserisci la tua mail"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                //println(user.email)
                if (confirmationText == user.email) {
                    showDialog = true
                } else {
                    errorMessage = "L'email inserita non corrisponde a quella dell'account."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
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
            text = {
                Text(
                    "Sei sicuro di voler eliminare il tuo account? Questa azione non può essere annullata.",
                    fontSize = 18.sp
                )
            },
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
                            if (confirmationText == user.email) {
                                deleteAccount()
                            }
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
