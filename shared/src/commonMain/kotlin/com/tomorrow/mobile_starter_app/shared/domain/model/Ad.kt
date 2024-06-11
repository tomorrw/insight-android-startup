package com.tomorrow.mobile_starter_app.shared.domain.model

class Ad(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val url: String,
    val image: String? = null,
    val isHighlighted: Boolean
)