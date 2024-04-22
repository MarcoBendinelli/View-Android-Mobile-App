package com.mvrc.viewapp.presentation.edit_profile.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.components.buttons.ViewFilterButton

/**
 * [Composable] representing a horizontal scrollable list of filters
 * that allows to select more topics.
 *
 * @param topics The list of strings representing the filter topics.
 * @param selectedTopics The current selected topics by the user.
 * @param callback The custom callback function that takes the string topic as input.
 */
@Composable
fun ScrollableMultiSelectionTopics(
    topics: List<String>,
    selectedTopics: List<String>,
    callback: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(15.rw())
    ) {
        topics.map { topic ->
            ViewFilterButton(
                textContent = topic,
                onClick = { callback(topic) },
                isClicked = selectedTopics.contains(topic),
                isOpacityBehaviour = true
            )
        }
    }

}