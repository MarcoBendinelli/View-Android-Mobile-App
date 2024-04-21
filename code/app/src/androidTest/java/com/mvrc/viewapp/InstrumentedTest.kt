package com.mvrc.viewapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun showAuthScreen() {
        // Launch the MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        composeTestRule.onNodeWithTag("viewLogo").assertExists()

        // Test Sign In

        composeTestRule.onNodeWithTag("signInFromAuthButton").performClick()

        composeTestRule.onNodeWithTag("emailField").performTextInput("asd@asd.asd")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("asd123")

        composeTestRule.onNodeWithTag("signInButton").performClick()

        // Test Navigation Bar

        composeTestRule.onNodeWithTag("iconButtonNavBarHome").performClick()

        composeTestRule.onNodeWithTag("homeFilters", useUnmergedTree = true).assertIsDisplayed()

        // Test Small Post and Post page visual

        composeTestRule.onNodeWithTag("smallPostButton").performClick()

        composeTestRule.onNodeWithTag("backButton").performClick()

        composeTestRule.onNodeWithTag("iconButtonNavBarSettings").performClick()

        // Search Screen

        composeTestRule.onNodeWithTag("iconButtonNavBarSearch").performClick()

        // Bookmarks Screen

        composeTestRule.onNodeWithTag("iconButtonNavBarBookmark").performClick()

        // Test Big Post and Post page visual

        composeTestRule.onNodeWithTag("bigPostButton").performClick()

        composeTestRule.onNodeWithTag("backButton").performClick()

        composeTestRule.onNodeWithTag("iconButtonNavBarSettings").performClick()

        // Test Log Out

        composeTestRule.onNodeWithTag("logoutButton").performClick()

        composeTestRule.onNodeWithTag("viewLogo").assertExists()

        // Test Sign Up

        composeTestRule.onNodeWithTag("signUpFromAuthButton").performClick()

        composeTestRule.onNodeWithTag("emailField").performTextInput("asd@asd.asd")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("asd123")
        composeTestRule.onNodeWithTag("repeatedPasswordField").performTextInput("asd123")

        composeTestRule.onNodeWithTag("signUpButton").performClick()

        composeTestRule.onNodeWithTag("topicMusicButton").performClick()

        composeTestRule.onNodeWithTag("continueButton").performClick()

        composeTestRule.onNodeWithTag("skipButton").performClick()

        // Test Log Out

        composeTestRule.onNodeWithTag("iconButtonNavBarSettings").performClick()

        composeTestRule.onNodeWithTag("logoutButton").performClick()

        composeTestRule.onNodeWithTag("viewLogo").assertExists()

        // Test Reset Password

        composeTestRule.onNodeWithTag("signInFromAuthButton").performClick()

        composeTestRule.onNodeWithTag("signInForgotPasswordButton").performClick()

        composeTestRule.onNodeWithTag("emailField").performTextInput("asd@asd.asd")

        composeTestRule.onNodeWithTag("resetButton").performClick()

        composeTestRule.onNodeWithTag("backToLoginButton").performClick()
    }
}