package com.samkt.dairyapp.navigation

import com.samkt.dairyapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screens(val route: String) {
    object Authentication : Screens("authentication_screen")
    object Home : Screens("home_screen")
    object Write : Screens("write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}") {
        fun passId(dairyId: String): String {
            return "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=$dairyId"
        }
    }
}
