package com.tomorrow.mobile_starter_app.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SpinnerDTO (
    val id: String,
    @SerialName("quick_description")
    val description: String,
    @SerialName("created_at")
    val publishedAt: String,
    val company: CompanyDTO,
    )