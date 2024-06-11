package com.tomorrow.mobile_starter_app.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val result: Boolean,
    val message: String,
)
