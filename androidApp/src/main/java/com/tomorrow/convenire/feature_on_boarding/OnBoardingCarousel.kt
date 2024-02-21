package com.tomorrow.convenire.feature_on_boarding

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

class OnBoardingItem(
    val title: String,
    val body: String,
    val buttons: @Composable (ColumnScope) -> Unit,
)

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun OnBoardingCarousel(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    items: List<OnBoardingItem>,
) {
    val firstVisibleItem: State<Int> =
        remember { derivedStateOf { listState.firstVisibleItemIndex } }

    Column(modifier) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
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
                OnBoardingCarouselItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    title = it.title,
                    body = it.body
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in items.indices) {
                    CarouselDotIndex(isSelected = firstVisibleItem.value == i)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items[firstVisibleItem.value].buttons(this)
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

@Composable
private fun OnBoardingCarouselItem(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.body1,
            color = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}