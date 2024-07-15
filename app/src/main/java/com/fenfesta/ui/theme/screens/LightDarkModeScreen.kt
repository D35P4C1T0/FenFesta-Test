package com.fenfesta.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fenfesta.data.viewmodel.ThemeOption

@Composable
fun ThemeSelector(
    modifier: Modifier,
    currentTheme: ThemeOption,
    onThemeSelected: (ThemeOption) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ThemeOption.entries.forEach { theme ->
            Button(
                onClick = { onThemeSelected(theme) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (theme == currentTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(theme.name)
            }
        }
    }
}