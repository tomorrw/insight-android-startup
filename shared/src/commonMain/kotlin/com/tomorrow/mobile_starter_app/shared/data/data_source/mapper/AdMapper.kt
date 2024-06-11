package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.AdDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.mobile_starter_app.shared.domain.model.Ad

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