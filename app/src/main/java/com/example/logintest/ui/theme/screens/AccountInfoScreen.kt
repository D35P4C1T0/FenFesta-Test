package com.example.logintest.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.logintest.R
import com.example.logintest.data.viewmodel.LoginState
import com.example.logintest.data.viewmodel.UserViewModel
import com.example.logintest.model.UserModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInfoScreen(
    modifier: Modifier,
    userViewModel: UserViewModel,
    navController: NavHostController
) {
    val userData by userViewModel.userData.collectAsState()
    val loginState by userViewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.Success -> {
            userViewModel.profileInfo() // fetches profile info
            userData?.let {
                AccountInfoContent(user = userData!!, modifier = modifier)
            } ?: run {
                Text(
                    text = "Errore Loggato ma no dati",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                )
            }
        }

        is LoginState.Idle -> {
            EmptyAccountInfo(
                modifier = modifier,
                onCreateAccountClick = { navController.navigate("register") },
                onLoginClick = { navController.navigate("login") }
            )
        }

        else -> {
            Text(text = "Errore", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AccountInfoContent(user: UserModel, modifier: Modifier) {
    val profileImage = remember { mutableStateOf(user.profileImageUrl) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        if (profileImage.value != null) {
            Image(
                painter = rememberAsyncImagePainter(profileImage.value),
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
//            value = user.eventsParticipated.toString()
            value = "69"
        )
        AccountInfoRow(label = "id", value = user.id.toString())

        Spacer(modifier = Modifier.weight(1f)) // Riempie lo spazio verticale rimanente

        Button(
            onClick = { /* Handle edit action */ },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(
                top = 48.dp,
                bottom = 48.dp
            ) // Spazio aggiuntivo sotto il pulsante
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
            modifier = Modifier.padding(
                start = 8.dp,
                bottom = 2.dp
            ) // Riduci lo spazio di indentazione e lo spazio verticale
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

@Composable
fun EmptyAccountInfo(
    modifier: Modifier,
    onCreateAccountClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Account Icon",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ciao Festaiolo!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crea un account o accedi per usufruire delle funzionalità personalizzate e salvare le tue preferenze.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCreateAccountClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Crea Account")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Accedi")
        }
    }
}