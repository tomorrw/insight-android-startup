package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDataDTO(
    val result: Boolean? = null,
    val message: String? = null
)

@Serializable
data class NotificationDTO(
    val data: String? = null,
    val event: String
) {
    enum class EventType(val value: String) {
        CHECK_IN("check_in"),
        CHECK_OUT("check_out"),
        ERROR("error")
    }
}