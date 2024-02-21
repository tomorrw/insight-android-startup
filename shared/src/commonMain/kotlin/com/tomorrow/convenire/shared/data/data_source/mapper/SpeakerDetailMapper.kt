package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.SpeakerDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.domain.model.FullName
import com.tomorrow.convenire.shared.domain.model.Page
import com.tomorrow.convenire.shared.domain.model.Salutation
import com.tomorrow.convenire.shared.domain.model.SocialLink
import com.tomorrow.convenire.shared.domain.model.Speaker
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import com.tomorrow.convenire.shared.domain.utils.ImageShape
import com.tomorrow.convenire.shared.domain.utils.ThumbnailUrlHelper

class SpeakerDetailMapper : EntityMapper<SpeakerDetail, SpeakerDTO> {

    override fun mapFromEntity(entity: SpeakerDTO) = SpeakerDetail(
        id = entity.id,
        title = entity.title,
        image = entity.image?.let { ThumbnailUrlHelper().setImageShapeInUrl(ImageShape.Square, it) },
        detailPages = Loadable.smartInit(
            PageMapper().let { mapper ->
                val detailPages =
                    entity.detailPages?.map { mapper.mapFromEntity(it) }

                val allPages: MutableList<Page> = detailPages?.mapNotNull {
                    if (it.title.contains("deleted")) null else it
                }?.toMutableList() ?: mutableListOf()

                val sessionMapper = SessionMapper()
                val dynamicThirdPage: MutableList<Page.Section> = mutableListOf()

                entity.events?.map { sessionMapper.mapFromEntity(it) }?.let {
                    if (it.isNotEmpty()) dynamicThirdPage.add(
                        Page.Section.EventList("Lectures", it)
                    )
                }
                if(dynamicThirdPage.isNotEmpty())
                    allPages.add(Page("Lectures",dynamicThirdPage))
                allPages
            }),
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
                country.url ?: ""
            )
        }
    )

    override fun mapToEntity(domainModel: SpeakerDetail): SpeakerDTO {
        TODO("Not yet implemented")
    }
}