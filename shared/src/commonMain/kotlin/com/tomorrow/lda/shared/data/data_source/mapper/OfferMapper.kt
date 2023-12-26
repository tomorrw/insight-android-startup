package com.tomorrow.lda.shared.data.data_source.mapper

import com.tomorrow.lda.shared.data.data_source.model.OfferDTO
import com.tomorrow.lda.shared.data.data_source.utils.EntityMapper
import com.tomorrow.lda.shared.domain.model.Offer
import kotlinx.datetime.LocalDateTime
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