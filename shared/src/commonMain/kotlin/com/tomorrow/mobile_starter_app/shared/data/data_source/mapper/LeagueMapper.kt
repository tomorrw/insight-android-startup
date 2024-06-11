package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.LeagueDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.mobile_starter_app.shared.domain.model.League

class LeagueMapper: EntityMapper<League, LeagueDTO> {
    override fun mapFromEntity(entity: LeagueDTO) = League(
        name = entity.name ?: "",
        lecturesAttendedCount = entity.lecturesAttended,
        lecturesRemaining = entity.lecturesRemaining,
        percentage = entity.percentage,
        color = entity.color
    )

    override fun mapToEntity(domainModel: League): LeagueDTO {
        TODO("Not yet implemented")
    }

}