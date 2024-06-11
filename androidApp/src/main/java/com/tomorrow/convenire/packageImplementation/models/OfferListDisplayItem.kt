package com.tomorrow.convenire.packageImplementation.models

import com.tomorrow.listdisplay.ListDisplayItemInterface

class OfferDisplayItem(
    override val id: String,
    override val title: String,
    override val description: String?,
    override val imageUrlString: String?,
    val postId: String,
) : ListDisplayItemInterface