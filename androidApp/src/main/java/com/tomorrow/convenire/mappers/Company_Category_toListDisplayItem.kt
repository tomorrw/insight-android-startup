package com.tomorrow.convenire.mappers

import com.tomorrow.convenire.feature_listing.ListDisplayItem
import com.tomorrow.convenire.shared.domain.model.Company

fun Company.Category.toListDisplayItem() = ListDisplayItem(
    id = this.id, title = this.name, description = this.description, imageUrlString = null
)