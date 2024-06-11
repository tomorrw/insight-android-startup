package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.SpeakerDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.mobile_starter_app.shared.domain.model.Speaker
import com.tomorrow.kmmProjectStartup.domain.model.FullName
import com.tomorrow.kmmProjectStartup.domain.model.Salutation
import com.tomorrow.kmmProjectStartup.domain.utils.ImageShape
import com.tomorrow.kmmProjectStartup.domain.utils.ThumbnailUrlHelper

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