package com.pkapps.punchclock.core.navigation

sealed class Screens(val route: String) {
    object Main: Screens("main")
}