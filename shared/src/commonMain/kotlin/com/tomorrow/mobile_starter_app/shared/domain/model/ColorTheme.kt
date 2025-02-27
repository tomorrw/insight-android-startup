package com.tomorrow.mobile_starter_app.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ColorTheme {
    Light, Auto, Dark;

    fun toBoolean() = when (this) {
        Light -> false
        Auto -> null
        Dark -> true
    }
}