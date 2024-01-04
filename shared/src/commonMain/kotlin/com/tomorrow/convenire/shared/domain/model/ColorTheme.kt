package com.tomorrow.convenire.shared.domain.model

enum class ColorTheme {
    Light, Auto, Dark;
    override fun toString(): String {
        return when (this) {
            Auto -> "Auto"
            Light -> "Light"
            Dark -> "Dark"
        }
    }
}