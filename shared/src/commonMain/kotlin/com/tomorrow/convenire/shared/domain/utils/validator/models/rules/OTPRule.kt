package com.tomorrow.convenire.shared.domain.utils.validator.models.rules

import com.tomorrow.convenire.shared.domain.model.OTP
import com.tomorrow.convenire.shared.domain.utils.validator.values.lang.ValidationStrings

class OTPRule : TransformativeRule<OTP>() {
    override fun getInstanceIfValid(value: Any?): OTP? {
        if (value is OTP) return value
        if (value is String && isValid(value)) return OTP.fromString(value)
        return null
    }

    override fun isValid(value: Any?): Boolean = try {
        if (value is String) {
            OTP.fromString(value)
            true
        } else false
    } catch (e: Exception) {
        false
    }

    override fun getErrorMessage(language: ValidationStrings.Language): (fieldName: String) -> String =
        { ValidationStrings.getLocalizedStrings(language).fieldNotValid(it) }
}
