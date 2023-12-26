package com.tomorrow.lda.shared.data.data_source.mapper

import com.tomorrow.lda.shared.data.data_source.model.SpeakerDTO
import com.tomorrow.lda.shared.data.data_source.utils.EntityMapper
import com.tomorrow.lda.shared.domain.model.Speaker
import com.tomorrow.lda.shared.domain.utils.ImageShape
import com.tomorrow.lda.shared.domain.utils.ThumbnailUrlHelper

class SpeakerMapper : EntityMapper<Speaker, SpeakerDTO> {
    override fun mapFromEntity(entity: SpeakerDTO) = Speaker(
        id = entity.id,
        title = entity.title,
        image = entity.image?.let { ThumbnailUrlHelper().setImageShapeInUrl(ImageShape.Square, it) },
        firstName = entity.firstName,
        lastName = entity.lastName,
        socialLinks = entity.socialLinks.map { SocialLinkMapper().mapFromEntity(it) },
        nationality = entity.country?.let { country ->
            Speaker.Country(
                country.code,
                country.name,
                country.url ?: "")
        }
    )

    override fun mapToEntity(domainModel: Speaker): SpeakerDTO {
        TODO("Not yet implemented")
    }
}