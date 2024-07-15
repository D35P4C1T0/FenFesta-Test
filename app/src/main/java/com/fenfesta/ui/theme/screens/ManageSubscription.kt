package com.fenfesta.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fenfesta.R
import com.fenfesta.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSubscriptionScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: UserViewModel
) {
    ManageSubscriptionContent(modifier = modifier, viewModel)
}

@Composable
fun ManageSubscriptionContent(modifier: Modifier = Modifier, viewModel: UserViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<SubscriptionOption?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState(), enabled = true),
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
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
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                textAlign = TextAlign.Center // Centrare il testo
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center // Centrare il testo
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSubscribe,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Verde
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
        containerColor = MaterialTheme.colorScheme.surface,
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
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary) // Verde
            ) {
                Text("Conferma")
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

enum class SubscriptionOption(val description: String, val price: Int) {
    Monthly("abbonamento mensile", 5),
    EventPackage("pacchetto evento singolo", 1)
}
