package com.tomorrow.convenire.feature_events

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
    val hasAttended: Boolean = false,
    val isLighter: Boolean = false,
    val isNow: Boolean = false
)

fun Event.isNow(): Boolean {
    val now = LocalDateTime.now()
    return now.isAfter(this.startDate) && now.isBefore(this.endDate)
}
