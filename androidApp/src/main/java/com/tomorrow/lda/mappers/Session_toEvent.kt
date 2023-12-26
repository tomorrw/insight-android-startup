package com.tomorrow.lda.mappers

import androidx.compose.ui.graphics.Color
import com.tomorrow.lda.feature_events.Event
import com.tomorrow.lda.shared.domain.model.Session
import kotlinx.datetime.toJavaLocalDateTime

fun Session.toEvent(color: Color = Color.Transparent) = Event(
    id = this.id,
    startDate = this.startTime.toJavaLocalDateTime(),
    endDate = this.endTime.toJavaLocalDateTime(),
    title = this.title,
    topic = null,
    speakers = this.speakers,
    location = this.location,
    hasAttended = this.hasAttended,
    color = color,
    isNow = this.isSessionHappeningNow()
)