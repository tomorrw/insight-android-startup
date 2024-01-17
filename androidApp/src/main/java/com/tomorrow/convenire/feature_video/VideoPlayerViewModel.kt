package com.tomorrow.convenire.feature_video

import android.app.Application
import android.media.metrics.PlaybackStateEvent.STATE_NOT_STARTED
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.tomorrow.convenire.common.Timer

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {
    var state by mutableStateOf(VideoPlayerState())

    val exoPlayer: ExoPlayer = ExoPlayer.Builder(application.applicationContext).build()

    init {
        Timer({
            state = state.copy(
                currentTime = exoPlayer.currentPosition.coerceAtLeast(0L),
                bufferedPercentage = exoPlayer.bufferedPercentage
            )
        }).loop()
    }

    private val listener = object : Player.Listener {
        override fun onEvents(
            player: Player,
            events: Player.Events
        ) {
            super.onEvents(player, events)
            state = state.copy(
                isPlaying = player.isPlaying,
                playbackState = player.playbackState,
                totalDuration = player.duration.coerceAtLeast(0L),
            )
        }
    }

    fun on(event: VideoPlayerEvent) {
        when (event) {
            VideoPlayerEvent.OnFullScreenClicked -> state =
                state.copy(isFullScreen = state.isFullScreen.not())

            VideoPlayerEvent.OnPauseToggle -> if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
            is VideoPlayerEvent.OnSeekChanged -> exoPlayer.seekTo(event.value.toLong())
            is VideoPlayerEvent.OnNewVideo -> exoPlayer.apply {
                prepare()
                setMediaItem(MediaItem.fromUri(event.videoUrl))
                addListener(listener)
            }

            VideoPlayerEvent.ResetAll -> {
                exoPlayer.removeListener(listener)
                exoPlayer.release()
                state = state.copy(
                    shouldShowControls = false,
                    isPlaying = false,
                    totalDuration = 0,
                    currentTime = 0,
                    bufferedPercentage = 0,
                    playbackState = STATE_NOT_STARTED,
                    isFullScreen = false
                )
            }

            VideoPlayerEvent.ToggleControls -> state =
                state.copy(shouldShowControls = state.shouldShowControls.not())

            VideoPlayerEvent.OnPause -> exoPlayer.pause()
            VideoPlayerEvent.OnPlay -> exoPlayer.play()
        }
    }
}
