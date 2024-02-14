package com.tomorrow.convenire.feature_video

sealed class VideoPlayerEvent {
    object OnPauseToggle : VideoPlayerEvent()
    object OnPause : VideoPlayerEvent()
    object OnPlay : VideoPlayerEvent()
    class OnSeekChanged(val value: Float) : VideoPlayerEvent()
    object OnFullScreenClicked : VideoPlayerEvent()
    class OnNewVideo(val videoUrl: String) : VideoPlayerEvent()
    object ResetAll : VideoPlayerEvent()
    object ToggleControls : VideoPlayerEvent()
}
