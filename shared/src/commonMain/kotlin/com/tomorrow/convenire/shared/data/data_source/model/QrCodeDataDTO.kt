package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class QrCodeDataDTO(
    @SerialName("convention_name")
    val conventionName: String,
    @SerialName("start_date")
    val conventionStartDate: String,
    @SerialName("end_date")
    val conventionEndDate: String,
    @SerialName("ticket_description")
    val ticketDescription: String,
)