package com.mvrc.viewapp.presentation.add_post.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

/**
 * [Composable] for creating a custom text field to use inside the Add Post Screen.
 *
 * @param modifier The modifier for styling and positioning the text field.
 * @param label The label text displayed inside the text field when it is empty.
 * @param textStyle The text style of the input text.
 * @param labelStyle The text style of the hint text.
 * @param value The current value of the text field.
 * @param imeAction The IME (Input Method Editor) action for the keyboard.
 * @param onAction The keyboard actions to perform when the IME action is triggered.
 * @param onValueChange The callback for handling changes in the text field value.
 */
@Composable
fun AddPostTextField(
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle,
    labelStyle: TextStyle,
    value: String,
    imeAction: ImeAction,
    onAction: KeyboardActions,
    onValueChange: (newValue: String) -> Unit
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction
        ),
        keyboardActions = onAction,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.inverseSurface),
        textStyle = textStyle,
        singleLine = false,
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
}