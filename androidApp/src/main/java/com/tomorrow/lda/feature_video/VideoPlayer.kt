package com.tomorrow.lda.feature_video

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.tomorrow.lda.launch.FullScreenViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel


@Composable
fun VideoPlayer(modifier: Modifier = Modifier, videoUrl: String) {
    val viewModel: VideoPlayerViewModel = getViewModel()
    var video by rememberSaveable { mutableStateOf("") }
    val fullScreenViewModel: FullScreenViewModel = getViewModel()

    LaunchedEffect(key1 = viewModel.state.isFullScreen) {
        if (viewModel.state.isFullScreen) fullScreenViewModel.setFullScreen {
            FullScreenPlayer(viewModel)
        } else fullScreenViewModel.clear()
    }

    LaunchedEffect(key1 = videoUrl) {
        if (video == videoUrl) return@LaunchedEffect

        video = videoUrl
        viewModel.on(VideoPlayerEvent.OnNewVideo(video))
    }

    var tmpFullScreen by rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(key1 = "") {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.on(VideoPlayerEvent.OnPause)
                }

                Lifecycle.Event.ON_RESUME -> {
                    viewModel.on(VideoPlayerEvent.OnPlay)
                }

                else -> {}
            }
        }
        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            if (tmpFullScreen == viewModel.state.isFullScreen) viewModel.on(VideoPlayerEvent.ResetAll)
            else tmpFullScreen = viewModel.state.isFullScreen

            lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (!viewModel.state.isFullScreen) Player(viewModel)
    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
private fun FullScreenPlayer(viewModel: VideoPlayerViewModel) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val systemUiController: SystemUiController = rememberSystemUiController()

    DisposableEffect("") {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        systemUiController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        systemUiController.isSystemBarsVisible = false
        activity?.let {
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        }

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            systemUiController.isSystemBarsVisible = true
            systemUiController.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
            activity?.let {
                WindowCompat.setDecorFitsSystemWindows(activity.window, true)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Player(viewModel)
    }
}

@Composable
private fun Player(viewModel: VideoPlayerViewModel) {
    val context = LocalContext.current

    BackHandler(enabled = viewModel.state.isFullScreen) {
        viewModel.on(VideoPlayerEvent.OnFullScreenClicked)
    }

    LaunchedEffect(key1 = Pair(viewModel.state.shouldShowControls, viewModel.state.isPlaying)) {
        if (!viewModel.state.shouldShowControls) return@LaunchedEffect

        delay(3000)
        if (viewModel.state.isPlaying) viewModel.on(VideoPlayerEvent.ToggleControls)
    }

    val interactionSource = remember { MutableInteractionSource() }
    AndroidView(modifier = Modifier
        .background(Color.Black)
        .clickable(interactionSource = interactionSource, indication = null) {
            viewModel.on(
                VideoPlayerEvent.ToggleControls
            )
        },
        factory = {
            StyledPlayerView(context).apply {
                player = viewModel.exoPlayer
                useController = false
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    )

    PlayerControls(
        Modifier.fillMaxSize(),
        isVisible = { viewModel.state.shouldShowControls },
        isPlaying = { viewModel.state.isPlaying },
        playbackState = { viewModel.state.playbackState },
        fullScreenState = { viewModel.state.isFullScreen },
        onPauseToggle = { viewModel.on(VideoPlayerEvent.OnPauseToggle) },
        totalDuration = { viewModel.state.totalDuration },
        currentTime = { viewModel.state.currentTime },
        bufferedPercentage = { viewModel.state.bufferedPercentage },
        onSeekChanged = { viewModel.on(VideoPlayerEvent.OnSeekChanged(it)) },
        onFullScreenClicked = { viewModel.on(VideoPlayerEvent.OnFullScreenClicked) }
    )
}
