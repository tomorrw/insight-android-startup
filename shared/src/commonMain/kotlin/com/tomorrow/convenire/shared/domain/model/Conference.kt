package com.tomorrow.convenire.shared.domain.model

import kotlinx.datetime.LocalDate

data class Conference(
    val id: String,
    val isActive: Boolean,
    val name: String,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)
