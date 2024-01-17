package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.DetailPageDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Page

class PageMapper : EntityMapper<Page, DetailPageDTO> {
    override fun mapFromEntity(entity: DetailPageDTO) = Page(
        title = entity.title,
        sections = entity.sections.map {
            if (it.video != null) Page.Section.VideoSection(
                title = it.title,
                videoUrl = it.video,
                description = it.body
            )
            else Page.Section.InfoSection(
                title = it.title,
                description = it.body,
                image = it.image
            )
        }
    )


    override fun mapToEntity(domainModel: Page): DetailPageDTO {
        TODO("Not yet implemented")
    }
}