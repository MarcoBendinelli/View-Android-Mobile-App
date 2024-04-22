package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.buttons.ViewFilterButton

/**
 * [Composable] representing a horizontal scrollable list of filters.
 *
 * @param modifier The Modifier to customize the Composable.
 * @param topics The list of strings representing the filter topics.
 * @param showNewestTopic The boolean variable to show / hide the "Newest" topic.
 * @param selectedFilter The current selected filter by the user_profile.
 * @param callback The callback function that takes the string topic as input.
 */
@Composable
fun ScrollableFilters(
    modifier: Modifier = Modifier,
    topics: List<String>,
    showNewestTopic: Boolean,
    selectedFilter: String,
    callback: (String) -> Unit
) {
    val newestString: String = stringResource(id = R.string.newest)

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        // Newest filter button
        if (showNewestTopic)
            ViewFilterButton(
                modifier = Modifier.padding(
                    start = 0.dp,
                    top = 0.dp,
                    bottom = 0.dp,
                    end = if (topics.isEmpty()) 0.dp else 15.rw()
                ),
                textContent = newestString,
                onClick = { callback(newestString) },
                isClicked = selectedFilter == newestString,
                isOpacityBehaviour = true
            )

        // Other topic filters
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.rw())
        ) {
            topics.map { topic ->
                ViewFilterButton(
                    textContent = topic,
                    onClick = { callback(topic) },
                    isClicked = selectedFilter == topic,
                    isOpacityBehaviour = true
                )
            }
        }
    }
}