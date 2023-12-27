package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SpeakerDTO(
    val id: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val title: String,
    @SerialName("full_image_url")
    val image: String?,
    val events: List<SessionDTO>? = null,
    @SerialName("detail_pages")
    val detailPages: List<DetailPageDTO>? = null,
    @SerialName("social_links")
    val socialLinks: List<SocialLinkDTO>,
    val country: Country? = null,
) {
    @Serializable
    data class Country(
        val code: String,
        val name: String,
        val url: String? = null,
    )
}