package com.tomorrow.lda.shared.domain.utils.validator.models.rules

import com.tomorrow.lda.shared.domain.utils.validator.values.lang.ValidationStrings

abstract class Rule {
    abstract fun isValid(value: Any?): Boolean

    fun isNotValid(value: Any?): Boolean = !isValid(value)

    abstract fun getErrorMessage(language: ValidationStrings.Language): (fieldName: String) -> String

    fun getErrorMessageIfNotValid(
        value: Any?,
        language: ValidationStrings.Language
    ): ((fieldName: String) -> String)? = if (isNotValid(value)) getErrorMessage(language) else null
}

