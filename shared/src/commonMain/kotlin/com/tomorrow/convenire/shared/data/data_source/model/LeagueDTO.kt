package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LeagueDTO (
    val name: String?,
    @SerialName("lectures_attended")
    val lecturesAttended: Int,
    @SerialName("lectures_remaining")
    val lecturesRemaining: Int,
    @SerialName("percentage")
    val percentage: Float,
    val color: String
)