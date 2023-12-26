package com.tomorrow.lda.mappers

import com.tomorrow.lda.feature_listing.ListDisplayItem
import com.tomorrow.lda.shared.domain.model.Company

fun Company.Category.toListDisplayItem() = ListDisplayItem(
    id = this.id, title = this.name, description = this.description, imageUrlString = null
)