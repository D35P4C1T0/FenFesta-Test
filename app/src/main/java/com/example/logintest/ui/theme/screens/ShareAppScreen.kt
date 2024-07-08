// ShareAppScreen.kt

package com.example.logintest.ui.theme.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logintest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareAppScreen(modifier: Modifier, navController: NavController) {

    ShareAppContent(modifier = modifier)

}

@Composable
fun ShareAppContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState(), enabled = true),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Condividi l'app con i tuoi amici!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary, // Verde
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.logo_fen_festa),
            contentDescription = "Share Icon",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Scarica la nostra app e inizia a creare eventi senza pubblicità! [Link all'app]")
                    type = "text/plain"
                }
                val chooserIntent = Intent.createChooser(shareIntent, null)
                context.startActivity(chooserIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Verde
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Condividi", color = Color.White)
        }
    }
}
