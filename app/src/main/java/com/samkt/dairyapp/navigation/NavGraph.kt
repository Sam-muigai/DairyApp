package com.samkt.dairyapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.samkt.dairyapp.presentation.screens.auth.AuthenticationScreen
import com.samkt.dairyapp.presentation.screens.auth.AuthenticationViewModel
import com.samkt.dairyapp.utils.Constants.APP_ID
import com.samkt.dairyapp.utils.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        authenticationRoute(
            onAuthSuccess = {
                navController.popBackStack()
                navController.navigate(Screens.Home.route)
            },
        )
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    onAuthSuccess: () -> Unit,
) {
    composable(Screens.Authentication.route) {
        val viewModel = viewModel<AuthenticationViewModel>()
        val authenticated = viewModel.authenticated
        val loadingState = viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            loadingState = loadingState,
            oneTapState = oneTapState,
            authenticated = authenticated,
            messageBarState = messageBarState,
            onTokenIdReceived = { tokenId ->
                viewModel.authenticateWithMongoDb(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = { exception ->
                        messageBarState.addError(exception)
                        viewModel.setLoading(false)
                    },
                )
            },
            onDialogDismissed = { exception ->
                messageBarState.addError(exception)
            },
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onSuccess = onAuthSuccess,
        )
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(Screens.Home.route) {
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                scope.launch {
                    App.create(APP_ID).currentUser?.logOut()
                }
            }) {
                Text(text = "LOG OUT")
            }
        }
    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        Screens.Write.route,
        arguments = listOf(
            navArgument(WRITE_SCREEN_ARGUMENT_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) {
    }
}
