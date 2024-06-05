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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logintest.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LightDarkModeScreen(navController: NavController) {
    var isDarkMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tema Chiaro/Scuro") },
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
            LightDarkModeContent(
                isDarkMode = isDarkMode,
                onModeChange = { isDarkMode = it },
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}

@Composable
fun LightDarkModeContent(
    isDarkMode: Boolean,
    onModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModeRadioButton(
                isSelected = !isDarkMode,
                text = "Light Mode",
                onClick = { onModeChange(false) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            ModeRadioButton(
                isSelected = isDarkMode,
                text = "Dark Mode",
                onClick = { onModeChange(true) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PreviewImage(
            imageRes = if (isDarkMode) R.drawable.dark_mode_preview else R.drawable.light_mode_preview,
            description = if (isDarkMode) "Dark Mode Preview" else "Light Mode Preview"
        )
    }
}

@Composable
fun ModeRadioButton(isSelected: Boolean, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun PreviewImage(imageRes: Int, description: String) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = description,
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
    )
}
