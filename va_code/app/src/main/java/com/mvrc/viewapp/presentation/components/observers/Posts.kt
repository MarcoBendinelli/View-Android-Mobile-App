package com.mvrc.viewapp.presentation.components.observers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.Utils.logMessage
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.domain.state.PostsState
import com.mvrc.viewapp.presentation.components.ViewCircularProgress

/**
 * [Composable] which observes the [PostsState] and updates the UI accordingly.
 *
 * @param postsState The [PostsState] representing the state of the posts, including loading status,
 *                   the list of posts, and any potential error messages.
 * @param content The composable lambda function responsible for rendering the UI when posts
 *                      are successfully loaded.
 */
@Composable
fun Posts(
    postsState: PostsState,
    content: @Composable (posts: List<ViewPost>) -> Unit
) {
    if (postsState.isLoading) {
        ViewCircularProgress()
    } else if (postsState.error.isNotEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.failed_fetch_posts),
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }
        logMessage("Posts", postsState.error)
    } else {
        content(postsState.posts)
    }
}