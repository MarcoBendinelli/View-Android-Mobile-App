package com.mvrc.viewapp.presentation.add_post

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.presentation.add_post.observers.AddPost
import kotlinx.coroutines.launch

/**
 * [Composable] representing the Add Post screen allowing the user_profile to create a new post.
 *
 * @param navigateBack Callback to navigate back to the previous screen.
 * @param addPostViewModel View model containing the states for the Add Post screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    navigateBack: () -> Unit,
    addPostViewModel: AddPostViewModel = hiltViewModel()
) {
    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val topics = addPostViewModel.getTopics()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { padding ->
            AddPostContent(
                padding = padding, navigateBack = navigateBack,
                areTheRequiredFieldsCompleted = addPostViewModel.areRequiredFieldsCompletedStateFlow.collectAsState().value,
                titleInputText = addPostViewModel.titleStateFlow.collectAsState().value,
                onTitleChange = { addPostViewModel.onTitleChange(it) },
                subtitleInputText = addPostViewModel.subtitleStateFlow.collectAsState().value,
                onSubtitleChange = { addPostViewModel.onSubtitleChange(it) },
                bodyInputText = addPostViewModel.bodyStateFlow.collectAsState().value,
                onBodyChange = { addPostViewModel.onBodyChange(it) },
                topics = topics,
                selectedTopic = addPostViewModel.selectedTopicStateFlow.collectAsState().value,
                onTopicSelection = { addPostViewModel.onTopicSelection(it) },
                saveNewPost = {
                    showSnackBar = true
                    addPostViewModel.saveNewPost(context)
                },
                onImageSelection = { addPostViewModel.selectImage(it) })
        },
    )

    // Function to show a snackBar
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    AddPost(showSnackBar = { showSnackBar(it) }, onSuccess = {
        navigateBack()
    })
}