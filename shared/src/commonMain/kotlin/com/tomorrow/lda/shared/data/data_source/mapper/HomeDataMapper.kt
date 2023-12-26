package com.tomorrow.lda.shared.data.data_source.mapper

import com.tomorrow.lda.shared.data.data_source.model.HomeDataDTO
import com.tomorrow.lda.shared.data.data_source.utils.EntityMapper
import com.tomorrow.lda.shared.data.data_source.utils.Loadable
import com.tomorrow.lda.shared.domain.model.HomeData

class HomeDataMapper : EntityMapper<HomeData, HomeDataDTO> {
    override fun mapFromEntity(entity: HomeDataDTO): HomeData {
        val postMapper = PostMapper()
        val sessionMapper = SessionMapper()
        val speakerMapper = SpeakerDetailMapper()
        val adMapper = AdMapper()
        val posts = entity.posts
            ?.map { postMapper.mapFromEntity(it) }

        val highlightedPost = posts?.firstOrNull { post -> post.isHighlighted }

        return HomeData(
            highlightedPost = Loadable.smartInit(highlightedPost),
            todaySpeakers = Loadable.smartInit(entity.todaySpeakers?.map {
                speakerMapper.mapFromEntity(
                    it
                )
            }),
            upcomingSessions = Loadable.smartInit(entity.upcomingSessions?.map {
                sessionMapper.mapFromEntity(
                    it
                )
            }),
            posts = Loadable.smartInit(posts?.filter { it.id != highlightedPost?.id }),
            highlightedAds = Loadable.smartInit(entity.ads?.map{adMapper.mapFromEntity(it)}?.filter { it.isHighlighted }),
            ads = Loadable.smartInit(entity.ads?.map { adMapper.mapFromEntity(it) }?.filter { !it.isHighlighted }),
        )
    }

    override fun mapToEntity(domainModel: HomeData): HomeDataDTO {
        TODO("Not yet implemented")
    }
}