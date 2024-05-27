package com.example.logintest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.logintest.ui.theme.LoginTestTheme
import com.example.logintest.ui.navigation.MyApp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logintest.data.viewmodel.EventViewModel
import com.example.logintest.model.EventModel
import com.example.logintest.ui.theme.LoginTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: EventViewModel by viewModels()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginTestTheme {
                MyApp(viewModel)
            }
        }
    }
}
/*
    @Composable
    fun EventList(
        vm: EventViewModel,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        modifier: Modifier = Modifier
    ) {
        val eventsData by vm.eventsData.collectAsState()

        LazyColumn(contentPadding = contentPadding, modifier = modifier) {
            items(eventsData) { event ->
                // Card with event details
                EventCard(
                    event = event
                )
            }
        }
    }

    @Composable
    fun EventCard(event: EventModel) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Event Name: ${event.name}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Description: ${event.description}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Location: ${event.location}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(text = "Date: ${event.date}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Created At: ${event.created_at}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "Capacity: ${event.capacity}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Capacity Left: ${event.capacity_left}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Creator ID: ${event.creator}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBarHome(navController: NavHostController, modifier: Modifier = Modifier) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "FenFesta Alpha",
                    style = MaterialTheme.typography.headlineMedium,
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("settings")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings", tint = LocalContentColor.current
                    )
                }
            },
            modifier = modifier
        )
    }

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

    @Composable
    fun MyApp(viewModel: EventViewModel) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Scaffold(
                    topBar = { TopAppBarHome(navController) }
                ) { paddingValues ->
                    EventList(vm = viewModel, contentPadding = paddingValues)
                }
            }
            composable("settings") {
                SettingsScreen(navController)
            }
        }
    }
}*/
