package com.tomorrow.convenire.shared.domain.utils

import com.google.i18n.phonenumbers.PhoneNumberUtil

actual class PhoneUtil {
    actual companion object {
        private val phoneUtil = PhoneNumberUtil.getInstance()

        actual val supportedRegions: List<Region> = phoneUtil.supportedRegions
            .map { Region(phoneUtil.getCountryCodeForRegion(it), it) }
            .distinct()


        actual fun getExamplePhoneNumberFor(region: Region): PhoneNumber? {
            val phone = phoneUtil.getExampleNumber(region.alpha2Code) ?: return null
            val phoneNumberAsString =
                phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            return PhoneNumber(phoneNumberAsString)
        }
    }
}