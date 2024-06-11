package com.tomorrow.mobile_starter_app.packageImplementation.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tomorrow.mobile_starter_app.shared.domain.model.Speaker
import com.tomorrow.eventlisting.presentationModel.EventCardModel
import java.time.LocalDateTime

data class Event(
    override val id: String = "123",
    override val startDate: LocalDateTime,
    override val endDate: LocalDateTime,
    override val title: String,
    override val topic: String?,
    val speakers: List<Speaker> = listOf(),
    override val location: String,
    override val color: Color? = null,
    override val getTag: @Composable () -> Tag?,
    override val onClick: (id: String) -> Unit = {},
    val isMinutesDisplayed: Boolean = false,
    val minutesAttended: Int? = null,
    val hasAttended: Boolean = false,
): EventCardModel(
    id = id,
    startDate = startDate,
    endDate = endDate,
    title = title,
    topic = topic,
    location = location,
    color = color,
    getTag = getTag,
    onClick = onClick
)

