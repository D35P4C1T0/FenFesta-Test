package com.example.logintest.ui.screens

import com.example.logintest.data.viewmodel.UserViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.logintest.R
import com.example.logintest.model.UserModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInfoScreen(navController: NavController, viewModel: UserViewModel) {
    val user = viewModel.getUser()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informazioni Account") },
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
        content = {
            AccountInfoContent(user = user)
        }
    )
}

@Composable
fun AccountInfoContent(user: UserModel) {
    val profileImage = remember { mutableStateOf(user.profileImageUrl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(72.dp)) // Aggiungi spazio sopra all'immagine

        if (profileImage.value != null) {
            Image(
                painter = rememberImagePainter(profileImage.value),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        // Handle image change
                    },
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        // Handle image change
                    },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Aggiungi spazio sotto all'immagine

        AccountInfoRow(label = "Username", value = user.username)
        AccountInfoRow(label = "Nome", value = user.firstName)
        AccountInfoRow(label = "Cognome", value = user.lastName)
        AccountInfoRow(label = "Email", value = user.email)
        AccountInfoRow(
            label = "Eventi partecipati",
            value = user.eventsParticipated.toString()
        )
        AccountInfoRow(label = "id", value = user.id.toString())

        Spacer(modifier = Modifier.weight(1f)) // Riempie lo spazio verticale rimanente

        Button(
            onClick = { /* Handle edit action */ },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(bottom = 48.dp) // Spazio aggiuntivo sotto il pulsante
        ) {
            Text(text = "Modifica", color = Color.White)
        }
    }
}

@Composable
fun AccountInfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 16.dp) // Riduci lo spazio verticale
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp) // Riduci lo spazio di indentazione e lo spazio verticale
        )
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp) // Riduci lo spazio verticale
        )
    }
}
