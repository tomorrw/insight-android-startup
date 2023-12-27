package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppPlatformDTO {
    @SerialName("android")
    Android,

    @SerialName("ios")
    IOS,
}