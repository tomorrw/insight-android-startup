package com.tomorrow.convenire.feature_in_app_update.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.tomorrow.convenire.common.dialogs.CustomAlertDialog
import kotlin.system.exitProcess

@Composable
fun FlexibleUpdateDialog(
    isDismissible: Boolean = false,
    isDismissibleOnBack: Boolean = false,
    onDismiss: () -> Unit,
    onCTAClick: () -> Unit,
    appName: String
) {
    CustomAlertDialog(
        title = "$appName needs an update!",
        description = "A new update is available, please download the latest version!",
        ctaButtonText = "Update",
        onCTAClick = { onCTAClick() },
        isDismissible = isDismissible,
        onDismiss = { onDismiss() },
        dismissButtonText = "No Thanks",
        isDismissibleOnBack = isDismissibleOnBack
    )
}

@Composable
fun ForceUpdateDialog(
    onCTAClick: () -> Unit,
    appName: String
) {
    CustomAlertDialog(
        title = "$appName needs an update!",
        description = "A new update is available, please download the latest version!",
        ctaButtonText = "Update",
        onCTAClick = { onCTAClick() },
        isDismissible = false,
        onDismiss = { exitProcess(0) },
        dismissButtonText = "Close App",
        isDismissibleOnBack = false
    )
}