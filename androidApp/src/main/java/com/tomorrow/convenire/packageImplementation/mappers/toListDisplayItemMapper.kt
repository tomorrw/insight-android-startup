package com.tomorrow.convenire.packageImplementation.mappers

import com.tomorrow.convenire.packageImplementation.models.OfferDisplayItem
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.model.Offer
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import com.tomorrow.listdisplay.ListDisplayItem

fun Company.toListDisplayItem() = ListDisplayItem(
    id = this.id,
    title = this.title,
    description = this.objectsClause,
    imageUrlString = this.image ?: "",
)

fun Company.Category.toListDisplayItem() = ListDisplayItem(
    id = this.id, title = this.name, description = this.description, imageUrlString = null
)

fun Offer.toOfferDisplayItem(): OfferDisplayItem? = this.postId?.let {
    OfferDisplayItem(
        id = this.id,
        title = this.title,
        description = this.company.title,
        imageUrlString = this.image,
        postId = it
    )
}

fun SpeakerDetail.toListDisplayItem() = ListDisplayItem(
    id = this.id,
    title = this.fullName.getFormattedName(),
    description = this.title,
    imageUrlString = this.image ?: "",
)