package com.tomorrow.mobile_starter_app.common

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CustomToast(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    message: String,
    isShown: MutableState<Boolean>,
    duration: Int = Toast.LENGTH_SHORT,
    contentAlignment: Alignment = Alignment.TopCenter,
    foregroundColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    if (isShown.value) {
        LaunchedEffect(Unit) {
            delay(duration.toLong())
            isShown.value = false
        }

        return Box(modifier = modifier.fillMaxSize(), contentAlignment = contentAlignment) {
            Row(
                modifier = Modifier
                    .sizeIn(minWidth = 50.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(backgroundColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = "",
                        Modifier.size(34.dp),
                        tint = iconColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(message, color = foregroundColor)
            }
        }
    }
}