package com.mvrc.viewapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mvrc.viewapp.core.ScreenUtils.Companion.r
import com.mvrc.viewapp.core.ScreenUtils.Companion.rh
import com.mvrc.viewapp.presentation.theme.ViewGrayBorder
import com.mvrc.viewapp.presentation.theme.ViewGrayLabel

/**
 * [Composable] for creating a text field with a title, error message, and optional trailing icon.
 *
 * @param modifier The modifier for styling and positioning the text field.
 * @param title The title text displayed above the text field.
 * @param label The label text displayed inside the text field when it is empty.
 * @param value The current value of the text field.
 * @param error The error message to be displayed, or null if there is no error.
 * @param trailingIcon The composable function for rendering a trailing icon within the text field.
 * @param keyboardType The keyboard type for the text field.
 * @param imeAction The IME (Input Method Editor) action for the keyboard.
 * @param onAction The keyboard actions to perform when the IME action is triggered.
 * @param applyPasswordVisualTransformation Whether to apply a visual transformation for password input.
 * @param onValueChange The callback for handling changes in the text field value.
 */
@Composable
fun TextFieldWithTitleAndError(
    modifier: Modifier = Modifier,
    title: String,
    label: String,
    value: String,
    error: String?,
    trailingIcon: @Composable () -> Unit = {},
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Default,
    onAction: KeyboardActions = KeyboardActions.Default,
    applyPasswordVisualTransformation: Boolean = false,
    onValueChange: (newValue: String) -> Unit
) {
    // Column to vertically arrange the title, text field, and error message
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        // Title text
        Text(text = title, style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(8.rh()))

        // Text field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = onAction,
            maxLines = 1,
            singleLine = true,
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = if (error == null) ViewGrayBorder else MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(size = 16.r())
                )
                .fillMaxWidth()
                .padding(16.r()),
            textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.secondary),
            visualTransformation = if (applyPasswordVisualTransformation) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.inverseSurface),
            // Custom decorationBox to include label and trailing icon
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Display label when text field is empty
                    if (value.isEmpty()) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium.copy(color = ViewGrayLabel),
                        )
                    }
                    innerTextField() // Display the actual text field
                    Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        trailingIcon() // Display optional trailing icon
                    }
                }
            }
        )

        // Display error message if present
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.error,
                ),
                modifier = Modifier.align(
                    Alignment.End
                ),
            )
        }
    }
}