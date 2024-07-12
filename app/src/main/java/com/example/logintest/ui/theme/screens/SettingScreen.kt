package com.example.logintest.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logintest.data.viewmodel.LoginState
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.ui.theme.navigation.navigateWithDefaultOptions

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {

    val loginState by userViewModel.loginState.collectAsState()

    val loginDependentSettings = when (loginState) {
        LoginState.Success -> listOf(
            Triple("Cambio Password", Icons.Filled.Lock, "change_password"),
            Triple("Eliminazione Account", Icons.Filled.Delete, "delete_account"),
            Triple("Gestisci Abbonamento", Icons.Filled.MailOutline, "manage_subscription"),
            Triple("Logout", Icons.AutoMirrored.Filled.ExitToApp, "logout"),
        )
        else -> listOf( Triple("Login", Icons.AutoMirrored.Filled.Login, "login"))
    }

    val commonSettings = listOf(
        Triple("Informazioni Account", Icons.Filled.AccountCircle, "account_info"),
        Triple("Light/Dark Mode", Icons.Filled.Build, "light_dark_mode"),
        Triple("Altro", Icons.Filled.Menu, "other"),
        Triple("Cerca Via", Icons.Filled.Search, "search_address"),
    ) + loginDependentSettings


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(commonSettings) { (label, icon, route) ->
            SettingItem(label = label, icon = icon, onClick = { navController.navigateWithDefaultOptions(route) })
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
