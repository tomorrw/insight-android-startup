package com.tomorrow.lda.mappers

import com.tomorrow.lda.feature_listing.ListDisplayItem
import com.tomorrow.lda.shared.domain.model.SpeakerDetail

fun SpeakerDetail.toListDisplayItem() = ListDisplayItem(
    id = this.id,
    title = this.getFullName(),
    description = this.title,
    imageUrlString = this.image ?: "",
)