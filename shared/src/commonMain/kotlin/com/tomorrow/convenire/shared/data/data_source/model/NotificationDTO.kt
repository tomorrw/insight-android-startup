package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDataDTO(
    val result: Boolean? = null,
    val message: String? = null,
    @SerialName("socket_id")
    val socketId: String? = "",
    val type: String? = null
){
    enum class EventType(val value: String) {
        CHECK_IN("check_in"),
        CHECK_OUT("check_out"),
        ERROR("error")
    }
}

@Serializable
data class NotificationDTO(
    @SerialName("data")
    val data: String? = null

)