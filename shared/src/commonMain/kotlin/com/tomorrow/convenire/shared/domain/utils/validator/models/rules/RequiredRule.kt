package com.tomorrow.convenire.shared.domain.utils.validator.models.rules

import com.tomorrow.convenire.shared.domain.utils.validator.values.lang.ValidationStrings

object RequiredRule : Rule() {
    override fun isValid(value: Any?): Boolean {
        if (value == null) return false;

        if (value is String && value.isBlank()) return false

        return true
    }

    override fun getErrorMessage(language: ValidationStrings.Language): (fieldName: String) -> String =
        { ValidationStrings.getLocalizedStrings(language).fieldRequired(it) }
}