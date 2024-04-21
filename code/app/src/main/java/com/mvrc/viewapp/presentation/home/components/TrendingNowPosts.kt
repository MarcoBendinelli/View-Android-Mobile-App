package com.mvrc.viewapp.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.presentation.components.post.ViewSmallPostPreview

/**
 * [Composable] function to display the list of trending posts.
 *
 * @param posts The [ViewPost]s to display.
 * @param navigateToPostScreen Function to navigate to the Post Screen.
 */
@Composable
fun TrendingNowPosts(posts: List<ViewPost>, navigateToPostScreen: (postId: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        // Display a message when there are no posts
        if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_post),
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary),
                )
            }
        }
        posts.map { post ->
            Column {
                ViewSmallPostPreview(post = post) { navigateToPostScreen(post.id) }
                if (posts.size > 1 && posts.first() == post) {
                    Divider(modifier = Modifier.padding(vertical = 20.rh()))
                }
            }
        }
    }
}