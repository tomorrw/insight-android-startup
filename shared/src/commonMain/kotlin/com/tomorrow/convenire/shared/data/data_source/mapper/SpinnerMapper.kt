package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.SpinnerDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Spinner
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class SpinnerMapper : EntityMapper<Spinner, SpinnerDTO> {
    override fun mapFromEntity(entity: SpinnerDTO) = Spinner(
        id = entity.id,
        description = entity.description,
        publishedAt = entity.publishedAt.toInstant()
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        company = CompanyMapper().mapFromEntity(entity.company),
    )

    override fun mapToEntity(domainModel: Spinner): SpinnerDTO {
        TODO("Not yet implemented")
    }
}