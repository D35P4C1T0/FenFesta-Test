package com.fenfesta.data.notifications

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit, onRequestPermission: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            "Permesso di Invio Notifiche richiesto", style = MaterialTheme.typography.titleLarge
        )
    }, text = {
        Text(
            "Le notifiche sono importati in questa app per tenerti informato sugli eventi imminenti. Senta questi permessi, potresti perderti aggiornamenti e promemoria importanti",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start
        )
    }, confirmButton = {
        Button(
            onClick = onRequestPermission
        ) {
            Text("Concedi Permessi")
        }
    }, dismissButton = {
        TextButton(
            onClick = onDismiss
        ) {
            Text("Non Ora")
        }
    })
}