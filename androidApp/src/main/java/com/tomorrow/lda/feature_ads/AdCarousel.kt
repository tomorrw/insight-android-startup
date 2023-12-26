package com.tomorrow.lda.feature_ads

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tomorrow.lda.common.handleLink
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.shared.domain.model.Ad
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.delay

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun AdCarousel(
    modifier: Modifier = Modifier,
    items: List<Ad>,
) {
    val listState = rememberLazyListState()
    var loop by remember { mutableStateOf(true) }
    var lastAnimatedToIndex by remember { mutableStateOf(0) }
    val navController = LocalNavController.current
    val firstVisibleItem: State<Int> = remember(listState) {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    LaunchedEffect(loop) {
        delay(5000)

        lastAnimatedToIndex = when {
            // if the firstVisibleItem isn't the same as the one we animated to
            // it means the user manually scrolled through
            firstVisibleItem.value != lastAnimatedToIndex -> firstVisibleItem.value
            lastAnimatedToIndex == items.lastIndex -> 0
            else -> lastAnimatedToIndex + 1
        }

        if (!listState.isScrollInProgress) listState.animateScrollToItem(lastAnimatedToIndex)

        loop = loop.not()
    }

    Column(modifier) {
        val context = LocalContext.current

        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            flingBehavior = rememberSnapperFlingBehavior(
                listState,
                snapOffsetForItem = { _, _ -> 0 },
                snapIndex = { _, startIndex, targetIndex ->
                    targetIndex.coerceIn(startIndex - 1, startIndex + 1)
                },
                springAnimationSpec = spring()
            )
        ) {
            items(items) {
                AsyncImage(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(88.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                        .clickable(true) {
                            handleLink(it.url, navController, context)
                        },
                    model = it.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun CarouselDotIndex(
    isSelected: Boolean
) = Box(
    Modifier
        .clip(RoundedCornerShape(50.dp))
        .size(8.dp)
        .background(MaterialTheme.colors.secondary.copy(alpha = if (isSelected) 1f else 0.2f))
)