package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TicketDataDTO(
    @SerialName("convention_name")
    val conventionName: String = "Convenire",
    @SerialName("start_date")
    val conventionStartDate: String? = null,
    @SerialName("end_date")
    val conventionEndDate: String? = null,
    @SerialName("ticket_description")
    val ticketDescription: String = "Your Digital Identity",
    val showTicket: Boolean? = null,
)