package com.tomorrow.lda.feature_events

import androidx.compose.ui.graphics.Color
import com.tomorrow.lda.shared.domain.model.Speaker
import java.time.LocalDateTime

data class Event(
    val id: String = "123",
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val title: String,
    val topic: String?,
    val speakers: List<Speaker>,
    val location: String,
    val hasAttended: Boolean = false,
    val color: Color,
    val isNow: Boolean = false
)

fun Event.isNow(): Boolean {
    val now = LocalDateTime.now()
    return now.isAfter(this.startDate) && now.isBefore(this.endDate)
}
