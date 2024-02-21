package com.tomorrow.convenire.feature_video

import android.media.metrics.PlaybackStateEvent

data class VideoPlayerState(
    val shouldShowControls: Boolean = false,
    val isPlaying: Boolean = false,
    val totalDuration: Long = 0,
    val currentTime: Long = 0,
    val bufferedPercentage: Int = 0,
    val playbackState: Int = PlaybackStateEvent.STATE_NOT_STARTED,
    val isFullScreen: Boolean = false
)