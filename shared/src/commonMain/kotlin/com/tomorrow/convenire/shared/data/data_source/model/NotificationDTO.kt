package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDTO(
    val result: Boolean,
    val message: String,
)
