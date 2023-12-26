package com.tomorrow.lda.mappers

import com.tomorrow.lda.feature_offers.OfferDisplayItem
import com.tomorrow.lda.shared.domain.model.Offer

fun Offer.toOfferDisplayItem(): OfferDisplayItem? = this.postId?.let {
    OfferDisplayItem(
        id = this.id,
        title = this.title,
        description = this.company.title,
        imageUrlString = this.image,
        postId = it
    )
}