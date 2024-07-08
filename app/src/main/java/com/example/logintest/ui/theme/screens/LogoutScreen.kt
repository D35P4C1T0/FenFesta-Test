package com.example.logintest.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LogoutScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logout") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LogoutContent(
                modifier = Modifier.padding(paddingValues),
                showDialog = { showDialog = true }
            )
        }
    )

    if (showDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showDialog = false
                // Aggiungi la logica di logout qui, ad esempio navigare alla schermata di login
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun LogoutContent(modifier: Modifier = Modifier, showDialog: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.ExitToApp,
            contentDescription = "Logout Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary // Verde
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sei sicuro di voler effettuare il logout?",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = showDialog,
            modifier = Modifier
                .defaultMinSize(minWidth = 120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Verde
        ) {
            Text(text = "Logout", color = Color.White)
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Conferma Logout",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "Sei sicuro di voler effettuare il logout?",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary) // Verde
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary) // Verde
            ) {
                Text("Annulla")
            }
        }
    )
}
