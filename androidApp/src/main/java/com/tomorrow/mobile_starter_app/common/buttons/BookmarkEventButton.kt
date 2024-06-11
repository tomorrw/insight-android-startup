package com.tomorrow.mobile_starter_app.common.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.tomorrow.components.dialogs.CustomAlertDialog
import com.tomorrow.mobile_starter_app.R
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.OverlappingSessionUseCase
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.ShouldNotifyEventUseCase
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.ToggleShouldNotifyEventUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun BookmarkEventButton(id: String) {
    var isNotificationEnabled by remember(id) {
        mutableStateOf(ShouldNotifyEventUseCase().shouldNotify(id))
    }
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    val isConfirmAddToBookmarkVisible = remember { mutableStateOf(false) }
    val overlappingSessionName = remember { mutableStateOf<String?>(null) }

    Icon(
        painter = painterResource(
            if (isNotificationEnabled) R.drawable.baseline_bookmark_added_24
            else R.drawable.baseline_bookmark_border_24
        ),
        contentDescription = "more",
        tint = if (isNotificationEnabled) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = androidx.compose.ui.Modifier
            .clickable(interactionSource, null) {
                val tmpIsNotificationEnabled =
                    ShouldNotifyEventUseCase().shouldNotify(id)
                val overlappingSession =
                    OverlappingSessionUseCase().getOverlapping(id)

                if (!isNotificationEnabled && overlappingSession != null) {
                    isConfirmAddToBookmarkVisible.value = true
                    overlappingSessionName.value = overlappingSession.title
                    return@clickable
                }

                isConfirmAddToBookmarkVisible.value = false
                overlappingSessionName.value = ""

                job?.cancel()
                job = scope.launch {
                    ToggleShouldNotifyEventUseCase().toggleShouldNotify(id)
                    isNotificationEnabled = !tmpIsNotificationEnabled
                    if (!tmpIsNotificationEnabled) Firebase.messaging.subscribeToTopic("session_${id}")
                    else Firebase.messaging.unsubscribeFromTopic("session_${id}")
                }
            }
    )

    if (isConfirmAddToBookmarkVisible.value) CustomAlertDialog(
        title = "Lectures Overlap",
        description = "The lecture you are trying to bookmark overlaps ${overlappingSessionName.value?.let { " with your bookmarked lecture: `$it`" } ?: "with another bookmarked lecture."}",
        ctaButtonText = "ADD ANYWAY",
        onCTAClick = {
            job?.cancel()
            job = scope.launch {
                ToggleShouldNotifyEventUseCase().toggleShouldNotify(id)
                isNotificationEnabled = ShouldNotifyEventUseCase().shouldNotify(id)
                if (isNotificationEnabled) Firebase.messaging.subscribeToTopic("session_${id}")
                else Firebase.messaging.unsubscribeFromTopic("session_${id}")

                isConfirmAddToBookmarkVisible.value = false
            }
        },
        isDismissible = true,
        isDismissibleOnBack = true,
        onDismiss = { isConfirmAddToBookmarkVisible.value = false },
        dismissButtonText = "CANCEL",
    )
}