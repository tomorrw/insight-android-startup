package com.tomorrow.convenire.shared.domain.utils.validator.values.lang

object EnglishLocalizedStrings : ValidationStrings() {
    override val languageCode: String = "en"

    override fun fieldRequired(fieldName: String): String = "$fieldName is required!"

    override fun fieldNotValid(fieldName: String): String = "$fieldName is not valid"
}