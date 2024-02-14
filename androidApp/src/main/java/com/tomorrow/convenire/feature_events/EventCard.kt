package com.tomorrow.convenire.feature_events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tomorrow.convenire.common.cards.EventSpeaker
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.tomorrow.convenire.common.cards.EventCard as Card

private val formatter = DateTimeFormatter.ofPattern("HH:mm")
private val dayFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")

@Composable
fun EventCard(modifier: Modifier = Modifier, event: Event?) {
    val navController = LocalNavController.current

    event?.let {
        Card(
            modifier = Modifier,
            eventName = event.title,
            topic = event.topic,
            startTime = event.startDate.format(formatter),
            endTime = event.endDate.format(formatter),
            day = event.startDate.format(dayFormatter),
            isHighlighted = LocalDateTime.now().let { it > event.startDate && it < event.endDate },
            location = event.location,
            speakers = event.speakers.map { speaker ->
                EventSpeaker(speaker.fullName.getFormattedName(), rememberAsyncImagePainter(model = speaker.nationality?.url)
                )
            },
            tag = if (event.isNow()) "NOW" else if (event.hasAttended) "ATTENDED" else null,
            onClick = { navController.navigate(AppRoute.EventDetail.generateExplicit(event.id)) },
            rightIcon = { BookmarkEventButton(id = event.id) },
            color = event.color ?: MaterialTheme.colorScheme.background,
        )
    } ?: Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFFD3E2F0))
    )
}

