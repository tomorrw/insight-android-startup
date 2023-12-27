package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.AdDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Ad

class AdMapper : EntityMapper<Ad, AdDTO> {
    override fun mapFromEntity(entity: AdDTO) = Ad(
        id = entity.id,
        title = entity.title,
        subtitle = entity.subtitle,
        url = entity.url,
        image = entity.image,
        isHighlighted = entity.isHighlighted ?: false
    )

    override fun mapToEntity(domainModel: Ad): AdDTO {
        TODO("Not yet implemented")
    }
}