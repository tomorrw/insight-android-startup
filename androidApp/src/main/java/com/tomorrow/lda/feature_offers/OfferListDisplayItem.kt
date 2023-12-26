package com.tomorrow.lda.feature_offers

import com.tomorrow.lda.feature_listing.ListDisplayItemInterface

class OfferDisplayItem(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val imageUrlString: String?,
    val postId: String,
) : ListDisplayItemInterface