package com.tomorrow.lda.common.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.R

@Composable
fun BellButton(
    modifier: Modifier = Modifier,
    isFilled: Boolean = false,
    onClick: () -> Unit = {},
    tint: Color = Color.White
) {
    val icon = painterResource(
        if (isFilled) R.drawable.baseline_notifications_inactive_24
        else R.drawable.ic_expand_to_full_screen_icon
    )

    Icon(
        modifier = modifier
            .clickable { onClick() }
            .size(26.dp),
        painter = icon,
        contentDescription = "Toggle FullScreen Icon",
        tint = tint
    )
}
