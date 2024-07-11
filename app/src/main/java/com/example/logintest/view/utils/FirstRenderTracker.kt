package com.example.logintest.view.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

class FirstRenderTracker {
    val isFirstRender = mutableStateOf(true)

    fun markRendered() {
        isFirstRender.value = false
    }
}

val LocalFirstRenderTracker = compositionLocalOf { FirstRenderTracker() }