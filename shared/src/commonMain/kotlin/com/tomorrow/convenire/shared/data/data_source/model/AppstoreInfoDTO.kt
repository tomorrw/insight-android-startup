package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class AppstoreInfoDTO (
    @SerialName("trackName")
    val appName: String,
    @SerialName("trackViewUrl")
    val storeUrl: String,
)

@Serializable
data class AppStoreResult(
    val results:  List<AppstoreInfoDTO>
)