package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.ProgressReportDTO
import com.tomorrow.convenire.shared.domain.model.ProgressReport
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.domain.model.Duration

class ProgressReportMapper : EntityMapper<ProgressReport, ProgressReportDTO> {
    override fun mapFromEntity(entity: ProgressReportDTO) = ProgressReport(
        league = LeagueMapper().mapFromEntity(entity.league),
        totalAttendedDuration = Duration((entity.totalAttendedMinutes * 60).toInt()),
        nextLeagueName = null,
        attendedSessions = entity.attendedSessions.map { SessionMapper().mapFromEntity(it) }
    )

    override fun mapToEntity(domainModel: ProgressReport): ProgressReportDTO {
        TODO("Not yet implemented")
    }
}