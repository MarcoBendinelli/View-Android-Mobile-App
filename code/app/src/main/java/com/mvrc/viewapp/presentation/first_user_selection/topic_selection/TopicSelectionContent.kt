package com.mvrc.viewapp.presentation.first_user_selection.topic_selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rT
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.data.model.ViewTopic
import com.mvrc.viewapp.presentation.components.buttons.ViewFilterButton
import com.mvrc.viewapp.presentation.components.buttons.ViewTextButton
import com.mvrc.viewapp.presentation.theme.UiConstants

/**
 * [Composable] representing the content of the Topic Selection screen.
 *
 * @param padding Padding values for layout customization.
 * @param saveTopics Callback to save topics on backend.
 * @param atLeastATopicIsSelected [MutableState] representing the pressed state of at least a topic.
 * @param onTopicSelection Callback function on a topic selection.
 * @param topics [List] of topics to display.
 * @param selectedTopics [SnapshotStateList] of selected topics.
 */
@Composable
fun TopicSelectionContent(
    padding: PaddingValues,
    topics: List<ViewTopic>,
    selectedTopics: SnapshotStateList<ViewTopic>,
    saveTopics: () -> Unit,
    atLeastATopicIsSelected: MutableState<Boolean>,
    onTopicSelection: (topic: ViewTopic) -> Unit,
) {
    var availableWidth = LocalContext.current.resources.displayMetrics.widthPixels.toFloat()
    val paddings = with(LocalDensity.current) {
        UiConstants.AUTH_CONTENT_H_PADDING.rw().toPx()
    }
    availableWidth -= paddings * 2
    val chunkedRows = calculateRows(topics, availableWidth)

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(
                start = UiConstants.AUTH_CONTENT_H_PADDING.rw(),
                end = UiConstants.AUTH_CONTENT_H_PADDING.rw(),
                top = 30.rh()
            )
            .fillMaxSize(),
    ) {
        // Title
        item {
            Text(
                text = stringResource(id = R.string.select_topic),
                style = MaterialTheme.typography.displayLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
            )
            Spacer(modifier = Modifier.height(40.rh()))
        }

        // Topics
        items(chunkedRows) { rowTopics ->
            RowOfTopics(topics = rowTopics, selectedTopics = selectedTopics) { topic ->
                onTopicSelection(topic)
            }
        }

        // Continue button
        item {
            Spacer(modifier = Modifier.height(30.rh()))
            ViewTextButton(
                modifier = Modifier.testTag("continueButton"),
                textContent = stringResource(id = R.string.continue_text),
                enabled = atLeastATopicIsSelected.value
            ) {
                saveTopics()
            }
            Spacer(modifier = Modifier.height(30.rh()))
        }
    }
}

/**
 * [Composable] to display a row of [ViewFilterButton] observers representing topics.
 *
 * @param topics List of ViewTopic objects to be displayed in the row.
 * @param onClick Callback function triggered when a topic button is clicked.
 */
@Composable
fun RowOfTopics(
    topics: List<ViewTopic>,
    selectedTopics: SnapshotStateList<ViewTopic>,
    onClick: (ViewTopic) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        topics.forEach { topic ->
            ViewFilterButton(
                modifier = Modifier.testTag("topic${topic.topicName}Button"),
                textContent = topic.topicName,
                isOpacityBehaviour = false,
                isClicked = selectedTopics.contains(topic)
            ) {
                onClick(topic)
            }
        }
    }
    Spacer(modifier = Modifier.height(20.rh()))
}

/**
 * Calculates the arrangement of topics into rows based on their individual widths
 * and the available width inside the screen.
 *
 * @param topics List of ViewTopic objects to be arranged into rows.
 * @param availableWidth The total available width for arranging the topics.
 * @return A list of rows, where each row is a list of ViewTopic objects.
 */
@Composable
fun calculateRows(
    topics: List<ViewTopic>, availableWidth: Float
): List<List<ViewTopic>> {
    val topicButtonsWidth = topics.map { topic -> buttonWidth(topic = topic) }
    val chunkOfTopics = mutableListOf<ViewTopic>()
    val chunkedTopics = mutableListOf<List<ViewTopic>>()
    var currentWidth = 0.0
    var index = 0

    while (index <= topics.size - 1) {
        currentWidth += topicButtonsWidth[index]
        chunkOfTopics.add(topics[index])
        if (currentWidth > availableWidth) {
            chunkedTopics.add(chunkOfTopics.toList())
            chunkOfTopics.clear()
            currentWidth = 0.0
        }
        index++
    }

    return chunkedTopics
}

/**
 * Calculates the width of a ViewTopic button based on its topic name, text size, and padding.
 *
 * @param topic The ViewTopic for which to calculate the button width.
 * @return The calculated width of the button for the specified topic.
 */
@Composable
fun buttonWidth(topic: ViewTopic): Float {
    val maxTextLength = topic.topicName.length
    val textSize = with(LocalDensity.current) { 14.rT().toPx() }
    val padding = with(LocalDensity.current) { 18.rw().toPx() }
    val totalPadding = padding * 2
    val textWidth = maxTextLength * textSize
    return textWidth + totalPadding
}