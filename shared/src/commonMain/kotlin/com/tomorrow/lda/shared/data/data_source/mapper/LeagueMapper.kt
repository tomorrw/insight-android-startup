package com.tomorrow.lda.shared.data.data_source.mapper

import com.tomorrow.lda.shared.data.data_source.model.LeagueDTO
import com.tomorrow.lda.shared.data.data_source.utils.EntityMapper
import com.tomorrow.lda.shared.domain.model.League

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