package com.tomorrow.convenire.shared.domain.utils.validator.models.rules

import com.tomorrow.convenire.shared.domain.model.Email
import com.tomorrow.convenire.shared.domain.utils.validator.values.lang.ValidationStrings

class EmailRule : TransformativeRule<Email>() {
    override fun getInstanceIfValid(value: Any?): Email? {
        if (value is Email) return value
        if (value is String && isValid(value)) return Email(value)
        return null
    }

    override fun isValid(value: Any?): Boolean = try {
        if (value is String) {
            Email(value)
            true
        } else false
    } catch (e: Exception) {
        false
    }

    override fun getErrorMessage(language: ValidationStrings.Language): (fieldName: String) -> String =
        { ValidationStrings.getLocalizedStrings(language).fieldNotValid(it) }
}
