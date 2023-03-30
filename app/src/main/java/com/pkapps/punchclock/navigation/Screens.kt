package com.pkapps.punchclock.navigation

sealed class Screens(val route: String) {
    object Main: Screens("main")
}