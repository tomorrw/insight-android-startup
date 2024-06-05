package com.tomorrow.convenire.mappers

import androidx.compose.ui.graphics.Color
import com.tomorrow.convenire.common.Section
import com.tomorrow.convenire.shared.domain.model.Page

fun Page.toPageUi(eventColor: Color? = null) =
    com.tomorrow.convenire.common.Page(this.title, this.sections.map { section ->
        when (section) {
            is Page.Section.EventList -> Section.EventList(
                title = section.title,
                events = section.events.map { session ->
                    session.toEvent().copy(color = eventColor)
                },
                shouldDisplayTitle = false
            )

            is Page.Section.InfoSection -> Section.InfoSection(
                title = section.title,
                description = section.description,
                image = section.image,
            )

            is Page.Section.Speakers -> Section.Speakers(
                title = section.title,
                speakers = section.speakers,
                shouldDisplayTitle = section.shouldDisplayTitle
            )

            is Page.Section.VideoSection -> Section.VideoSection(
                title = section.title,
                videoUrl = section.videoUrl,
                description = section.description
            )
        }
    })