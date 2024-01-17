package com.tomorrow.convenire.mappers

import com.tomorrow.convenire.feature_listing.ListDisplayItem
import com.tomorrow.convenire.shared.domain.model.Company

fun Company.toListDisplayItem() = ListDisplayItem(
    id = this.id,
    title = this.title,
    description = this.objectsClause,
    imageUrlString = this.image ?: "",
)