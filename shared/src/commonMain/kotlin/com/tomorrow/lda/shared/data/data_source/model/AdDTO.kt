package com.tomorrow.lda.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AdDTO(
    val id: String,
    val title: String,
    @SerialName("is_highlighted")
    val isHighlighted: Boolean ?= null,
    @SerialName("full_image_url")
    val image: String?,
    val subtitle: String,
    val url: String,
)
