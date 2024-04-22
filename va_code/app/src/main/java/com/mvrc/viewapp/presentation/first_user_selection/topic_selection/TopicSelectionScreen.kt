package com.mvrc.viewapp.presentation.first_user_selection.topic_selection

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mvrc.viewapp.presentation.components.top_app_bars.BackSkipTopBar
import com.mvrc.viewapp.presentation.first_user_selection.FirstUserSelectionViewModel
import com.mvrc.viewapp.presentation.first_user_selection.observers.SaveTopics
import kotlinx.coroutines.launch

/**
 * [Composable] representing the screen for topic
 * selection during the first app experience.
 *
 * @param navigateToUserSelectionScreen Callback function to navigate to the user_profile selection screen.
 * @param firstUserSelectionViewModel ViewModel for managing the selection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSelectionScreen(
    navigateToUserSelectionScreen: () -> Unit,
    firstUserSelectionViewModel: FirstUserSelectionViewModel = hiltViewModel()
) {
    val atLeastATopicIsSelected = rememberSaveable {
        mutableStateOf(false)
    }

    val topics = firstUserSelectionViewModel.getTopics()

    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        BackSkipTopBar(navigate = {
            firstUserSelectionViewModel.resetSelectedTopics()
            atLeastATopicIsSelected.value = false
            navigateToUserSelectionScreen()
        }, showSkip = true)
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }, content = { padding ->
        TopicSelectionContent(padding = padding,
            topics = topics,
            selectedTopics = firstUserSelectionViewModel.selectedTopics,
            atLeastATopicIsSelected = atLeastATopicIsSelected,
            saveTopics = {
                firstUserSelectionViewModel.saveTopics()
            },
            onTopicSelection = { topic ->
                firstUserSelectionViewModel.handleTopicSelection(topic)
                atLeastATopicIsSelected.value =
                    firstUserSelectionViewModel.selectedTopics.isNotEmpty()
            })
    })

    // Function to show a snackBar
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    SaveTopics(showSnackBar = {
        showSnackBar(it)
    }, onSuccess = {
        navigateToUserSelectionScreen()
        firstUserSelectionViewModel.resetSaveTopicsStateFlow()
    })
}