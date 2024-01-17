package com.tomorrow.convenire.common.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.convenire.R
import com.tomorrow.convenire.common.*
import com.tomorrow.convenire.common.buttons.BackButton
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryEntityDetailHeaderLayout(
    title: String,
    image: String,
    socialLinks: List<SocialLink>?,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    zoomEnabled: Boolean = false,
    content: LazyListScope.() -> Unit
) = Column {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        windowInsets = WindowInsets(12.dp, 12.dp, 12.dp, 0.dp),
        title = { Text(title) },
        navigationIcon = { BackButton(onClick = { onBack() }) },
        actions = actions,
    )

    LazyColumn {
        item {
            val scale = remember { mutableStateOf(1f) }
            val rotationState = remember { mutableStateOf(1f) }
            val translation = remember { mutableStateOf(Offset(0f, 0f)) }
            val state = rememberTransformableState { zoom, pan, rotation ->
                if (!zoomEnabled) return@rememberTransformableState
                scale.value *= zoom
                rotationState.value += rotation
                translation.value += pan
            }

            LaunchedEffect(state.isTransformInProgress) {
                if (state.isTransformInProgress) return@LaunchedEffect

                scale.value = 1f
                rotationState.value = 1f
                translation.value = Offset(0f, 0f)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .transformable(state)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .graphicsLayer(
                            translationX = translation.value.x,
                            translationY = translation.value.y,
                            scaleX = maxOf(.5f, minOf(3f, scale.value)),
                            scaleY = maxOf(.5f, minOf(3f, scale.value)),
                        ),
                    model = image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_image_default_albumart)
                )
            }

            socialLinks?.let {
                Socials(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    links = it,
                )
            }

            Spacer(Modifier.height(8.dp))
        }

        content()
    }

}