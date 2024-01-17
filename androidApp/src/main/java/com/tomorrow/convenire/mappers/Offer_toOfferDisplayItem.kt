package com.tomorrow.convenire.mappers

import com.tomorrow.convenire.feature_offers.OfferDisplayItem
import com.tomorrow.convenire.shared.domain.model.Offer

fun Offer.toOfferDisplayItem(): OfferDisplayItem? = this.postId?.let {
    OfferDisplayItem(
        id = this.id,
        title = this.title,
        description = this.company.title,
        imageUrlString = this.image,
        postId = it
    )
}