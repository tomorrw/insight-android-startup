package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.SpeakerDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Speaker
import com.tomorrow.convenire.shared.domain.model.FullName
import com.tomorrow.convenire.shared.domain.model.Salutation
import com.tomorrow.convenire.shared.domain.utils.ImageShape
import com.tomorrow.convenire.shared.domain.utils.ThumbnailUrlHelper

class SpeakerMapper : EntityMapper<Speaker, SpeakerDTO> {
    override fun mapFromEntity(entity: SpeakerDTO) = Speaker(
        id = entity.id,
        title = entity.title,
        image = entity.image?.let { ThumbnailUrlHelper().setImageShapeInUrl(ImageShape.Square, it) },
        fullName = FullName(
            salutation = SalutationMapper().mapFromEntityIfNotNull(entity.salutation) ?: Salutation.None,
            firstName = entity.firstName,
            lastName = entity.lastName,
        ),
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