package com.tomorrow.convenire.shared.domain.utils.validator.values.lang

object ArabicLocalizedStrings : ValidationStrings() {
    override val languageCode: String = "ar"

    override fun fieldRequired(fieldName: String): String = "$fieldName مطلوب!"

    override fun fieldNotValid(fieldName: String): String = "$fieldName غير صالح"
}