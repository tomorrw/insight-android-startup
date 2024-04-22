package com.tomorrow.convenire.shared.data.data_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProgressReportDTO(
    val league: LeagueDTO,
    @SerialName("total_attended_minutes")
    val totalAttendedMinutes: Float,
    @SerialName("attended_sessions")
    val attendedSessions: List<SessionDTO>,
)