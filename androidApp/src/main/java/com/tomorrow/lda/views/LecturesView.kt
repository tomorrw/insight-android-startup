package com.tomorrow.lda.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.R
import com.tomorrow.lda.common.cards.DefaultCardDisplay
import com.tomorrow.lda.common.headers.PageHeaderLayout
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController

@Composable
fun LecturesView() {
    val navController = LocalNavController.current

    PageHeaderLayout(
        title = "Lectures",
        subtitle = "Explore lectures and plan your day"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(vertical = 24.dp)
        ) {
            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_schedule_foreground),
                onClick = { navController.navigate(AppRoute.DailyLectures.generate()) },
                title = "Lectures Schedule",
                subtitle = "Explore the complete schedule of lectures by day",
                isHighlighted = true
            )

            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_bookmarks_foreground),
                onClick = { navController.navigate(AppRoute.MyLectures.generate()) },
                title = "My Lectures",
                subtitle = "Manage and access your bookmarked lectures",
            )

            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_speaker_foreground),
                onClick = { navController.navigate(AppRoute.Speakers.generate()) },
                title = "Speakers",
                subtitle = "Discover our diverse speakers",
            )
        }
    }
}