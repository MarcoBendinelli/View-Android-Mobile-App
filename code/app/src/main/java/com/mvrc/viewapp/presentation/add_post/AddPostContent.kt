package com.mvrc.viewapp.presentation.add_post

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.presentation.add_post.components.AddPostTextField
import com.mvrc.viewapp.presentation.add_post.components.AddPostTopBar
import com.mvrc.viewapp.presentation.add_post.components.UploadImage
import com.mvrc.viewapp.presentation.components.ScrollableFilters
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.HOME_CONTENT_TOP_PADDING
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold
import com.mvrc.viewapp.presentation.theme.ViewGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [Composable] function representing the content of the Add Post screen.
 *
 * @param padding The Scaffold padding values.
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param titleInputText String representing the content of the Title Text Field.
 * @param onTitleChange Callback executed on Title Text Field value change.
 * @param subtitleInputText String representing the content of the Subtitle Text Field.
 * @param onSubtitleChange Callback executed on Subtitle Text Field value change.
 * @param bodyInputText String representing the content of the Body Text Field.
 * @param onBodyChange Callback executed on Body Text Field value change.
 * @param areTheRequiredFieldsCompleted Boolean indicating if the "Publish button" can be enabled or not.
 * @param topics The system topics to display.
 * @param selectedTopic The current selected filter.
 * @param saveNewPost Callback function to save the new create post on backend.
 * @param onImageSelection Callback to save the selected image inside the [AddPostViewModel].
 * @param onTopicSelection Callback executed on new Filter selected.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddPostContent(
    padding: PaddingValues,
    navigateBack: () -> Unit,
    titleInputText: String,
    onTitleChange: (String) -> Unit,
    subtitleInputText: String,
    onSubtitleChange: (String) -> Unit,
    bodyInputText: String,
    onBodyChange: (String) -> Unit,
    areTheRequiredFieldsCompleted: Boolean,
    topics: List<String>,
    selectedTopic: String,
    saveNewPost: () -> Unit,
    onTopicSelection: (String) -> Unit,
    onImageSelection: (Uri?) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val subtitleFocusRequest = remember { FocusRequester() }
    val bodyFocusRequest = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember {
        BringIntoViewRequester()
    }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .padding(
                start = HOME_CONTENT_PADDING.rw(),
                end = HOME_CONTENT_PADDING.rw(),
                top = HOME_CONTENT_TOP_PADDING.rh()
            )
            .fillMaxSize()
    ) {
        // Top Bar
        item {
            AddPostTopBar(
                areTheRequiredFieldsCompleted = areTheRequiredFieldsCompleted,
                onPublishNowTap = {
                    keyboardController?.hide()
                    saveNewPost()
                }, onBackTap = {
                    navigateBack()
                })
        }

        item {
            Spacer(modifier = Modifier.height(HOME_CONTENT_TOP_PADDING.rh()))
        }

        // Post Title
        item {
            AddPostTextField(
                label = stringResource(id = R.string.add_title),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
                labelStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = ViewGray,
                ),
                value = titleInputText,
                imeAction = ImeAction.Next,
                onAction = KeyboardActions {
                    subtitleFocusRequest.requestFocus()
                },
                onValueChange = onTitleChange
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.rh()))
        }

        // Post Subtitle
        item {
            AddPostTextField(
                modifier = Modifier.focusRequester(subtitleFocusRequest),
                label = stringResource(id = R.string.add_subtitle),
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = openSansSemiBold
                ),
                labelStyle = MaterialTheme.typography.headlineLarge.copy(
                    color = ViewGray,
                    fontFamily = openSansSemiBold
                ),
                value = subtitleInputText,
                imeAction = ImeAction.Next,
                onAction = KeyboardActions {
                    bodyFocusRequest.requestFocus()
                },
                onValueChange = onSubtitleChange
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.rh()))
        }

        // Upload Image
        item {
            UploadImage {
                onImageSelection(it)
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.rh()))
        }

        // Topics
        item {
            ScrollableFilters(
                topics = topics,
                showNewestTopic = false,
                selectedFilter = selectedTopic,
                callback = onTopicSelection
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.rh()))
        }

        // Post Body
        item {
            AddPostTextField(
                modifier = Modifier
                    .focusRequester(
                        bodyFocusRequest
                    )
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(400) // delay to way the keyboard shows up
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                label = stringResource(id = R.string.start_typing_here),
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.secondary,
                ),
                labelStyle = MaterialTheme.typography.titleSmall,
                value = bodyInputText,
                imeAction = ImeAction.None,
                onAction = KeyboardActions {
                    bodyFocusRequest.requestFocus()
                },
                onValueChange = {
                    onBodyChange(it)
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.rh()))
        }
    }
}