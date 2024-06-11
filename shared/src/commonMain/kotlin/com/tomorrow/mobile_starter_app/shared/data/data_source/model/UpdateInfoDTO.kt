package com.tomorrow.mobile_starter_app.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateInfoDTO(
    val version: String,
    @SerialName("soft_update_date")
    val softUpdateDate: String,
    @SerialName("hard_update_date")
    val hardUpdateDate: String,
)
