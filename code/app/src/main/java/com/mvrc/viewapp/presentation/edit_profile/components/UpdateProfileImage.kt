package com.mvrc.viewapp.presentation.edit_profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.data.model.ViewUser
import com.mvrc.viewapp.presentation.components.ViewSmallCircularProgress
import com.mvrc.viewapp.presentation.theme.UiConstants
import com.mvrc.viewapp.presentation.theme.ViewBlue

/**
 * [Composable] for uploading the [ViewUser] profile image.
 *
 * @param onClick Callback to retrieve the image chose by the user_profile.
 * @param currentProfileImage The Current Url of the User Profile Image.
 */
@Composable
fun UpdateProfileImage(currentProfileImage: String, onClick: (Uri?) -> Unit) {
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

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .height(80.r())
                .width(80.r())
                .clip(CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) {
                    galleryLauncher.launch("image/*")
                }
                .alpha(backgroundAlpha),
        ) {
            if (imageUri == null)
                Box(
                    modifier = Modifier
                        .background(if (currentProfileImage.isNotEmpty()) Color.Transparent else ViewBlue),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentProfileImage.isNotEmpty()) {
                        SubcomposeAsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = currentProfileImage,
                            contentScale = ContentScale.Crop,
                            contentDescription = "User Profile Image",
                            loading = { ViewSmallCircularProgress() },
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.icon_profile),
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(30.r()),
                            tint = Color.White
                        )
                    }
                }
            else
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 16.r())),
                    model = imageUri,
                    contentDescription = "User Profile Image",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    loading = { ViewSmallCircularProgress() },
                )
        }
    }
}