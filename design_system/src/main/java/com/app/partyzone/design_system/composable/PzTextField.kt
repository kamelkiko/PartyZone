package com.app.partyzone.design_system.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.app.partyzone.design_system.R
import com.app.partyzone.design_system.theme.Theme.colors
import com.app.partyzone.design_system.theme.Theme.radius
import com.app.partyzone.design_system.theme.Theme.typography

@Composable
fun PzTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    shapeRadius: Shape = RoundedCornerShape(radius.large),
    leadingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    errorMessage: String = "",
    isError: Boolean = errorMessage.isNotEmpty(),
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            modifier = textFieldModifier,
            value = text,
            placeholder = {
                Text(
                    hint,
                    style = typography.title,
                    color = colors.contentTertiary
                )
            },
            onValueChange = onValueChange,
            shape = shapeRadius,
            textStyle = typography.body.copy(colors.contentPrimary),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (keyboardType == KeyboardType.Password) {
                { TrailingIcon(showPassword) { showPassword = !showPassword } }
            } else null,
            leadingIcon = leadingIcon,
            visualTransformation = PzVisualTransformation(keyboardType, showPassword),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.primary,
                unfocusedContainerColor = colors.surface,
                focusedBorderColor = Color(0xFFF703D0).copy(alpha = 0.37f),
                unfocusedBorderColor = colors.contentBorder.copy(alpha = 0.1f),
                errorBorderColor = Color(0xFFFB0160),
                errorCursorColor = Color(0xFFFB0160),
                cursorColor = colors.contentTertiary,
            )
            //colors = TextFieldDefaults.outlinedTextFieldColors(
//                containerColor = ContainerColor(isError, correctValidation),
//                unfocusedBorderColor = colors.contentBorder.copy(alpha = 0.1f),
//                focusedBorderColor = colors.contentTertiary.copy(alpha = 0.2f),
//                errorBorderColor = colors.primary.copy(alpha = 0.5f),
//                errorCursorColor = colors.primary,
//                cursorColor = colors.contentTertiary,
//            ),
        )

        AnimatedVisibility(isError) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp),
                style = typography.body,
                color = Color(0xFFFB0160)
            )
        }
    }
}

@Composable
private fun TrailingIcon(
    showPassword: Boolean,
    togglePasswordVisibility: () -> Unit,
) {
    IconButton(onClick = { togglePasswordVisibility() }) {
        Icon(
            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
            contentDescription = if (showPassword) stringResource(R.string.show_password)
            else stringResource(R.string.hide_password),
            tint = colors.contentTertiary
        )
    }
}

@Composable
private fun PzVisualTransformation(
    keyboardType: KeyboardType,
    showPassword: Boolean,
): VisualTransformation {
    return if (showPassword || keyboardType != KeyboardType.Password && keyboardType != KeyboardType.NumberPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
}