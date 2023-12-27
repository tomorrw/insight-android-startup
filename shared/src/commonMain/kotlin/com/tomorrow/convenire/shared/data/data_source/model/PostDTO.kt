package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PostDTO(
    val id: String,
    val title: String,
    @SerialName("quick_description")
    val description: String,
    @SerialName("full_image_url")
    val image: String?,
    @SerialName("detail_page")
    val detailPage: DetailPageDTO? = null,
    @SerialName("is_highlighted")
    val isHighlighted: Int,
    @SerialName("social_links")
    val socialLinks: List<SocialLinkDTO>,
    @SerialName("created_at")
    val publishedAt: String,
    val company: CompanyDTO? = null,
    val actions: List<ActionDTO>? = listOf()
)