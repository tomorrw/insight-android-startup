package com.tomorrow.convenire.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.convenire.feature_events.Event
import com.tomorrow.convenire.feature_events.EventCard
import com.tomorrow.convenire.feature_speakers.SpeakerDisplay
import com.tomorrow.convenire.feature_video.VideoPlayer
import com.tomorrow.convenire.shared.domain.model.Speaker

@Composable
fun SectionDisplay(
    modifier: Modifier = Modifier,
    section: Section,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    onShowAll: (() -> Unit)? = null
) = when (section) {
    is Section.EventList -> {
        Column(modifier.padding(contentPadding)) {
            if (section.shouldDisplayTitle) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        section.title,
                        modifier = Modifier.weight(weight = 1f, fill = false),
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis
                    )

                    onShowAll?.let {
                        val interactionSource = remember { MutableInteractionSource() }
                        Text(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    null
                                ) { it() }
                                .padding(vertical = 8.dp),
                            text = "Show All",
                            style = MaterialTheme.typography.titleSmall.copy(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }
                Spacer(Modifier.size(16.dp))
            }
            section.events.forEach {
                EventCard(event = it)
                Spacer(Modifier.size(16.dp))
            }
        }
    }

    is Section.InfoSection -> {
        Column(modifier.padding(contentPadding)) {
            section.image?.let {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(16f / 9f)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    model = it, contentDescription = ""
                )
                Spacer(Modifier.size(16.dp))
            }
            Text(
                section.title,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.size(16.dp))
            Text(
                section.description,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }

    is Section.Speakers -> {
        Column(modifier) {
            if (section.shouldDisplayTitle) Column {
                Row(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        section.title,
                        modifier = Modifier.weight(weight = 1f, fill = false),
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis
                    )

                    onShowAll?.let {
                        val interactionSource = remember { MutableInteractionSource() }
                        Text(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    null
                                ) { it() },
                            text = "Show All",
                            style = MaterialTheme.typography.titleSmall.copy(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }
                Spacer(Modifier.size(16.dp))
            }

            LazyRow(
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) { items(section.speakers) { SpeakerDisplay(it) } }
        }
    }

    is Section.VideoSection -> {
        Column(modifier.padding(contentPadding)) {
            VideoPlayer(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(16f / 9f)
                    .fillMaxSize(),
                videoUrl = section.videoUrl
            )

            Spacer(Modifier.size(16.dp))

            Text(
                section.description,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}


sealed class Section {
    abstract val title: String

    class InfoSection(
        override val title: String,
        val description: String,
        val image: String? = null
    ) : Section()

    class VideoSection(
        override val title: String,
        val videoUrl: String,
        val description: String
    ) :
        Section()

    class EventList(
        override val title: String,
        val events: List<Event>,
        val shouldDisplayTitle: Boolean = true
    ) : Section()

    class Speakers(
        override val title: String,
        val speakers: List<Speaker>,
        val shouldDisplayTitle: Boolean = true
    ) : Section()
}