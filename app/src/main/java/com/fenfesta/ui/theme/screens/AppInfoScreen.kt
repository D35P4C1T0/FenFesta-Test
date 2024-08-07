package com.fenfesta.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppInfoScreen(modifier: Modifier, navController: NavController) {
    AppInfoContent(modifier = modifier)
}

@Composable
fun AppInfoContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            //.fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState(), enabled = true),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Titolo App e Versione
        Text(
            text = "FenFesta",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Versione: 1.0.0",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Data di rilascio: 24 Luglio 2024",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descrizione dell'App
        Text(
            text = "FenFesta Alpha è un'app innovativa per gestire e partecipare a eventi. Con FenFesta, puoi creare eventi, scovarne di nuovi, invitare partecipanti e molto altro! Abbonati per non dover guardare una pubblicità ad ogni evento da inserire!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Autori/Sviluppatori
        Text(
            text = "Sviluppato da:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
        Text(
            text = "Matteo Girardi • Edoardo Ghirardello",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Informativa sulla Privacy e Termini di Servizio
        Text(
            text = "Privacy Policy e Termini di Servizio:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
        Text(
            text = "Per maggiori dettagli, contattaci all'indirizzo support@fenfesta.com",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Licenze
        Text(
            text = "Licenze Open Source:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
        Text(
            text = "Questa applicazione utilizza componenti open source. Visita la pagina delle licenze per maggiori informazioni.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Note di rilascio
        Text(
            text = "Note di Rilascio:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
        Text(
            text = "Versione 1.0.0: Prima release dell'app.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ringraziamenti
        Text(
            text = "Ringraziamenti:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
        Text(
            text = "Un ringraziamento speciale a tutti i beta tester e ai collaboratori che hanno contribuito allo sviluppo di FenFesta, al professor Battiti e a Beatrice Ghirardello, designer del logo",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
