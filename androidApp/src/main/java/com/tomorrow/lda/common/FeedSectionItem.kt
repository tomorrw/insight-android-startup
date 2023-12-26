package com.tomorrow.lda.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun FeedSectionItem(
    modifier: Modifier? = Modifier,
    albumArt: @Composable () -> Unit,
    title: @Composable () -> Unit,
) {
    Column(
        modifier = modifier ?: Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        albumArt()

        title()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FeedSectionItemPreview() {
//    FeedSectionItem(
//        modifier = Modifier.width(80.dp),
//        albumArt = {
//            MediaAlbumArt(
//                modifier = Modifier.width(168.dp),
//                imageUrl = listOfAlbumArts.random(),
//                ratio = Ratio.SixteenByNine,
//                trackTime = 0.7f
//            )
//        },
//        twoLineDescription = "Playlist Title",
//    )
//}