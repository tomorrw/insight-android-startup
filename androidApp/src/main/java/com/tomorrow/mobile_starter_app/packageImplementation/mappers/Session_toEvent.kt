package com.tomorrow.mobile_starter_app.packageImplementation.mappers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tomorrow.mobile_starter_app.packageImplementation.models.Event
import com.tomorrow.mobile_starter_app.shared.domain.model.Session
import com.tomorrow.eventlisting.presentationModel.EventCardModel
import kotlinx.datetime.toJavaLocalDateTime

fun Session.toEvent(onClick: (String) -> Unit = {}) = Event(
    id = this.id,
    startDate = this.startTime.toJavaLocalDateTime(),
    endDate = this.endTime.toJavaLocalDateTime(),
    title = this.title,
    topic = null,
    speakers = this.speakers,
    location = this.location,
    getTag = @Composable { this.getTag()?.let { EventCardModel.Tag(
        text = it.text,
        textStyle = MaterialTheme.typography.headlineSmall.copy(color = Color(android.graphics.Color.parseColor(it.color))),
        backgroundColor = Color(android.graphics.Color.parseColor(it.color))
    ) } },
    onClick = onClick
)

fun EventCardModel.toEvent() = Event(
    id = this.id,
    startDate = this.startDate,
    endDate = this.endDate,
    title = this.title,
    topic = this.topic,
    location = this.location,
    color = this.color,
    getTag = this.getTag,
    onClick = this.onClick
)