package com.tomorrow.convenire.shared.domain.utils

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber as PNumber

actual class PhoneNumber actual constructor(number: String?) {
    private val phoneUtil = PhoneNumberUtil.getInstance()
    private var phoneNumber: PNumber? = number?.toPNumberOrNull()

    actual var number: String? = number
        get() = phoneNumber?.let {
            phoneUtil.format(it, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        } ?: field
        set(value) {
            phoneNumber = value?.toPNumberOrNull()
            field = value
        }

    actual fun isValid(): Boolean =
        number?.toPNumberOrNull()?.let { phoneUtil.isValidNumber(it) } ?: false

    actual var region: Region?
        get() {
            val code = phoneNumber?.countryCode ?: return null
            return Region(
                code = code,
                alpha2Code = phoneUtil.getRegionCodeForCountryCode(code)
            )
        }
        set(value) {
            if (value != null) phoneNumber?.countryCode = value.code
        }

    private fun String.toPNumberOrNull(): PNumber? {
        return try {
            phoneUtil.parseAndKeepRawInput(this, "LB")
        } catch (e: Exception) {
            null
        }
    }

    actual fun getFormattedNumberInOriginalFormat(): String? =
        phoneNumber?.let {
            phoneUtil.formatInOriginalFormat(
                phoneNumber,
                region?.alpha2Code ?: "LB"
            )
        }
}