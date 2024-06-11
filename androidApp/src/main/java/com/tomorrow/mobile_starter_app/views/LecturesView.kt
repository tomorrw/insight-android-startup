package com.tomorrow.mobile_starter_app.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomorrow.components.cards.CardStyle
import com.tomorrow.components.cards.DefaultCardDisplay
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.mobile_starter_app.R
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController

@Composable
fun LecturesView() {
    val navController = LocalNavController.current

    PageHeaderLayout(
        title = "Lectures",
        subtitle = "Explore lectures and plan your day"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp)
        ) {
            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_schedule_foreground),
                onClick = { navController.navigate(AppRoute.DailyLectures.generate()) },
                title = "Lectures Schedule",
                subtitle = "Explore the complete schedule of lectures by day",
                style = CardStyle(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    titleStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
                    descriptionStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.secondary),
                    imageBackgroundColor = MaterialTheme.colorScheme.surface,
                )
            )

            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_bookmarks_foreground),
                onClick = { navController.navigate(AppRoute.MyLectures.generate()) },
                title = "My Lectures",
                subtitle = "Manage and access your bookmarked lectures",
            )

            DefaultCardDisplay(
                painter = painterResource(id = R.mipmap.ic_progress_foreground),
                onClick = { navController.navigate(AppRoute.MyProgress.generate()) },
                title = "My Progress",
                subtitle = "Checkout your progress throughout the conference",
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