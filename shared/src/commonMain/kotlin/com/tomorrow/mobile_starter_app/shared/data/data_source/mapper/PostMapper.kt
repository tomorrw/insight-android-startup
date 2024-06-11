package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.PostDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.data.utils.Loadable
import com.tomorrow.mobile_starter_app.shared.domain.model.Post
import com.tomorrow.kmmProjectStartup.domain.utils.ImageShape
import com.tomorrow.kmmProjectStartup.domain.utils.ThumbnailUrlHelper
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class PostMapper : EntityMapper<Post, PostDTO> {
    override fun mapFromEntity(entity: PostDTO) = Post(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        image = entity.image?.let { ThumbnailUrlHelper().setImageShapeInUrl(ImageShape.Rectangle, it) },
        detailPage = Loadable.smartInit(PageMapper().mapFromEntityIfNotNull(entity.detailPage)),
        isHighlighted = entity.isHighlighted == 1,
        publishedAt = entity.publishedAt.toInstant()
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        company = CompanyMapper().mapFromEntityIfNotNull(entity.company),
        action = entity.actions?.map { ActionMapper().mapFromEntity(it) } ?: listOf()
    )

    override fun mapToEntity(domainModel: Post): PostDTO {
        TODO("Not yet implemented")
    }
}