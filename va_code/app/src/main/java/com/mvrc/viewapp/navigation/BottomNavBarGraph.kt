package com.mvrc.viewapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mvrc.viewapp.navigation.Screen.BookmarksScreen
import com.mvrc.viewapp.navigation.Screen.EditProfileScreen
import com.mvrc.viewapp.navigation.Screen.HomeScreen
import com.mvrc.viewapp.navigation.Screen.PostScreen
import com.mvrc.viewapp.navigation.Screen.SearchScreen
import com.mvrc.viewapp.navigation.Screen.SettingsScreen
import com.mvrc.viewapp.navigation.Screen.UserScreen
import com.mvrc.viewapp.presentation.bookmarks.BookmarksScreen
import com.mvrc.viewapp.presentation.home.HomeScreen
import com.mvrc.viewapp.presentation.search.SearchScreen
import com.mvrc.viewapp.presentation.settings.SettingsScreen

/**
 * [NavHost] that defines the navigation graph used by the bottom navigation bar
 * of the landing screen to navigate throw the main destinations.
 *
 * @param bottomNavBarController The [NavHostController] used for navigation inside the bottom navigation bar.
 * @param landingNavController The [NavHostController] used for navigating with new screens that
 * do not show the bottom navigation bar.
 * @param paddingValues The padding values of the landing screen Scaffold.
 */
@Composable
fun BottomNavBarGraph(
    bottomNavBarController: NavHostController,
    landingNavController: NavHostController,
    paddingValues: PaddingValues,
) {
    // Setting up the navigation host with the specified start destination
    NavHost(
        navController = bottomNavBarController,
        startDestination = HomeScreen.route,
    ) {
        // HomeScreen
        composable(route = HomeScreen.route) {
            HomeScreen(
                paddingValues = paddingValues,
                navigateToPostScreen = {
                    landingNavController.navigate(PostScreen.route + "/$it" + "/false")
                },
                navigateToUserProfileScreen = {
                    landingNavController.navigate(UserScreen.route + "/$it" + "/nothing")
                })
        }

        // SearchScreen
        composable(route = SearchScreen.route) {
            SearchScreen(
                paddingValues = paddingValues,
                navigateToPostScreen = {
                    landingNavController.navigate(PostScreen.route + "/$it" + "/false")
                })
        }

        // BookmarksScreen
        composable(route = BookmarksScreen.route) {
            BookmarksScreen(
                paddingValues = paddingValues,
                navigateToPostScreen = {
                    landingNavController.navigate(PostScreen.route + "/$it" + "/false")
                },
                navigateToUserProfileScreen = {
                    landingNavController.navigate(UserScreen.route + "/$it" + "/nothing")
                })
        }

        // SettingsScreen
        composable(route = SettingsScreen.route) {
            SettingsScreen(
                paddingValues = paddingValues,
                navigateToEditProfileScreen = {
                    landingNavController.navigate(EditProfileScreen.route)
                }
            )
        }
    }
}
