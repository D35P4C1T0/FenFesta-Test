package com.fenfesta.ui.theme.screens

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
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fenfesta.data.viewmodel.LoginState
import com.fenfesta.data.viewmodel.ThemeOption
import com.fenfesta.data.viewmodel.UserViewModel
import com.fenfesta.ui.theme.navigation.navigateWithDefaultOptions

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    currentTheme: ThemeOption,
    onThemeChanged: (ThemeOption) -> Unit,
) {
    val loginState by userViewModel.loginState.collectAsState()
    var showThemeSelector by remember { mutableStateOf(false) }

    val loginDependentSettings = when (loginState) {
        LoginState.Success -> listOf(
            SettingItem(
                "Prenotazioni",
                Icons.Filled.Bookmarks,
                "user_reservations"
            ) { navController.navigateWithDefaultOptions("user_reservations") },
            SettingItem(
                "Gestisci Abbonamento",
                Icons.Filled.MailOutline,
                "manage_subscription"
            ) { navController.navigateWithDefaultOptions("manage_subscription") },
            SettingItem(
                "Cambio Password",
                Icons.Filled.Lock,
                "change_password"
            ) { navController.navigateWithDefaultOptions("change_password") },
            SettingItem(
                "Eliminazione Account",
                Icons.Filled.Delete,
                "delete_account"
            ) { navController.navigateWithDefaultOptions("delete_account") },
            SettingItem(
                "Logout",
                Icons.AutoMirrored.Filled.ExitToApp,
                "logout"
            ) { navController.navigateWithDefaultOptions("logout") },
        )

        else -> listOf(
            SettingItem(
                "Login",
                Icons.AutoMirrored.Filled.Login,
                "login"
            ) { navController.navigateWithDefaultOptions("login") })
    }

    val commonSettings = listOf(
        SettingItem(
            "Informazioni Account",
            Icons.Filled.AccountCircle,
            "account_info"
        ) { navController.navigateWithDefaultOptions("account_info") },
        SettingItem(
            "Cambia Tema",
            Icons.Filled.Contrast,
            "light_dark_mode"
        ) { showThemeSelector = true },
    ) + loginDependentSettings + listOf(
        SettingItem(
            "Altro",
            Icons.Filled.Menu,
            "other"
        ) { navController.navigateWithDefaultOptions("other") })

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(commonSettings) { item ->
            SettingItemRow(item)
        }
    }

    if (showThemeSelector) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onThemeSelected = { onThemeChanged(it); showThemeSelector = false },
            onDismiss = { showThemeSelector = false })
    }

}

@Composable
fun SettingItemRow(item: SettingItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

data class SettingItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val onClick: () -> Unit
)
