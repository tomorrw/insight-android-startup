package com.tomorrow.lda.common.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import com.tomorrow.lda.R


@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    tint: Color? = null,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    IconButton(
        onClick = onClick,
        modifier = modifier
            .clickable { onClick() }
            .clip(CircleShape) // Adding circular clipping
            .graphicsLayer { if (isRtl) rotationZ = 180f }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left_icon),
            contentDescription = "Back Button",
            tint = tint ?: MaterialTheme.colorScheme.primary,
        )
    }
}