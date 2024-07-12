package com.example.logintest.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.logintest.data.viewmodel.LogoutState
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.ui.theme.navigation.navigateWithDefaultOptions

@Composable
fun LogoutScreen(modifier: Modifier, navController: NavController, userViewModel: UserViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val logoutState by userViewModel.logoutState.collectAsState()

    LogoutContent(
        modifier = modifier,
        showDialog = { showDialog = true },
        logoutState = logoutState
    )

    if (showDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                showDialog = false
                userViewModel.logout()
            },
            onDismiss = { showDialog = false }
        )
    }

    // Handle logout state
    LaunchedEffect(logoutState) {
        when (logoutState) {
            is LogoutState.Success -> {
                // Navigate to login screen after successful logout
                navController.navigateWithDefaultOptions("mapbox")
            }

            else -> {} // Handle other states if needed
        }
    }
}

@Composable
fun LogoutContent(
    modifier: Modifier = Modifier,
    showDialog: () -> Unit,
    logoutState: LogoutState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Logout Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
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
            modifier = Modifier.defaultMinSize(minWidth = 120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = logoutState !is LogoutState.Loading
        ) {
            Text(text = "Logout", color = Color.White)
        }

        when (logoutState) {
            is LogoutState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            is LogoutState.Error -> Text(
                text = logoutState.message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )

            else -> {} // Handle other states if needed
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
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Annulla")
            }
        }
    )
}