package com.tomorrow.lda.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CompanyDTO(
    val id: String,
    val title: String,
    @SerialName("full_image_url")
    val image: String?,
    val events: List<SessionDTO>,
    val speakers: List<SpeakerDTO>,
    @SerialName("product_categories")
    val productCategories: List<ProductCategory>,
    @SerialName("objects_clause")
    val objectsClause: String,
    @SerialName("booth_number")
    val boothDescription: String,
    @SerialName("detail_pages")
    val detailPages: List<DetailPageDTO>,
    @SerialName("social_links")
    val socialLinks: List<SocialLinkDTO>,
    @SerialName("floor_map_group")
    val floorMapGroup: FloorMapGroup? = null
) {
    @Serializable
    class ProductCategory(
        val id: String,
        val name: String,
        @SerialName("quick_description")
        val description: String,
    )

    @Serializable
    class FloorMapGroup(
        val name: String,
        @SerialName("full_image_url")
        val image: String,
        @SerialName("floor_map")
        val floorMap: FloorMap,
    )

    @Serializable
    class FloorMap(
        @SerialName("full_image_url")
        val image: String
    )
}