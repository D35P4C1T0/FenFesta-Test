package com.example.logintest.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SupportScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Supporto") },
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
            SupportContent(modifier = Modifier.padding(paddingValues))
        }
    )
}

@Composable
fun SupportContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Contatti per il Supporto",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ContactItem(
            label = "E-mail per Assistenza",
            detail = "supporto@fanfesta.com",
            icon = Icons.Filled.Email
        )

        ContactItem(
            label = "Numero di Telefono",
            detail = "+39 339 102 4808",
            icon = Icons.Filled.Phone
        )

        Spacer(modifier = Modifier.height(24.dp))

        FAQSection()

        Spacer(modifier = Modifier.height(24.dp))

        FeedbackSection()
    }
}

@Composable
fun ContactItem(label: String, detail: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
            )
        }
    }
}

@Composable
fun FAQSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "FAQ",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        FAQItem(
            question = "Come posso creare un evento?",
            answer = "Nella home page, premi l'icona + per creare un nuovo evento"
        )

        FAQItem(
            question = "Come posso modificare il mio profilo?",
            answer = "Vai alla sezione 'Informazioni Account' e premi il pulsante 'Modifica'"
        )

        FAQItem(
            question = "Come posso contattare il supporto?",
            answer = "Puoi contattarci via email a supporto@fanfesta.com"
        )
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
        )
    }
}

@Composable
fun FeedbackSection() {
    var feedbackText by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Inviaci un Feedback",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Scrivi il tuo feedback") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Logica per l'invio del feedback
                isSubmitted = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Invia", color = Color.White)
        }

        if (isSubmitted) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Grazie per il tuo feedback!",
                color = Color(0xFF4CAF50),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
