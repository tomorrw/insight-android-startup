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
fun FullScreenButton(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false,
    onClick: (Boolean) -> Unit = {},
    tint: Color = Color.White
) {
    val icon = painterResource(
        if (isFullScreen) R.drawable.ic_collapse_full_screen_icon
        else R.drawable.ic_expand_to_full_screen_icon
    )

    Icon(
        modifier = modifier
            .clickable { onClick(!isFullScreen) }
            .size(26.dp),
        painter = icon,
        contentDescription = "Toggle FullScreen Icon",
        tint = tint
    )
}
