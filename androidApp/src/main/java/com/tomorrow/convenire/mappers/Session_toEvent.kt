package com.tomorrow.convenire.mappers

import androidx.compose.ui.graphics.Color
import com.tomorrow.convenire.feature_events.Event
import com.tomorrow.convenire.shared.domain.model.Session
import kotlinx.datetime.toJavaLocalDateTime

fun Session.toEvent() = Event(
    id = this.id,
    startDate = this.startTime.toJavaLocalDateTime(),
    endDate = this.endTime.toJavaLocalDateTime(),
    title = this.title,
    topic = null,
    speakers = this.speakers,
    location = this.location,
    getTag = {
        this.getTag()?.let { tag ->
            Event.Tag(
                tag.text,
                Color(android.graphics.Color.parseColor(tag.color)),
                Color(android.graphics.Color.parseColor(tag.background))
            )
        }
    },
    minutesAttended = this.minutesAttended,
)