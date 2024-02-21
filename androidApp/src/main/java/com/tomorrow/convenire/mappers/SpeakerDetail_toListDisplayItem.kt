package com.tomorrow.convenire.mappers

import com.tomorrow.convenire.feature_listing.ListDisplayItem
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail

fun SpeakerDetail.toListDisplayItem() = ListDisplayItem(
    id = this.id,
    title = this.fullName.getFormattedName(),
    description = this.title,
    imageUrlString = this.image ?: "",
)