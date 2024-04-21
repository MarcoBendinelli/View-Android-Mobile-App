package com.mvrc.viewapp.presentation.components.bottom_nav_bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.navigation.Screen.AddPostScreen
import com.mvrc.viewapp.navigation.Screen.BookmarksScreen
import com.mvrc.viewapp.navigation.Screen.HomeScreen
import com.mvrc.viewapp.navigation.Screen.SearchScreen
import com.mvrc.viewapp.navigation.Screen.SettingsScreen

/**
 * Custom bottom navigation bar with centered [ViewAddButton].
 *
 * @param landingNavController The [NavHostController] used for navigating with new screens that
 * do not show the bottom navigation bar.
 * @param bottomNavController The [NavHostController] used for navigation inside the bottom navigation bar
 */
@Composable
fun ViewBottomNavBar(
    landingNavController: NavHostController,
    bottomNavController: NavHostController
) {
    val landingScreens = listOf(
        HomeScreen,
        SearchScreen,
        BookmarksScreen,
        SettingsScreen
    )

    // Retrieve the current route from the navigation controller's back stack
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.height(104.rh()),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            landingScreens.take(2).forEach { screen ->
                ViewBottomNavBarItem(
                    modifier = Modifier.weight(1f).testTag("iconButtonNavBar${screen.route}"),
                    screen = screen,
                    isClicked = currentRoute == screen.route
                ) {
                    bottomNavController.navigate(screen.route) {
                        bottomNavController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }

            // Add button in the center of the navigation bar
            ViewAddButton(
                modifier = Modifier
                    .weight(1f)
            ) {
                landingNavController.navigate(AddPostScreen.route) {
                    bottomNavController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            landingScreens.takeLast(2).forEach { screen ->
                ViewBottomNavBarItem(
                    modifier = Modifier.weight(1f).testTag("iconButtonNavBar${screen.route}"),
                    screen = screen,
                    isClicked = currentRoute == screen.route
                ) {
                    bottomNavController.navigate(screen.route) {
                        bottomNavController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}