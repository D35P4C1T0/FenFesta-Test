package com.fenfesta.view.utils

object FirstLaunch {
    var isFirstLaunch: Boolean = true

    fun toggleFirstLaunch() {
        isFirstLaunch = !isFirstLaunch
    }
}
