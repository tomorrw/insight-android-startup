package com.tomorrow.lda.common.fields

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    supportingText: @Composable (() -> Unit)? = null
) =
    OutlinedTextField(
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary
        ),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        modifier = modifier.height(60.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        shape = RoundedCornerShape(8.dp),
        supportingText = supportingText,
        leadingIcon = icon?.let {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = "name",
                )
            }
        },
        visualTransformation = visualTransformation
    )