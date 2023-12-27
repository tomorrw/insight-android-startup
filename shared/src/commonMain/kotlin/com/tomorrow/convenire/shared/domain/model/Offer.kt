package com.tomorrow.convenire.shared.domain.model

import kotlinx.datetime.LocalDateTime

class Offer(
    val id: String,
    val postId: String? = null,
    val title: String,
    val publishedAt: LocalDateTime?= null,
    val image: String?,
    val company: Company,
)