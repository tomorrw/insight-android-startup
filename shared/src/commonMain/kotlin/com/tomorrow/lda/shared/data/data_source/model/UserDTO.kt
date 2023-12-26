package com.tomorrow.lda.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val uuid: String,
    val name: String,
    val email: String?,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("has_paid")
    val hasPaid: Boolean? = false,
    val league: LeagueDTO? = null,
    @SerialName("next_league_name")
    val nextLeagueName: String? = null,
    @SerialName("actions")
    val actions: List<ActionDTO> = listOf(),
)

