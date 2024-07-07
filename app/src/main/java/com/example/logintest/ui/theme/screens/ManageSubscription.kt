package com.example.logintest.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logintest.R
import com.example.logintest.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManageSubscriptionScreen(navController: NavController, viewModel: UserViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestisci Abbonamento") },
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
            ManageSubscriptionContent(modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun ManageSubscriptionContent(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<SubscriptionOption?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SubscriptionOption(
            title = "Abbonamento Mensile",
            description = "5€ al mese per creare eventi senza pubblicità",
            price = 5,
            imageRes = R.drawable.ic_subscription_monthly,
            onSubscribe = {
                selectedOption = SubscriptionOption.Monthly
                showDialog = true
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SubscriptionOption(
            title = "Pacchetto Evento Singolo",
            description = "1€ per 3 eventi senza pubblicità",
            price = 1,
            imageRes = R.drawable.ic_subscription_event,
            onSubscribe = {
                selectedOption = SubscriptionOption.EventPackage
                showDialog = true
            }
        )
    }

    if (showDialog) {
        SubscriptionConfirmationDialog(
            selectedOption = selectedOption,
            onConfirm = {
                // Gestisci logica di pagamento
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun SubscriptionOption(
    title: String,
    description: String,
    price: Int,
    imageRes: Int,
    onSubscribe: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrare il contenuto orizzontalmente
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4CAF50),
                fontSize = 20.sp,
                textAlign = TextAlign.Center // Centrare il testo
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center // Centrare il testo
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSubscribe,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde
            ) {
                Text(text = "Sottoscrivi")
            }
        }
    }
}

@Composable
fun SubscriptionConfirmationDialog(
    selectedOption: SubscriptionOption?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Conferma Sottoscrizione",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "Vuoi procedere con il pagamento di ${selectedOption?.price}€ per l'${selectedOption?.description}?",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50)) // Verde
            ) {
                Text("Conferma")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50)) // Verde
            ) {
                Text("Annulla")
            }
        }
    )
}

enum class SubscriptionOption(val description: String, val price: Int) {
    Monthly("abbonamento mensile", 5),
    EventPackage("pacchetto evento singolo", 1)
}
