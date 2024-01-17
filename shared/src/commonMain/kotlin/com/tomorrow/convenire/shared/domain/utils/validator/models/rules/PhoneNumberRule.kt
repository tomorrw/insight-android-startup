package com.tomorrow.convenire.shared.domain.utils.validator.models.rules

import com.tomorrow.convenire.shared.domain.utils.PhoneNumber
import com.tomorrow.convenire.shared.domain.utils.validator.values.lang.ValidationStrings

class PhoneNumberRule : TransformativeRule<PhoneNumber>() {
    override fun getInstanceIfValid(value: Any?): PhoneNumber? {
        if (value is PhoneNumber && value.isValid()) return value
        if (value is String && isValid(value)) return PhoneNumber(value)
        return null
    }

    override fun isValid(value: Any?): Boolean = value is String && PhoneNumber(value).isValid()

    override fun getErrorMessage(language: ValidationStrings.Language): (fieldName: String) -> String =
        { ValidationStrings.getLocalizedStrings(language).fieldNotValid(it) }
}