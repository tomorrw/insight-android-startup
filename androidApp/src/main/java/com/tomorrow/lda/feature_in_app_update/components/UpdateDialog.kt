package com.tomorrow.lda.feature_in_app_update.components

import androidx.compose.runtime.Composable
import com.tomorrow.lda.common.dialogs.CustomAlertDialog
import kotlin.system.exitProcess

@Composable
fun FlexibleUpdateDialog(
    isDismissible: Boolean = false,
    isDismissibleOnBack: Boolean = false,
    onDismiss: () -> Unit,
    onCTAClick: () -> Unit,
) {
    CustomAlertDialog(
        title = "FMD needs an update!",
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
) {
    CustomAlertDialog(
        title = "FMD needs an update!",
        description = "A new update is available, please download the latest version!",
        ctaButtonText = "Update",
        onCTAClick = { onCTAClick() },
        isDismissible = false,
        onDismiss = { exitProcess(0) },
        dismissButtonText = "Close App",
        isDismissibleOnBack = false
    )
}