package com.mvrc.viewapp.presentation.post.observers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.Response.Failure
import com.mvrc.viewapp.data.model.Response.Loading
import com.mvrc.viewapp.data.model.Response.Success
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.presentation.components.ViewCircularProgress
import com.mvrc.viewapp.presentation.post.PostViewModel

/**
 * [Composable] which observes the state of the Post retrieval
 * and handles the UI accordingly.
 *
 * @param viewModel The ViewModel responsible for retrieving the Post data.
 * @param postContent The [Composable] to show the post on Success.
 */
@Composable
fun Post(
    viewModel: PostViewModel = hiltViewModel(),
    postContent: @Composable (post: ViewPost) -> Unit
) {
    // Observe the state of the Post retrieval
    when (val postResponse = viewModel.postResponseStateFlow.collectAsState().value) {
        // Loading state - show a circular progress indicator
        is Loading -> ViewCircularProgress()
        is Success -> postResponse.data?.let { postContent(it) }
        // Failure state - log the error and show a SnackBar
        is Failure -> postResponse.apply {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.failed_fetch_post),
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }
            logMessage("Post", postResponse.e.message.toString())
        }
    }
}
