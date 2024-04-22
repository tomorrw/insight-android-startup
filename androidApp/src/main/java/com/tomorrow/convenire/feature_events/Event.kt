package com.tomorrow.convenire.feature_events

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tomorrow.convenire.shared.domain.model.Speaker
import java.time.LocalDateTime

data class Event(
    val id: String = "123",
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val title: String,
    val topic: String?,
    val speakers: List<Speaker>,
    val location: String,
    // the tag will update on recomposition
    val getTag: @Composable () -> Tag?,
    val minutesAttended: Int? = null,
    val color: Color? = null,
    val isMinutesDisplayed: Boolean = false
) {
    data class Tag(
        val text: String,
        val color: Color?,
        val backgroundColor: Color?
    )
}

fun Event.isNow(): Boolean {
    val now = LocalDateTime.now()
    return now.isAfter(this.startDate) && now.isBefore(this.endDate)
}
