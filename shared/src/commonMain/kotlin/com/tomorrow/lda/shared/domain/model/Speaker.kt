package com.tomorrow.lda.shared.domain.model

open class Speaker(
    val id: String,
    val firstName: String,
    val lastName: String,
    val title: String,
    val image: String?,
    val socialLinks: List<SocialLink>,
    val nationality: Country? = null
) {
    fun getFullName(): String = "$firstName $lastName"

    data class Country(
        val code: String,
        val name: String,
        val url: String,
    )
}