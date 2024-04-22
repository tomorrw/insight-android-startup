package com.tomorrow.convenire.shared.domain.model

class ProgressReport (
    val league: League,
    val totalAttendedDuration: Duration,
    val nextLeagueName: String?,
    val attendedSessions: List<Session>,
)