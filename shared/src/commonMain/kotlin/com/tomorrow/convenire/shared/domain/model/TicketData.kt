package com.tomorrow.convenire.shared.domain.model

import kotlinx.datetime.LocalDate

data class TicketData(
    val title: String,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val hasDate: Boolean = startDate != null && endDate != null,
    val description: String,
    val showTicket: Boolean,
)
