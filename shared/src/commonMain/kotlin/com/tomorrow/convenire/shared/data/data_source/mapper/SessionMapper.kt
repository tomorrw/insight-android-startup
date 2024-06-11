package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.SessionDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.data.utils.Loadable
import com.tomorrow.kmmProjectStartup.data.utils.Loaded
import com.tomorrow.kmmProjectStartup.data.utils.fromApiFormatToDate
import com.tomorrow.convenire.shared.domain.model.Page
import com.tomorrow.convenire.shared.domain.model.Session
import com.tomorrow.kmmProjectStartup.domain.utils.ImageShape
import com.tomorrow.kmmProjectStartup.domain.utils.ThumbnailUrlHelper

class SessionMapper : EntityMapper<Session, SessionDTO> {
    override fun mapFromEntity(entity: SessionDTO) = Session(
        id = entity.id,
        title = entity.title,
        image = entity.image?.let {
            ThumbnailUrlHelper().setImageShapeInUrl(
                ImageShape.Rectangle,
                it
            )
        } ?: "",
        topic = entity.topic,
        startTime = entity.start.fromApiFormatToDate(),
        endTime = entity.end.fromApiFormatToDate(),
        location = entity.location,
        companyId = entity.companyId,
        detailPage = Loadable.smartInit(PageMapper().let { mapper ->
            val speakerMapper = SpeakerMapper()

            val page = mapper.mapFromEntityIfNotNull(entity.detailPage) ?: Page(
                "",
                listOf()
            )

            return@let entity.speakers?.let {
                if (it.isEmpty()) page
                else page.copy(
                    sections = listOf(
                        Page.Section.Speakers(
                            title = "Speakers",
                            shouldDisplayTitle = true,
                            speakers = it.map { speaker -> speakerMapper.mapFromEntity(speaker) })
                    ) + page.sections
                )
            } ?: page
        }),
        hasAttended = entity.hasAttended == 1,
        minutesAttended = entity.minutesAttended,
        canAskQuestions = entity.canAskQuestions == 1,
        speakers = entity.speakers?.map { SpeakerMapper().mapFromEntity(it) } ?: listOf(),
        actions = entity.actions?.map { ActionMapper().mapFromEntity(it) } ?: listOf(),
        numberOfAttendees = Loadable.smartInit(entity.numberOfAttendees),
        numberOfSeats = Loadable.smartInit(entity.numberOfSeats),
    )

    override fun mapToEntity(domainModel: Session): SessionDTO {
        TODO("Not yet implemented")
    }
}