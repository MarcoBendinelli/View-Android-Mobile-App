package com.mvrc.viewapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mvrc.viewapp.core.Constants.COMING_FROM_USER_SCREEN_ARGUMENT
import com.mvrc.viewapp.core.Constants.POST_ARGUMENT
import com.mvrc.viewapp.core.Constants.USER_ARGUMENT
import com.mvrc.viewapp.navigation.Screen.AddPostScreen
import com.mvrc.viewapp.navigation.Screen.EditProfileScreen
import com.mvrc.viewapp.navigation.Screen.HomeScreen
import com.mvrc.viewapp.navigation.Screen.LandingScreen
import com.mvrc.viewapp.navigation.Screen.PostScreen
import com.mvrc.viewapp.navigation.Screen.SearchScreen
import com.mvrc.viewapp.navigation.Screen.SettingsScreen
import com.mvrc.viewapp.navigation.Screen.UserScreen
import com.mvrc.viewapp.presentation.add_post.AddPostScreen
import com.mvrc.viewapp.presentation.bookmarks.BookmarksScreen
import com.mvrc.viewapp.presentation.edit_profile.EditProfileScreen
import com.mvrc.viewapp.presentation.home.HomeScreen
import com.mvrc.viewapp.presentation.landing.LandingScreen
import com.mvrc.viewapp.presentation.post.PostScreen
import com.mvrc.viewapp.presentation.search.SearchScreen
import com.mvrc.viewapp.presentation.settings.SettingsScreen
import com.mvrc.viewapp.presentation.user_profile.UserProfileScreen


/**
 * [Composable] representing the main landing screen of the application.
 * It includes a custom bottom navigation bar allowing the navigation between
 * the main screens: [HomeScreen], [SearchScreen], [BookmarksScreen] and [SettingsScreen].
 * It also allows to navigate to the [AddPostScreen] to create a new post.
 */
@Composable
fun LandingNavGraph() {
    // Navigation controller to manage navigation within the bottom navigation bar.
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LandingScreen.route,
    ) {
        // Main Screen with Navigation Bar.
        composable(
            route = LandingScreen.route
        ) {
            LandingScreen(
                landingNavController = navController
            )
        }

        // Add Post Screen.
        composable(route = AddPostScreen.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }) {
            AddPostScreen(navigateBack = {
                if (!navController.popBackStack()) {
                    navController.navigate(LandingScreen.route)
                }
            })
        }

        // Post Screen
        composable(
            route = PostScreen.route + "/{${POST_ARGUMENT}}" + "/{${COMING_FROM_USER_SCREEN_ARGUMENT}}",
            arguments = listOf(
                navArgument(POST_ARGUMENT) { type = NavType.StringType },
                navArgument(COMING_FROM_USER_SCREEN_ARGUMENT) { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            PostScreen(
                postId = backStackEntry.arguments?.getString(POST_ARGUMENT)!!,
                comingFromUserScreen = backStackEntry.arguments?.getBoolean(
                    COMING_FROM_USER_SCREEN_ARGUMENT
                )!!,
                navigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(LandingScreen.route)
                    }
                },
                navigateToUserProfileScreen = { userId, postId ->
                    navController.navigate(UserScreen.route + "/$userId" + "/$postId")
                })
        }

        // User Screen
        composable(
            route = UserScreen.route + "/{${USER_ARGUMENT}}" + "/{${POST_ARGUMENT}}",
            arguments = listOf(
                navArgument(USER_ARGUMENT) { type = NavType.StringType },
                navArgument(POST_ARGUMENT) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            UserProfileScreen(
                userId = backStackEntry.arguments?.getString(USER_ARGUMENT)!!,
                comingFromPostId = backStackEntry.arguments?.getString(POST_ARGUMENT)!!,
                navigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(LandingScreen.route)
                    }
                },
                navigateToPostScreen = {
                    navController.navigate(PostScreen.route + "/$it" + "/true")
                })
        }

        // Edit Profile Screen
        composable(
            route = EditProfileScreen.route
        ) {
            EditProfileScreen(
                navigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(LandingScreen.route)
                    }
                }
            )
        }
    }
}