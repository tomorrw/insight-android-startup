package com.tomorrow.lda.shared.domain.model

import kotlinx.datetime.LocalDateTime

class Spinner (
    val id: String,
    val description: String,
    val publishedAt: LocalDateTime,
    val company: Company,
)