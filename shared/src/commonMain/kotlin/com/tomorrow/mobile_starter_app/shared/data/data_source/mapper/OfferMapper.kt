package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.OfferDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.mobile_starter_app.shared.domain.model.Offer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class OfferMapper: EntityMapper<Offer, OfferDTO> {
    override fun mapFromEntity(entity: OfferDTO) = Offer(
        id = entity.id,
        postId = entity.postId,
        title = entity.title,
        image = entity.image?: entity.company.image?: "",
        publishedAt = entity.publishedAt?.toInstant()?.toLocalDateTime(TimeZone.currentSystemDefault()),
        company = CompanyMapper().mapFromEntity(entity.company),
    )

    override fun mapToEntity(domainModel: Offer): OfferDTO {
        TODO("Not yet implemented")
    }
}