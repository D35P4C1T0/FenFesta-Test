package com.example.logintest.ui.utils

import android.content.Context
import android.widget.Toast
import com.example.logintest.MainActivity

object AdvLauncher {
    fun launch(context: Context, onAdLaunched: () -> Unit) {
        // Mostra l'annuncio quando l'utente preme "Crea"
        val activity = context as MainActivity
        activity.showInterstitialAd {
            // Codice da eseguire dopo che l'annuncio è stato chiuso
            Toast.makeText(context, "Evento creato", Toast.LENGTH_SHORT).show()
//            navController.popBackStack()
            onAdLaunched()
        }
    }
}