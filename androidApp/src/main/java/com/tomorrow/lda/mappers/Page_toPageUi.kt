package com.tomorrow.lda.mappers

import androidx.compose.ui.graphics.Color
import com.tomorrow.lda.common.Section
import com.tomorrow.lda.shared.domain.model.Page

fun Page.toPageUi() =
    com.tomorrow.lda.common.Page(this.title, this.sections.map { section ->
        when (section) {
            is Page.Section.EventList -> Section.EventList(
                title = section.title,
                events = section.events.map { session -> session.toEvent(Color(0xFFEEF5FC)) },
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