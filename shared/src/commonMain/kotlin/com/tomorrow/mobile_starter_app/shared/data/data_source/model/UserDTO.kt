package com.tomorrow.mobile_starter_app.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String,
    val uuid: String,
    val salutation: SalutationDTO? = SalutationDTO.None,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val name: String,
    val email: String?,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("notification_topics")
    val notificationTopics: List<String>? = null,
)

