package com.tomorrow.lda.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DetailPageDTO(
    val id: Int,
    val title: String,
    val sections: List<Section>
) {
    @Serializable
    class Section(
        val title: String,
        val body: String,
        @SerialName("full_image_url")
        val image: String?,
        @SerialName("full_video_url")
        val video: String?,
    )
}