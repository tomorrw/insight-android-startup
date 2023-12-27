package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.CompanyDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.data.data_source.utils.Loadable
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.model.Page
import com.tomorrow.convenire.shared.domain.utils.ImageShape
import com.tomorrow.convenire.shared.domain.utils.ThumbnailUrlHelper

class CompanyMapper : EntityMapper<Company, CompanyDTO> {
    override fun mapFromEntity(entity: CompanyDTO) = Company(
        id = entity.id,
        title = entity.title,
        image = entity.image?.let { ThumbnailUrlHelper().setImageShapeInUrl(ImageShape.Square, it) },
        boothDescription = entity.boothDescription,
        objectsClause = entity.objectsClause,
        categories = entity.productCategories.map {
            Company.Category(id = it.id, name = it.name, description = it.description)
        },
        detailPages = Loadable.smartInit(
            PageMapper().let { mapper ->

                val detailPages =
                    entity.detailPages.map { mapper.mapFromEntity(it) }

                val allPages: MutableList<Page> = detailPages.mapNotNull {
                    if (it.title.contains("deleted")) null else it
                }.toMutableList()

                val sessionMapper = SessionMapper()
                val dynamicThirdPage: MutableList<Page.Section> = mutableListOf()

                entity.events.map { sessionMapper.mapFromEntity(it) }.let {
                    if (it.isNotEmpty()) dynamicThirdPage.add(
                        Page.Section.EventList("Lectures", it)
                    )
                }
                if(dynamicThirdPage.isNotEmpty())
                    allPages.add(Page("Lectures",dynamicThirdPage))
                allPages
            }),
        socialLinks = entity.socialLinks.map { SocialLinkMapper().mapFromEntity(it) },
        floorMapGroup = entity.floorMapGroup?.let { Company.FloorMapGroup(it.name, it.image, it.floorMap.image) }
    )

    override fun mapToEntity(domainModel: Company): CompanyDTO {
        TODO("Not yet implemented")
    }
}