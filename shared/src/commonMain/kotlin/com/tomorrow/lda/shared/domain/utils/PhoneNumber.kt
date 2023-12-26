package com.tomorrow.lda.shared.domain.utils

expect class PhoneNumber(
    number: String?
) {
    var number: String?

    fun getFormattedNumberInOriginalFormat(): String?

    fun isValid(): Boolean

    var region: Region?
}

data class Region(val code: Int, val alpha2Code: String)

fun PhoneNumber.isNotValid(): Boolean = !isValid()

