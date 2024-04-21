package com.mvrc.viewapp.presentation.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.R
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.core.ScreenUtils.Companion.rw

/**
 * Custom Search Text Field [Composable]
 *
 * @param value The current value of the text field.
 * @param onValueChange The callback that is invoked when the text in the text field changes.
 */
@Composable
fun SearchTextField(value: String, onValueChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(size = 16.r())
            )
            .height(50.rh())
            .padding(horizontal = 20.rw(), vertical = 15.rh())
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_search),
                contentDescription = "Search Icon",
                contentScale = ContentScale.FillHeight,
            )

            Spacer(modifier = Modifier.width(16.rw()))

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions {
                    keyboardController?.hide()
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.inverseSurface),
                textStyle = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
                singleLine = true,
                decorationBox = { innerTextField ->
                    // Display label when text field is empty
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.search),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    innerTextField() // Display the actual text field
                }
            )
        }
    }
}