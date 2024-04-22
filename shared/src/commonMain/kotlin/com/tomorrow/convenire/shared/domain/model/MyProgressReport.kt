package com.tomorrow.convenire.shared.domain.model

import com.tomorrow.convenire.shared.domain.utils.PhoneNumber

class ProgressReport (
    val league: League,
    val totalAttendedDuration: Duration,
    val nextLeagueName: String?,
    val attendedSessions: List<Session>,
)