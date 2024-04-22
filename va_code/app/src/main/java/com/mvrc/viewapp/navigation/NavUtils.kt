package com.mvrc.viewapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.mvrc.viewapp.AppViewModel
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.Constants
import com.mvrc.viewapp.core.Constants.ADD_POST_SCREEN
import com.mvrc.viewapp.core.Constants.AUTH_SCREEN
import com.mvrc.viewapp.core.Constants.BOOKMARKS_SCREEN
import com.mvrc.viewapp.core.Constants.EDIT_PROFILE_SCREEN
import com.mvrc.viewapp.core.Constants.HOME_SCREEN
import com.mvrc.viewapp.core.Constants.LANDING_NAV_GRAPH
import com.mvrc.viewapp.core.Constants.LANDING_SCREEN
import com.mvrc.viewapp.core.Constants.POST_SCREEN
import com.mvrc.viewapp.core.Constants.SEARCH_SCREEN
import com.mvrc.viewapp.core.Constants.SETTINGS_SCREEN
import com.mvrc.viewapp.core.Constants.SIGN_UP_SCREEN
import com.mvrc.viewapp.core.Constants.TOPIC_SELECTION_SCREEN
import com.mvrc.viewapp.core.Constants.USER_SCREEN
import com.mvrc.viewapp.core.Constants.USER_SELECTION_SCREEN
import com.mvrc.viewapp.domain.state.AppStatus

/**
 * Sealed class representing different screens in the application, each associated with a unique route.
 *
 * @property route The route string associated with the screen.
 */
sealed class Screen(
    val route: String,
    val icon: Int? = null,
    val iconFilled: Int? = null,
    val iconFilledDark: Int? = null
) {
    object AuthScreen : Screen(route = AUTH_SCREEN)
    object SignInScreen : Screen(route = Constants.SIGN_IN_SCREEN)
    object ForgotPasswordScreen : Screen(route = Constants.FORGOT_PASSWORD_SCREEN)
    object EmailSentScreen : Screen(route = Constants.EMAIL_SENT_SCREEN)
    object SignUpScreen : Screen(route = SIGN_UP_SCREEN)
    object TopicSelectionScreen : Screen(route = TOPIC_SELECTION_SCREEN)
    object UserSelectionScreen : Screen(route = USER_SELECTION_SCREEN)

    object LandingScreen : Screen(route = LANDING_SCREEN)
    object LandingNavGraph : Screen(route = LANDING_NAV_GRAPH)

    object PostScreen : Screen(route = POST_SCREEN)
    object UserScreen : Screen(route = USER_SCREEN)
    object EditProfileScreen : Screen(route = EDIT_PROFILE_SCREEN)

    object HomeScreen : Screen(
        route = HOME_SCREEN,
        icon = R.drawable.icon_home,
        iconFilled = R.drawable.icon_home_filled,
        iconFilledDark = R.drawable.icon_home_filled_dark,
    )

    object SearchScreen : Screen(
        route = SEARCH_SCREEN,
        icon = R.drawable.icon_search,
        iconFilled = R.drawable.icon_search_filled,
        iconFilledDark = R.drawable.icon_search_filled_dark
    )

    object AddPostScreen : Screen(route = ADD_POST_SCREEN, icon = R.drawable.icon_add)

    object BookmarksScreen : Screen(
        route = BOOKMARKS_SCREEN,
        icon = R.drawable.icon_bookmark,
        iconFilled = R.drawable.icon_bookmark_filled,
        iconFilledDark = R.drawable.icon_bookmark_filled_dark
    )

    object SettingsScreen : Screen(
        route = SETTINGS_SCREEN,
        icon = R.drawable.icon_settings,
        iconFilled = R.drawable.icon_settings_filled,
        iconFilledDark = R.drawable.icon_settings_filled_dark
    )
}

/**
 * It determines the appropriate initial screen based on the application's status.
 *
 * @param appViewModel the [AppViewModel] in charge on maintaining the [AppStatus]
 */
@Composable
fun getRoute(
    appViewModel: AppViewModel
): String {
    return when (appViewModel.appStatus.collectAsState().value) {
        AppStatus.AUTHENTICATED -> Screen.LandingNavGraph.route
        AppStatus.FIRST_SELECTION -> Screen.TopicSelectionScreen.route
        AppStatus.UNAUTHENTICATED -> Screen.AuthScreen.route
    }
}