package com.tomorrow.mobile_starter_app.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ConferenceDTO (
    val id: String? = null,
    @SerialName("is_active")
    val isActive: Boolean = true,
    val name: String = "Convenire",
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null
)