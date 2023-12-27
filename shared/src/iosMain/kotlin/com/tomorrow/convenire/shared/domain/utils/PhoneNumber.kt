package com.tomorrow.convenire.shared.domain.utils

import cocoapods.libPhoneNumber_iOS.NBEPhoneNumberFormatINTERNATIONAL
import cocoapods.libPhoneNumber_iOS.NBPhoneNumber
import cocoapods.libPhoneNumber_iOS.NBPhoneNumberUtil
import platform.Foundation.NSNumber

actual class PhoneNumber actual constructor(number: String?) {
    private val phoneUtil = NBPhoneNumberUtil()
    private var phoneNumber: NBPhoneNumber? = number?.toNBPhoneNumberOrNull()

    actual var number: String? = number
        get() = phoneNumber?.let {
            phoneUtil.format(it, NBEPhoneNumberFormatINTERNATIONAL, null)
        } ?: field
        set(value) {
            phoneNumber = value?.toNBPhoneNumberOrNull()
            field = value
        }

    actual fun isValid(): Boolean =
        number?.toNBPhoneNumberOrNull()?.let { phoneUtil.isValidNumber(it) } ?: false

    actual var region: Region?
        get() {
            val code = phoneNumber?.countryCode ?: return null
            val alpha2Code = phoneUtil.getRegionCodeForCountryCode(code) ?: return null
            return Region(code = code.intValue, alpha2Code = alpha2Code)
        }
        set(value) {
            if (value != null) phoneNumber?.countryCode = NSNumber(value.code)
        }

    private fun String.toNBPhoneNumberOrNull(): NBPhoneNumber? {
        return try {
            phoneUtil.parseAndKeepRawInput(this, "lb", null)
        } catch (e: Exception) {
            null
        }
    }

    actual fun getFormattedNumberInOriginalFormat(): String? =
        phoneNumber?.let {
            phoneUtil.formatInOriginalFormat(
                it,
                region?.alpha2Code ?: "lb",
                null
            )
        }
}