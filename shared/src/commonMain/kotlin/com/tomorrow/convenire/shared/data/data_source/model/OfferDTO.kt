package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class OfferDTO(
    val id: String,
    @SerialName("post_id")
    val postId: String? = null,
    val title: String,
    @SerialName("created_at")
    val publishedAt: String? = null,
    @SerialName("full_image_url")
    val image: String?,
    val company: CompanyDTO,
)