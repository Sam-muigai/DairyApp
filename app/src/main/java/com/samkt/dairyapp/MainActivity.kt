package com.samkt.dairyapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.samkt.dairyapp.navigation.Screens
import com.samkt.dairyapp.navigation.SetupNavGraph
import com.samkt.dairyapp.ui.theme.DairyAppTheme
import com.samkt.dairyapp.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App

@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * To be changed later
         */
        WindowCompat.setDecorFitsSystemWindows(window, true)
        installSplashScreen()
        setContent {
            DairyAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController,
                )
            }
        }
    }
}

private fun getStartDestination(): String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) {
        Screens.Home.route
    } else {
        Screens.Authentication.route
    }
}
