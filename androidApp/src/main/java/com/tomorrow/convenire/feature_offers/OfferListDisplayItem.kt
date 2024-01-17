package com.tomorrow.convenire.feature_offers

import com.tomorrow.convenire.feature_listing.ListDisplayItemInterface

class OfferDisplayItem(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val imageUrlString: String?,
    val postId: String,
) : ListDisplayItemInterface