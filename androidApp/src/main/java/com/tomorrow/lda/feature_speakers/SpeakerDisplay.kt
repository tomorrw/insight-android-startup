package com.tomorrow.lda.feature_speakers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.lda.R
import com.tomorrow.lda.common.FeedSectionItem
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.shared.domain.model.Speaker


@Composable
fun SpeakerDisplay(speaker: Speaker) {
    val navController = LocalNavController.current
    val interactionSource = remember { MutableInteractionSource() }
    FeedSectionItem(
        modifier = Modifier
            .clickable(
                interactionSource,
                null
            ) { navController.navigate(AppRoute.Speaker.generateExplicit(speaker.id)) }
            .height(100.dp)
            .width(84.dp),
        albumArt = {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.background),
                    model = speaker.image ?: "",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_avatar_placeholder)
                )
                AsyncImage(
                    speaker.nationality?.url,
                    contentDescription = "country",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .align(Alignment.BottomStart)
                        .background(MaterialTheme.colorScheme.background)
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(0.4f), CircleShape),
                )
            }
        },
        title = {
            Text(
                speaker.getFullName(),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                modifier = Modifier.padding(top = 6.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    )
}