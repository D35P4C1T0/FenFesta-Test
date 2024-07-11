package com.example.logintest.view.utils

object FirstLaunch {
    var isFirstLaunch: Boolean = true

    fun toggleFirstLaunch() {
        isFirstLaunch = !isFirstLaunch
    }
}
