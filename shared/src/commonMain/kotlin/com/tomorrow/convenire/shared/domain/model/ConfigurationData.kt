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
) {
    fun getFormattedDate(): String? {
        if (hasDate.not()) return null

        return if (startDate?.month != endDate?.month)
            "${startDate?.dayOfMonth}  ${startDate?.month?.name} - ${endDate?.dayOfMonth} ${endDate?.month?.name}"
        else {
            if (startDate?.dayOfMonth == endDate?.dayOfMonth)
                "${startDate?.month?.name} ${startDate?.dayOfMonth}"
            else
                "${startDate?.month?.name} ${startDate?.dayOfMonth} - ${endDate?.dayOfMonth}"
        }
    }
}
