package com.tomorrow.lda.shared.domain.use_cases

import com.tomorrow.lda.shared.domain.model.Session
import kotlinx.datetime.*

class GetAppropriateDisplayedDayForEvent {
    fun getDateTime(events: List<Session>): LocalDateTime? = getDay(events)?.atTime(0,0,0)

    fun getDay(events: List<Session>): LocalDate? {
        if (events.isEmpty()) return null

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        if (events.any { it.startTime.date == today }) return today

        val firstEvent = events.minByOrNull { it.startTime }

        if (firstEvent != null) return firstEvent.startTime.date

        return today
    }
}