package com.tomorrow.convenire.common.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomAlertDialog(
    title: String,
    description: String,
    ctaButtonText: String,
    onCTAClick: () -> Unit,
    isDismissible: Boolean,
    isDismissibleOnBack: Boolean,
    ctasEnabled: Boolean = true,
    onDismiss: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    dismissButtonText: String,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {
    AlertDialog(
        modifier = Modifier.padding(horizontal = 8.dp),
        onDismissRequest = { onDismiss() },
        containerColor = containerColor,
        confirmButton = {
            Text(
                text = ctaButtonText,
                modifier = Modifier
                    .clip(ButtonDefaults.textShape)
                    .clickable { if (ctasEnabled) onCTAClick() }
                    .padding(8.dp),
                style = LocalTextStyle.current.copy(MaterialTheme.colorScheme.surfaceVariant)
            )
        }, dismissButton = {
            if (isDismissible) Text(
                text = dismissButtonText,
                modifier = Modifier
                    .clip(ButtonDefaults.textShape)
                    .clickable { if (ctasEnabled) onDismiss() }
                    .padding(8.dp),
                style = LocalTextStyle.current.copy(MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        title = { Text(text = title) },
        text = {
            Text(
                text = description,
                style = LocalTextStyle.current.copy(MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Spacer(Modifier.height(24.dp))
        },
        icon = icon,
        shape = RoundedCornerShape(8.dp),
        properties = DialogProperties(
            dismissOnBackPress = isDismissibleOnBack,
            dismissOnClickOutside = isDismissible,
        )
    )
}