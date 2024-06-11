package com.tomorrow.mobile_starter_app.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SocialLinkDTO(
    val url: String,
    val platform: Platform
) {
    @Serializable
    enum class Platform {
        @SerialName("instagram")
        Instagram,

        @SerialName("website")
        Website,

        @SerialName("linked-in")
        LinkedIn,

        @SerialName("facebook")
        Facebook,

        @SerialName("tiktok")
        Tiktok,

        @SerialName("twitter")
        Twitter,

        @SerialName("youtube")
        Youtube,

        @SerialName("whatsapp")
        WhatsApp,

        @SerialName("phone")
        Phone
    }
}
