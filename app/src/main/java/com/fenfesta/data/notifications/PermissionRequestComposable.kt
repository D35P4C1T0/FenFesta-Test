package com.fenfesta.data.notifications

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun RequestNotificationPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Check if the permission is needed (Android 13 and above)
    val permissionNeeded = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // Function to check if the permission is granted
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    // State to track if the permission is granted
    var permissionGranted by remember { mutableStateOf(isPermissionGranted()) }

    // Create a permission launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
            if (!isGranted) {
                if (!shouldShowRequestPermissionRationale(
                        context as ComponentActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    showSettingsDialog = true
                } else {
                    showPermissionDialog = true
                }
            }
            onPermissionResult(isGranted)
        }
    )

    // Function to request permission
    fun requestPermission() {
        if (permissionNeeded && !permissionGranted) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onPermissionResult(true)
        }
    }

    // Request permission if needed
    LaunchedEffect(permissionNeeded) {
        requestPermission()
    }

    // Show dialog if permission is denied
    if (showPermissionDialog) {
        NotificationPermissionDialog(
            onDismiss = {
                showPermissionDialog = false
                onPermissionResult(false)
            },
            onRequestPermission = {
                showPermissionDialog = false
                requestPermission()
            }
        )
    }

    // Show settings dialog if permission is permanently denied
    if (showSettingsDialog) {
        NotificationSettingsDialog(
            onDismiss = {
                showSettingsDialog = false
                onPermissionResult(false)
            },
            onOpenSettings = {
                showSettingsDialog = false
                openAppSettings(context)
            }
        )
    }
}

// Function to open app settings
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

// Helper function to check if we should show permission rationale
fun shouldShowRequestPermissionRationale(activity: ComponentActivity, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}

@Composable
fun NotificationSettingsDialog(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Apri Impostazioni",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                "Hai rifiutato i permessi più di una volta. L'app non potrà chiederti più di accettare i permessi. Passa dalle impostazioni e concedi i permessi a FenFesta",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            Button(onClick = onOpenSettings) {
                Text("Apri Impostazioni")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Non Ora")
            }
        }
    )
}