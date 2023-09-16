package com.samkt.dairyapp.presentation.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.samkt.dairyapp.R
import com.samkt.dairyapp.presentation.components.GoogleButton
import com.samkt.dairyapp.utils.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import java.lang.Exception

const val AUTH = "google_auth"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    oneTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (Exception) -> Unit,
    authenticated: Boolean,
    onSuccess: () -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        content = { paddingValues ->
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    modifier = Modifier.padding(paddingValues),
                    loadingState = loadingState,
                    onButtonClicked = onButtonClicked,
                )
            }
        },
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(Exception(message))
        },
    )

    LaunchedEffect(
        key1 = authenticated,
        block = {
            if (authenticated) {
                onSuccess.invoke()
            }
        },
    )
}

@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(9f)
                .fillMaxWidth()
                .padding(all = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(10f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google logo",
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.auth_title),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                )
                Text(
                    text = stringResource(id = R.string.auth_subtitle),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                )
            }
        }
        Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.Bottom,
        ) {
            GoogleButton(
                modifier = Modifier.padding(20.dp),
                loadingState = loadingState,
                onClick = onButtonClicked,
            )
        }
    }
}
