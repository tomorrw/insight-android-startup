package com.tomorrow.convenire.shared.domain.model

open class Speaker(
    val id: String,
    val fullName: FullName,
    val title: String,
    val image: String?,
    val socialLinks: List<SocialLink>,
    val nationality: Country? = null
) {
    data class Country(
        val code: String,
        val name: String,
        val url: String,
    )
}