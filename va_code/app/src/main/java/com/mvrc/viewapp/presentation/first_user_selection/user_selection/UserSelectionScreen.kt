package com.mvrc.viewapp.presentation.first_user_selection.user_selection

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
import com.mvrc.viewapp.AppViewModel
import com.mvrc.viewapp.presentation.components.top_app_bars.BackSkipTopBar
import com.mvrc.viewapp.presentation.first_user_selection.FirstUserSelectionViewModel
import com.mvrc.viewapp.presentation.first_user_selection.observers.SaveUsers
import kotlinx.coroutines.launch

/**
 * [Composable] representing the screen for user_profile contributor
 * selection during the first app experience.
 *
 * @param appViewModel ViewModel for managing app status.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSelectionScreen(
    appViewModel: AppViewModel,
    firstUserSelectionViewModel: FirstUserSelectionViewModel = hiltViewModel(),
) {
    // SnackBar related observers: showSnackBar state necessary to not show 2 times
    // the snackBar on configuration changes
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BackSkipTopBar(navigate = {
                appViewModel.onEndUserSelection()
            }, showSkip = true)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { padding ->
            UserSelectionContent(
                padding = padding,
                selectedUsers = firstUserSelectionViewModel.selectedUsersIds,
                loadUsers = {
                    firstUserSelectionViewModel.getUsers()
                },
                saveUser = { userId ->
                    firstUserSelectionViewModel.saveAndHandleUserSelection(userId)
                })
        }
    )

    // Function to show a snackBar
    fun showSnackBar(message: String) = coroutineScope.launch {
        if (showSnackBar) {
            snackBarHostState.showSnackbar(message)
            showSnackBar = false
        }
    }

    SaveUsers(
        showSnackBar = {
            showSnackBar(it)
        }, onSuccess = {
            appViewModel.onEndUserSelection()
        })
}
