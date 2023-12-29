package com.tomorrow.convenire.shared.domain.model

import kotlinx.datetime.LocalDate

data class ConfigurationData(
    val title: String,
    val subTitle: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val hasDate: Boolean = startDate != null && endDate != null,
    val description: String,
    val showTicket: Boolean,
    val status: String? = null,
    val showExhibitionMap: Boolean,
    val showExhibitionOffers: Boolean,
)
