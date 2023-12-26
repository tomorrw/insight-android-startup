package com.tomorrow.lda.mappers

import com.tomorrow.lda.feature_listing.ListDisplayItem
import com.tomorrow.lda.shared.domain.model.Company

fun Company.toListDisplayItem() = ListDisplayItem(
    id = this.id,
    title = this.title,
    description = this.objectsClause,
    imageUrlString = this.image ?: "",
)