package com.tomorrow.lda.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.shared.domain.model.Duration

@Composable
fun VideoProgressBar(
    progress: Float,
    bufferProgress: Float,
    maxValue: Float,
    onSeek: (sliderPosition: Float) -> Unit,
    modifier: Modifier = Modifier,
    setIsSeeking: (Boolean) -> Unit,
    rightIcons: @Composable () -> Unit,
) {
    val tempSliderPosition = remember { mutableStateOf(progress) }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged = interactionSource.collectIsDraggedAsState()
    val valueSliderPosition =
        (if (isDragged.value) tempSliderPosition.value else progress).coerceIn(0f, maxValue)

    Column(modifier) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Slider(
                modifier = Modifier.height(30.dp),
                value = bufferProgress,
                enabled = false,
                onValueChange = { /*do nothing*/ },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    disabledThumbColor = Color.Transparent,
                    inactiveTrackColor = Color.Gray.copy(0.2f)
                ),
            )

            Slider(
                modifier = Modifier.height(30.dp),
                value = valueSliderPosition,
                onValueChange = { progress ->
                    tempSliderPosition.value = progress
                    setIsSeeking(true)
                },
                onValueChangeFinished = {
                    onSeek(tempSliderPosition.value)
                    setIsSeeking(false)
                },
                colors = SliderDefaults.colors(
                    inactiveTrackColor = Color.Gray.copy(0.2f)
                ),
                valueRange = 0f..maxValue,
                interactionSource = interactionSource,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = if (valueSliderPosition == 0f && maxValue == 0f) "Loading ..."
                else "${
                    Duration(valueSliderPosition.toLong()).toVideoFormatString()
                } • ${
                    Duration(maxValue.toLong()).toVideoFormatString()
                }",
            )

            rightIcons()
        }
    }
}

@Composable
fun VideoProgressBar(
    modifier: Modifier = Modifier,
    completedColor: Color = MaterialTheme.colorScheme.primary,
    bufferedColor: Color = Color(0X61FFFFFF),
    inactiveTrackColor: Color = Color.Gray.copy(0.2f),
    bufferProgress: Float = 0f,
    playtimeProgress: Float = 0f,
    maxValue: Float = 0f,
    onValueChange: (sliderPosition: Float) -> Unit = {},
    setIsSeeking: (Boolean) -> Unit,
) {

    val tempSliderPosition = remember { mutableStateOf(playtimeProgress) }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged = interactionSource.collectIsDraggedAsState()
    val valueSliderPosition =
        (if (isDragged.value) tempSliderPosition.value else playtimeProgress).coerceIn(0f, maxValue)
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {

            val bufferedPosition = remember { derivedStateOf { bufferProgress / maxValue } }

            LinearProgressIndicator(
                progress = bufferedPosition.value,
                color = bufferedColor,
                trackColor = inactiveTrackColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )


            val playProgress = remember { derivedStateOf { valueSliderPosition / maxValue } }

            LinearProgressIndicator(
                progress = playProgress.value,
                color = completedColor,
                trackColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp),
                value = valueSliderPosition,
                onValueChange = { progress ->
                    tempSliderPosition.value = progress
                    setIsSeeking(true)
                },
                onValueChangeFinished = {
                    onValueChange(tempSliderPosition.value)
                    setIsSeeking(false)
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,

                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                ),
                valueRange = 0f..maxValue,
                interactionSource = interactionSource,
            )
        }
    }
}