package com.example.logintest.ui.theme.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Impostazioni") },
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
            SettingsContent(navController = navController, modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun SettingsContent(navController: NavController, modifier: Modifier = Modifier) {
    val settingsOptions = listOf(
        Triple("Informazioni Account", Icons.Filled.AccountCircle, "account_info"),
        Triple("Cambio Password", Icons.Filled.Lock, "change_password"),
        Triple("Eliminazione Account", Icons.Filled.Delete, "delete_account"),
        Triple("Gestisci Abbonamento", Icons.Filled.MailOutline, "manage_subscription"),
        Triple("Light/Dark Mode", Icons.Filled.Build, "light_dark_mode"),
        Triple("Logout", Icons.Filled.ExitToApp, "logout"),
        Triple("Altro", Icons.Filled.Menu, "other")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(settingsOptions) { (label, icon, route) ->
            SettingItem(label = label, icon = icon, onClick = { navController.navigate(route) })
        }
    }
}

@Composable
fun SettingItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}
