package com.tomorrow.mobile_starter_app.views

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
import com.tomorrow.carousel.AdCarousel
import com.tomorrow.components.cards.HighlightedCard
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.PullToRefreshLayout
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.AdPresentationModelMapper
import com.tomorrow.mobile_starter_app.packageImplementation.mappers.toEvent
import com.tomorrow.mobile_starter_app.common.*
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.mobile_starter_app.shared.domain.model.HomeData
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetHomeDataUseCase
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import org.koin.androidx.compose.getViewModel

class HomeViewModel : ReadViewModel<HomeData>(
    load = { GetHomeDataUseCase().getHome() },
    refresh = { GetHomeDataUseCase().refresh() },
    emptyCheck = { it.isEmpty() }
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

    DefaultReadView(
        viewModel = viewModel,
        emptyState = {
            GeneralError(
                modifier = Modifier.padding(16.dp),
                message = "No data found",
                description = "Stay Tuned for more updates!",
                onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
            )
        }) {
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
                            AdCarousel(
                                items = it.map { ad -> AdPresentationModelMapper().mapFromEntity(ad) },
                                onClick = { url, context ->
                                    handleLink(url, navController, context)
                                },
                            )
                    }

                it.upcomingSessions
                    .getDataIfLoaded()
                    ?.let { sessions ->
                        if (sessions.isNotEmpty()) {
                            SectionDisplay(
                                section = Section.EventList(
                                    "Upcoming Lectures",
                                    sessions.map {
                                        it.toEvent(onClick = { id ->
                                            navController.navigate(
                                                AppRoute.EventDetail.generateExplicit(id)
                                            )
                                        })
                                    }
                                ),
                                onShowAll = { navController.navigate(AppRoute.DailyLectures.generate()) }
                            )

                            it.ads
                                .getDataIfLoaded()
                                ?.let {
                                    AdCarousel(
                                        items = it.map { ad ->
                                            AdPresentationModelMapper().mapFromEntity(
                                                ad
                                            )
                                        },
                                        onClick = { url, context ->
                                            handleLink(url, navController, context)
                                        },
                                    )
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