package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TicketDataDTO(
    @SerialName("ticket_description")
    val ticketDescription: String = "Your Digital Identity",
    val showTicket: Boolean? = false,
    val conference: ConferenceDTO? = ConferenceDTO()
)