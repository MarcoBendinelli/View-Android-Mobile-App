package com.mvrc.viewapp.presentation.landing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mvrc.viewapp.navigation.BottomNavBarGraph
import com.mvrc.viewapp.presentation.components.bottom_nav_bar.ViewBottomNavBar

/**
 * [Composable] function representing the main landing screen after user_profile sign-in.
 * The screen is composed of a [Scaffold] and dynamically displays content based on the selected
 * icon in the bottom navigation bar.
 *
 * @param landingNavController Navigation controller for the landing screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    landingNavController: NavHostController,
) {
    // Keyboard controller, interaction source, and focus manager to hide the keyboard and
    // remove the focus on the text field when users press outside these
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    val bottomNavController: NavHostController = rememberNavController()

    Scaffold(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = interactionSource
        ) {
            // Hide keyboard and clear focus on scaffold click
            keyboardController?.hide()
            focusManager.clearFocus()
        },
        bottomBar = {
            // Custom bottom navigation bar component
            ViewBottomNavBar(
                landingNavController = landingNavController,
                bottomNavController = bottomNavController,
            )
        }) { paddingValues ->
        // Navigate between different destinations using the bottom navigation bar
        BottomNavBarGraph(
            bottomNavBarController = bottomNavController,
            landingNavController = landingNavController,
            paddingValues = paddingValues,
        )
    }
}