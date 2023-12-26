package com.tomorrow.lda.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.lda.R

@Composable
fun AlbumArt(
    modifier: Modifier = Modifier,
    imageUrl: String,
    ratio: Ratio = Ratio.Square,
    trackTime: Float? = null,
    borderRadius: Dp = 0.dp,
    contentScale: ContentScale = ContentScale.Crop,
    imagePlaceHolderType: ImagePlaceHolderType = ImagePlaceHolderType.VideoPlaceHolder
) {
    val placeHolderImage = when (imagePlaceHolderType) {
        ImagePlaceHolderType.MusicPlaceHolder -> painterResource(id = R.drawable.ic_music_default_albumart)
        ImagePlaceHolderType.VideoPlaceHolder -> painterResource(id = R.drawable.ic_video_default_albumart)
        ImagePlaceHolderType.PersonPlaceHolder -> painterResource(id = R.drawable.ic_avatar_placeholder)
        ImagePlaceHolderType.ImagePlaceHolder -> painterResource(id = R.drawable.ic_image_default_albumart)
        ImagePlaceHolderType.None -> null
    }
    AlbumArt(
        modifier = modifier,
        image = {
            AsyncImage(
                model = imageUrl,
                placeholder = placeHolderImage,
                fallback = placeHolderImage,
                contentDescription = "Placeholder Image",
                contentScale = contentScale,
                modifier = Modifier
                    .background(Color(0xFF2F3D45), shape = RoundedCornerShape(borderRadius))
                    .aspectRatio(ratio = ratio.value)
                    .clip(RoundedCornerShape(borderRadius))
                    .fillMaxSize(),
                filterQuality = FilterQuality.Low
            )
        },
        trackTime = trackTime,
    )
}


// this is the base of all album art
// please use it to create new variations of media album art
@Composable
fun AlbumArt(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
    trackTime: Float? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        image()

        if (trackTime != null && trackTime > 0f) {
            Box(modifier = Modifier, contentAlignment = Alignment.BottomCenter) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = trackTime,
                    backgroundColor = Color.Black.copy(0.3f),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


enum class Ratio(
    val value: Float
) {
    SixteenByNine(16f / 9f),
    Square(1f)
}

enum class ImagePlaceHolderType {
    VideoPlaceHolder,
    MusicPlaceHolder,
    PersonPlaceHolder,
    ImagePlaceHolder,
    None
}