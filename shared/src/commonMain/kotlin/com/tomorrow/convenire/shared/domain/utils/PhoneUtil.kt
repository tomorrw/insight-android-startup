package com.tomorrow.convenire.shared.domain.utils

expect class PhoneUtil {
    companion object {
        val supportedRegions: List<Region>
        fun getExamplePhoneNumberFor(region: Region): PhoneNumber?
    }
}