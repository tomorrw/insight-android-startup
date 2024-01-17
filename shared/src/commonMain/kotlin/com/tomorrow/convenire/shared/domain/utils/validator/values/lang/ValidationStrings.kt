package com.tomorrow.convenire.shared.domain.utils.validator.values.lang

sealed class ValidationStrings {
    abstract val languageCode: String
    abstract fun fieldRequired(fieldName: String): String
    abstract fun fieldNotValid(fieldName: String): String

    enum class Language(code: String) {
        English("en"), Arabic("ar")
    }

    companion object {
        // ValidationStrings::class.sealedSubclasses can be used when it is supported in kotlin multiplatform
        fun getLocalizedStrings(language: Language): ValidationStrings = when (language) {
            Language.English -> EnglishLocalizedStrings
            Language.Arabic -> ArabicLocalizedStrings
        }
    }

}