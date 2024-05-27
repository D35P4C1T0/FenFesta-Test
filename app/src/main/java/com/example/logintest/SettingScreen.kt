package com.example.logintest.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            SettingsContent(modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    val settingsOptions = listOf(
        "Informazioni Account" to Icons.Filled.AccountCircle,
        "Cambio Password" to Icons.Filled.Lock,
        "Eliminazione Account" to Icons.Filled.Delete,
        "Gestisci Abbonamento" to Icons.Filled.MailOutline,
        "Light/Dark Mode" to Icons.Filled.Build,
        "Logout" to Icons.Filled.ExitToApp,
        "Altro" to Icons.Filled.Menu,
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(settingsOptions) { (label, icon) ->
            SettingItem(label = label, icon = icon)
        }
    }
}

@Composable
fun SettingItem(label: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click here */ }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )
        }
        Divider(
            color = Color.Green,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}
