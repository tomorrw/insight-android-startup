package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDataDTO(
    val result: Boolean? = null,
    val message: String? = null
)

@Serializable
data class NotificationDTO(
    val data: NotificationDataDTO? = null,
    val event: String = "error"
) {
    enum class EventType(val value: String) {
        CHECK_IN("check_in"),
        ERROR("error")
    }
}