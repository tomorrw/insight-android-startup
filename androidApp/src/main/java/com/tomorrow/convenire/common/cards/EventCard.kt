package com.tomorrow.convenire.common.cards


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.TagText

data class EventSpeaker(
    val speakerName: String,
    val speakerImage: Painter,
)

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    eventName: String,
    topic: String?,
    startTime: String,
    endTime: String,
    isHighlighted: Boolean,
    location: String,
    day: String? = null,
    speakers: List<EventSpeaker>,
    onClick: () -> Unit,
    tag: String? = null,
    color: Color = Color.Transparent,
    rightIcon: @Composable () -> Unit = { Spacer(modifier = Modifier) }
) = Box(modifier = modifier
    .clip(RoundedCornerShape(8.dp))
    .background(color = if (color == Color.Transparent) MaterialTheme.colorScheme.background else color)
    .clickable { onClick() }
    .padding(all = 16.dp)
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Column(
            modifier = Modifier.weight(0.9f)
        ) {
            Text(
                text = "$startTime - $endTime",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = eventName,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
            )

            topic?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.secondary)
                )
            }

            Spacer(modifier = Modifier.size(18.dp))

            day?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.DateRange,
                        contentDescription = "date",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = "location",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }


            if (speakers.isNotEmpty()) {

                Column {
                    Spacer(Modifier.height(8.dp))
                    speakers.map {
                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = it.speakerImage,
                                contentDescription = "speaker image",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(18.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                            )
                            Text(
                                text = it.speakerName,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(2.dp))
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            rightIcon()
        }

    }
    Column(
        modifier = Modifier.align(Alignment.BottomEnd)
    ) {
        if (tag != null) TagText(text = tag) else Spacer(Modifier)
    }
}
