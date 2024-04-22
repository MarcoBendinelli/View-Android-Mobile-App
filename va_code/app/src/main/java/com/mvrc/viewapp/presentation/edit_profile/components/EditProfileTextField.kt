package com.mvrc.viewapp.presentation.edit_profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh

/**
 * [Composable] for creating a custom text field to use inside the Edit Profile Screen.
 *
 * @param label The label text displayed inside the text field when it is empty.
 * @param textStyle The text style of the input text.
 * @param labelStyle The text style of the hint text.
 * @param value The current value of the text field.
 * @param onValueChange The callback for handling changes in the text field value.
 */
@Composable
fun EditProfileTextField(
    label: String,
    textStyle: TextStyle,
    labelStyle: TextStyle,
    value: String,
    onValueChange: (newValue: String) -> Unit
) {
    Column {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.inverseSurface),
            textStyle = textStyle,
            singleLine = true,
            decorationBox = { innerTextField ->
                // Display label when text field is empty
                if (value.isEmpty()) {
                    Text(
                        text = label,
                        style = labelStyle,
                    )
                }
                innerTextField() // Display the actual text field
            }
        )
        Divider(modifier = Modifier.padding(top = 10.rh()))
    }
}