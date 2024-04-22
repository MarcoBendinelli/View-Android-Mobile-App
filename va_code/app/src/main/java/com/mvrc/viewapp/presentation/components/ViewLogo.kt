package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw

/**
 * [Composable] for displaying the app logo.
 *
 * @param modifier The modifier for styling and positioning the logo.
 */
@Composable
fun ViewLogo(modifier: Modifier = Modifier) {
    // Display the app logo using the provided modifier
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo app",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .width(134.rw())
            .testTag("viewLogo")
    )
}