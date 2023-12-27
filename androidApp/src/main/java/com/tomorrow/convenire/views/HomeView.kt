package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.*
import com.tomorrow.convenire.common.cards.HighlightedCard
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_ads.AdCarousel
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toEvent
import com.tomorrow.convenire.shared.domain.model.HomeData
import com.tomorrow.convenire.shared.domain.use_cases.GetHomeDataUseCase
import org.koin.androidx.compose.getViewModel

class HomeViewModel : ReadViewModel<HomeData>(
    load = { GetHomeDataUseCase().getHome() },
    refresh = { GetHomeDataUseCase().refresh() }
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView() = PageHeaderLayout(
    modifier = Modifier,
    title = "Home",
    contentPadding = PaddingValues(0.dp),
    subtitle = "Browse announcements & updates"
) {
    val viewModel: HomeViewModel = getViewModel()
    Spacer(modifier = Modifier.height(20.dp))

    val navController = LocalNavController.current
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = "") {
        viewModel.on(ReadViewModel.Event.LoadSilently)
    }

    DefaultReadView(viewModel = viewModel) {
        PullToRefreshLayout(
            state = rememberPullRefreshState(
                refreshing = viewModel.state.isRefreshing,
                onRefresh = { viewModel.on(ReadViewModel.Event.OnRefresh) }
            ),
            isRefreshing = viewModel.state.isRefreshing
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                it.highlightedPost
                    .getDataIfLoaded()
                    ?.let {
                        HighlightedCard(
                            image = it.image,
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            title = it.title,
                            description = it.description,
                            onClick = {
                                navController.navigate(
                                    AppRoute.Post.generateExplicit(
                                        it.id
                                    )
                                )
                            }
                        )
                    }

                it.todaySpeakers
                    .getDataIfLoaded()
                    ?.let {
                        if (it.isNotEmpty()) SectionDisplay(
                            section = Section.Speakers(
                                title = "Featured Speakers",
                                speakers = it
                            ),
                            onShowAll = { navController.navigate(AppRoute.Speakers.generate()) }
                        )
                    }

                it.highlightedAds
                    .getDataIfLoaded()
                    ?.let {
                        if (it.isNotEmpty())
                            AdCarousel(items = it)
                    }

                it.upcomingSessions
                    .getDataIfLoaded()
                    ?.let { sessions ->
                        if (sessions.isNotEmpty()) {
                            SectionDisplay(
                                section = Section.EventList(
                                    "Upcoming Lectures",
                                    sessions.map { it.toEvent() }
                                ),
                                onShowAll = { navController.navigate(AppRoute.DailyLectures.generate()) }
                            )

                            it.ads
                                .getDataIfLoaded()
                                ?.let {
                                    AdCarousel(items = it.reversed())
                                }
                        }
                    }

                it.posts
                    .getDataIfLoaded()
                    ?.let { posts ->
                        if (posts.isNotEmpty()) Column(
                            Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Updates", style = MaterialTheme.typography.titleLarge)

                            posts.forEach {
                                HighlightedCard(
                                    image = it.image,
                                    title = it.title,
                                    description = it.description,
                                    onClick = {
                                        navController.navigate(
                                            AppRoute.Post.generateExplicit(
                                                it.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                Spacer(Modifier.height(72.dp))
            }
        }
    }
}