package com.mvrc.viewapp.presentation.add_post.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw
import com.mvrc.viewapp.data.model.ViewPost
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.UiConstants.openSansSemiBold

/**
 * [Composable] for uploading the [ViewPost] thumbnail image.
 *
 * @param onClick Callback to retrieve the image chose by the user_profile.
 */
@Composable
fun UploadImage(onClick: (Uri?) -> Unit) {
    // Mutable interaction source to handle button press interactions
    // and to collect the pressed state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Calculate background alpha based on the pressed state
    val backgroundAlpha = if (isPressed) UiConstants.MIN_OPACITY else UiConstants.MAX_OPACITY

    // State variable to hold the selected image URI
    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    /**
     * Launcher for the activity result used to pick an image from the gallery.
     * It uses the [ActivityResultContracts.GetContent] contract.
     */
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        // When the activity result is received, update the imageUri variable.
        onResult = { uri ->
            uri?.let {
                imageUri = if (it.path!!.startsWith("/picker/")) {
                    null
                } else {
                    it
                }
                onClick(imageUri)
            }
        }
    )

    Surface(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(size = 16.r())
            )
            .height(if (imageUri == null) 100.rh() else 170.rh())
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                galleryLauncher.launch("image/*")
            },
        color = MaterialTheme.colorScheme.tertiary.copy(alpha = backgroundAlpha)
    ) {
        if (imageUri == null)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_thumb_image),
                    contentDescription = "Thumb Image Icon",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(20.rw())
                        .alpha(backgroundAlpha)
                )
                Spacer(modifier = Modifier.height(10.rh()))
                Text(
                    text = stringResource(id = R.string.upload_image),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = backgroundAlpha),
                        fontFamily = openSansSemiBold
                    )
                )
            }
        else
            SubcomposeAsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 16.r())),
                model = imageUri,
                contentDescription = "Post Image",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                loading = { ViewSmallCircularProgress() },
            )
    }
}