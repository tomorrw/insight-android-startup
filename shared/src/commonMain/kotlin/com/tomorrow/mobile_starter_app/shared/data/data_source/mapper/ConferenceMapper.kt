package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.ConferenceDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.data.utils.fromApiFormatToDate
import com.tomorrow.mobile_starter_app.shared.domain.model.Conference

class ConferenceMapper: EntityMapper<Conference, ConferenceDTO> {
    override fun mapFromEntity(entity: ConferenceDTO): Conference = Conference(
        id = entity.id ?: "",
        isActive = entity.isActive,
        name = entity.name,
        startDate = entity.startDate?.fromApiFormatToDate()?.date,
        endDate = entity.endDate?.fromApiFormatToDate()?.date
    )

    override fun mapToEntity(domainModel: Conference): ConferenceDTO {
        TODO("Not yet implemented")
    }


}