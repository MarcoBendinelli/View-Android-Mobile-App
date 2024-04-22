package com.mvrc.viewapp.presentation.settings.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl.Companion.fromFirebaseException
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.settings.SettingsViewModel

@Composable
fun SignOut(
    viewModel: SettingsViewModel = hiltViewModel(),
    showSnackBar: (message: String) -> Unit,
) {
    when (val signOutResponse = viewModel.signOutResponse.collectAsState().value) {
        is Loading -> ViewCircularProgress()
        is Success -> Unit
        is Failure -> LaunchedEffect(Unit) {
            logMessage(
                "Failure",
                "SignOut: ${signOutResponse.e.message.toString()}"
            )
            showSnackBar(fromFirebaseException(signOutResponse.e))
        }
    }
}