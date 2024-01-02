package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ConfigurationDTO(
    //new
    @SerialName("ticket_title")
    val title: String = "Convenire",
    @SerialName("ticket_sub_title")
    val ticketSubTitle: String? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("ticket_description")
    val ticketDescription: String = "Your Digital Identity",
    @SerialName("show_ticket")
    val showTicket: Boolean = false,
    @SerialName("show_exhibition_map")
    val showExhibitionMap : Boolean = false,
    @SerialName("show_exhibition_offers")
    val showExhibitionOffers : Boolean = false,
    @SerialName("ticket_status")
    val ticketStatus: String? = null,

)