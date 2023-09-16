package com.samkt.dairyapp.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samkt.dairyapp.utils.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {
    var authenticated by mutableStateOf(false)
        private set

    var loadingState by mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState = loading
    }

    fun authenticateWithMongoDb(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(Constants.APP_ID)
                        .login(
                            /*
                            *   JWT helps in obtaining the field values
                            * */
                            Credentials.jwt(tokenId),
                        )
                        .loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result){
                        onSuccess()
                        delay(600)
                        authenticated = true
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}


