package com.mvrc.viewapp.presentation.add_post.observers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.core.Utils
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.repository.AuthRepositoryImpl
import com.mvrc.viewapp.presentation.add_post.AddPostViewModel
import com.mvrc.viewapp.presentation.components.ViewCircularProgress

/**
 * [Composable] which observes the state of the Add Post process
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for Add Post logic.
 * @param showSnackBar Callback to show a snackBar with a given message.
 * @param onSuccess Callback function executed when the sign up is successful.
 */
@Composable
fun AddPost(
    viewModel: AddPostViewModel = hiltViewModel(),
    showSnackBar: (message: String) -> Unit,
    onSuccess: () -> Unit
) {
    // Observe the state of the Add Post response
    when (val addPostResponse = viewModel.addPostResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> if (addPostResponse.data!!) onSuccess() else Unit
        // Failure state - log the error and show a SnackBar
        is Failure -> addPostResponse.apply {
            Utils.logMessage(
                "Failure",
                "AddPost: ${addPostResponse.e.message.toString()}"
            )
            showSnackBar(AuthRepositoryImpl.fromFirebaseException(addPostResponse.e))
        }
    }
}