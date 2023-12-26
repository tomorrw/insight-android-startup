package com.tomorrow.lda.feature_video

import android.media.session.PlaybackState.STATE_PAUSED
import android.media.session.PlaybackState.STATE_PLAYING
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.common.PlayPauseButton
import com.tomorrow.lda.common.VideoProgressBar
import com.tomorrow.lda.common.buttons.FullScreenButton
import com.tomorrow.lda.common.buttons.Skip30Button
import com.tomorrow.lda.common.buttons.SkipDurationButtonState

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    onPauseToggle: () -> Unit,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferedPercentage: () -> Int,
    playbackState: () -> Int,
    fullScreenState: () -> Boolean,
    onFullScreenClicked: () -> Unit,
    onSeekChanged: (timeMs: Float) -> Unit
) {
    val visible = remember(isVisible()) { isVisible() }
    val playerState = remember(playbackState()) { playbackState() }
    val duration = remember(totalDuration()) { totalDuration() }
    val videoTime = remember(currentTime()) { currentTime() }
    val buffer = remember(bufferedPercentage()) { bufferedPercentage() }
    val isFullScreen = remember(fullScreenState()) { fullScreenState() }
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }

    Box(modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.5f))
                    .padding(if (isFullScreen) 16.dp else 0.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                VideoProgressBar(
                    progress = videoTime.toFloat(),
                    bufferProgress = buffer.toFloat(),
                    maxValue = duration.toFloat(),
                    onSeek = onSeekChanged,
                    setIsSeeking = {},
                    modifier = Modifier.align(Alignment.BottomStart),
                    rightIcons = {
                        FullScreenButton(
                            isFullScreen = isFullScreen,
                            onClick = { onFullScreenClicked() })
                    }
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = (playerState != STATE_PLAYING && playerState != STATE_PAUSED) || visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Skip30Button(state = SkipDurationButtonState.Backward) {
                    onSeekChanged(videoTime.toFloat() + 30000)
                }

                PlayPauseButton(
                    onToggle = { onPauseToggle() },
                    isPlaying = isVideoPlaying,
                    isBuffering = playerState != STATE_PLAYING && playerState != STATE_PAUSED
                )

                Skip30Button(state = SkipDurationButtonState.Forward) {
                    onSeekChanged(videoTime.toFloat() + 30000)
                }
            }
        }
    }
}