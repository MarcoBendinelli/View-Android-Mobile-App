package com.mvrc.viewapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mvrc.viewapp.AppViewModel
import com.mvrc.viewapp.navigation.Screen.AuthScreen
import com.mvrc.viewapp.navigation.Screen.EmailSentScreen
import com.mvrc.viewapp.navigation.Screen.ForgotPasswordScreen
import com.mvrc.viewapp.navigation.Screen.LandingNavGraph
import com.mvrc.viewapp.navigation.Screen.SignInScreen
import com.mvrc.viewapp.navigation.Screen.SignUpScreen
import com.mvrc.viewapp.navigation.Screen.TopicSelectionScreen
import com.mvrc.viewapp.navigation.Screen.UserSelectionScreen
import com.mvrc.viewapp.presentation.auth.AuthScreen
import com.mvrc.viewapp.presentation.email_sent.EmailSentScreen
import com.mvrc.viewapp.presentation.first_user_selection.topic_selection.TopicSelectionScreen
import com.mvrc.viewapp.presentation.first_user_selection.user_selection.UserSelectionScreen
import com.mvrc.viewapp.presentation.forgot_password.ForgotPasswordScreen
import com.mvrc.viewapp.presentation.sign_in.SignInScreen
import com.mvrc.viewapp.presentation.sign_up.SignUpScreen

/**
 * [NavHost] defining the navigation graph for the app authentication.
 *
 * @param navController The [NavHostController] used for navigation.
 * @param appViewModel The [AppViewModel] instance provided by Hilt.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthNavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel,
) {
    // Setting up the navigation host with the specified start destination
    NavHost(
        navController = navController,
        startDestination = getRoute(appViewModel = appViewModel),
    ) {
        // AuthScreen
        composable(route = AuthScreen.route) {
            AuthScreen(
                navigateToSignInScreen = {
                    navController.navigate(SignInScreen.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(SignUpScreen.route)
                },
                appViewModel = appViewModel
            )
        }

        // SignInScreen
        composable(route = SignInScreen.route) {
            SignInScreen(
                navigateToForgotPasswordScreen = {
                    navController.navigate(ForgotPasswordScreen.route)
                },
                navigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AuthScreen.route)
                    }
                }
            )
        }

        // ForgotPasswordScreen
        composable(route = ForgotPasswordScreen.route) {
            ForgotPasswordScreen(
                navigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(SignInScreen.route)
                    }
                },
                navigateToEmailSentScreen = {
                    navController.navigate(EmailSentScreen.route)
                }
            )
        }

        // EmailSentScreen
        composable(route = EmailSentScreen.route) {
            EmailSentScreen(
                navigateToLoginScreen = {
                    navController.popBackStack(route = SignInScreen.route, inclusive = false)
                }
            )
        }

        // SignUpScreen
        composable(route = SignUpScreen.route) {
            SignUpScreen(
                navigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(AuthScreen.route)
                    }
                },
                appViewModel = appViewModel
            )
        }

        // TopicSelectionScreen
        composable(route = TopicSelectionScreen.route) {
            TopicSelectionScreen(
                navigateToUserSelectionScreen = {
                    navController.navigate(UserSelectionScreen.route)
                }
            )
        }

        // UserSelectionScreen
        composable(route = UserSelectionScreen.route) {
            UserSelectionScreen(
                appViewModel = appViewModel
            )
        }

        // LandingScreen
        composable(route = LandingNavGraph.route) {
            LandingNavGraph()
        }
    }
}
