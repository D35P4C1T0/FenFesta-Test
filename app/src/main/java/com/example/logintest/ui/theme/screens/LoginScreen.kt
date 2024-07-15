package com.example.logintest.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.logintest.data.viewmodel.LoginState
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.model.UserModel

@Composable
fun LoginPage(
    modifier: Modifier,
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    goBackToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val scrollState = rememberScrollState()

    var showDialog by remember { mutableStateOf(false) }

    val userData by userViewModel.userData.collectAsState()

    if (showDialog) {
        userData?.let {
            LoginResultDialog(
                userData = it,
                onConfirm = { showDialog = false; goBackToHome() },
                onDismiss = { showDialog = false; goBackToHome() },
            )
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding() // This will push the content up when the keyboard is visible
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Your existing login content goes here
            LoginContent(
                userViewModel = userViewModel,
                onLoginSuccess = { showDialog = true },
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}

@Composable
fun LoginContent(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginState by userViewModel.loginState.collectAsState()

    Text(
        text = "Login",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 32.dp)
    )

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )

    Button(
        onClick = { userViewModel.login(email, password) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text("Login")
    }

    // Handle login state
    when (loginState) {
        is LoginState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        is LoginState.Error -> Text(
//            text = (loginState as LoginState.Error).message,
            text = "Credenziali non corrette o non esistenti. Riprova.",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 16.dp)
        )

        is LoginState.Success -> LaunchedEffect(Unit) { onLoginSuccess() }
        else -> {} // Handle Idle state if needed
    }

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(onClick = onNavigateToRegister) {
        Text("Non hai un account? Registrati")
    }
}

@Composable
fun LoginResultDialog(
    userData: UserModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Benvenuto ${userData.firstName} ${userData.lastName}!",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "La tua mail è ${userData.email}. Che bella giornata!",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Grazie!")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Seh Ciao!")
            }
        }
    )
}