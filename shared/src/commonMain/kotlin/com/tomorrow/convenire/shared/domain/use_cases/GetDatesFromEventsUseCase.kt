package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.Session
import kotlinx.datetime.LocalDate

class GetDatesFromEventsUseCase {
    fun getDates(events: List<Session>): List<LocalDate>  = events.groupBy { it.startTime.date }.map { it.key }
}