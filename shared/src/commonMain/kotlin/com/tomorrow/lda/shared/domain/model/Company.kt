package com.tomorrow.lda.shared.domain.model

import com.tomorrow.lda.shared.data.data_source.utils.Loadable
import com.tomorrow.lda.shared.data.data_source.utils.NotLoaded

data class Company(
    val id: String,
    val title: String,
    val image: String?,
    val boothDescription: String,
    val objectsClause: String,
    val categories: List<Category>,
    val socialLinks: List<SocialLink>,
    val detailPages: Loadable<List<Page>> = NotLoaded(),
    val floorMapGroup: FloorMapGroup? = null
) {
    data class Category(
        val id: String,
        val name: String,
        val description: String,
    )

    data class FloorMapGroup(val name: String, val image: String, val floorImage: String)
}