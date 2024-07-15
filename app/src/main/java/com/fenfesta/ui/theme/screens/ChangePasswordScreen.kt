package com.fenfesta.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fenfesta.data.viewmodel.PasswordChangeState
import com.fenfesta.data.viewmodel.UserViewModel

@Composable
fun ChangePasswordScreen(
    modifier: Modifier,
    navController: NavController,
    userModel: UserViewModel
) {
    ChangePasswordContent(
        userViewModel = userModel,
        modifier = modifier,
        onSuccess = {
            navController.popBackStack()
        },
        onChangePassword = { currentPassword, newPassword ->
            userModel.changePassword(
                oldPassword = currentPassword,
                newPassword = newPassword,
                confirmPassword = newPassword
            )
        },
    )
}

@Composable
fun ChangePasswordContent(
    userViewModel: UserViewModel,
    modifier: Modifier,
    onChangePassword: (String, String) -> Unit,
    onSuccess: () -> Unit,
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val changePasswordState by userViewModel.passwordChangeState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val fieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
    )

    if (showDialog) {
        PasswordChangeDialog(
            onConfirm = {
                showDialog = false; onSuccess(); userViewModel.clearPasswordChangeState()
            },
            onDismiss = {
                showDialog = false; onSuccess(); userViewModel.clearPasswordChangeState()
            })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState(), enabled = true),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            colors = fieldColors,
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Password Attuale") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            colors = fieldColors,
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nuova Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            colors = fieldColors,
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Conferma Nuova Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = passwordVisibility,
                onCheckedChange = { passwordVisibility = it }
            )
            Text(text = "Mostra password")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // qui dialoghi ed errori
        // Handle login state
        when (changePasswordState) {
            is PasswordChangeState.Loading -> CircularProgressIndicator(
                modifier = Modifier.padding(
                    top = 16.dp
                )
            )

            is PasswordChangeState.Error -> Text(
//            text = (loginState as LoginState.Error).message,
                text = "Credenziali non corrette o non esistenti. Riprova.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )

            is PasswordChangeState.Success -> LaunchedEffect(Unit) { showDialog = true }
            else -> {} // Handle Idle state if needed
        }


        Button(
            onClick = {
                if (newPassword == confirmPassword) {
                    onChangePassword(currentPassword, newPassword)
                } else {
                    errorMessage = "Le nuove password non corrispondono"
                }
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Modifica", color = Color.White)
        }
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = Color.Red)
        }
    }
}

@Composable
fun PasswordChangeDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Password modificata Correttamente!",
                style = MaterialTheme.typography.titleLarge
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Conferma")
            }
        },
    )
}