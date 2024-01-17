package com.tomorrow.convenire.shared.domain.utils

import cocoapods.libPhoneNumber_iOS.NBEPhoneNumberFormatINTERNATIONAL
import cocoapods.libPhoneNumber_iOS.NBPhoneNumberUtil
import kotlinx.cinterop.ExperimentalForeignApi

actual class PhoneUtil {
    actual companion object {
        private val phoneUtil = NBPhoneNumberUtil()

        actual val supportedRegions: List<Region> =
            (phoneUtil.getSupportedRegions() ?: listOf<String>())
                .filterIsInstance<String>()
                .mapNotNull {
                    if (it == "ZZ" || it.length != 2) null
                    else phoneUtil.getCountryCodeForRegion(it)
                        ?.let { code -> Region(code.intValue, it) }
                }

        @OptIn(ExperimentalForeignApi::class)
        actual fun getExamplePhoneNumberFor(region: Region): PhoneNumber? {
            val phone = phoneUtil.getExampleNumber(region.alpha2Code, null) ?: return null
            val phoneNumberAsString =
                phoneUtil.format(phone, NBEPhoneNumberFormatINTERNATIONAL, null)
            return PhoneNumber(phoneNumberAsString)
        }
    }
}