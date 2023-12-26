package com.tomorrow.lda.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.R

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    onToggle: () -> Unit,
    isPlaying: Boolean,
    backgroundColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconSize: Dp = 32.dp,
    boxSize: Dp? = 40.dp,
    isBuffering: Boolean,
    opacityWhenBuffering: Float = 1f
) {
    val changingBackgroundColor =
        if (isBuffering) backgroundColor.copy(alpha = opacityWhenBuffering) else backgroundColor

    val roundedShapeModifier = if (boxSize != null) Modifier.size(boxSize) else Modifier

    Box(
        contentAlignment = Alignment.Center,
        modifier = roundedShapeModifier
            .clip(shape = CircleShape)
            .background(changingBackgroundColor, shape = CircleShape)
    ) {
        if (isBuffering) Loader(modifier = Modifier.size(32.dp)) else Icon(
            painter = painterResource(
                id = if (!isPlaying) R.drawable.ic_menu_play_icon
                else R.drawable.ic_state_pause__circle_false
            ),
            contentDescription = "Play / Pause Button",
            modifier = modifier
                .clickable { onToggle() }
                .size(iconSize),
            tint = tint
        )
    }
}