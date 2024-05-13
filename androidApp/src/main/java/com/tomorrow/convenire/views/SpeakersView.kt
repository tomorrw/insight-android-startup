package com.tomorrow.convenire.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tomorrow.convenire.common.GeneralError
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_listing.ListDisplay
import com.tomorrow.convenire.feature_listing.ListHeader
import com.tomorrow.convenire.feature_listing.ListLoader
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toListDisplayItem
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import com.tomorrow.convenire.shared.domain.use_cases.GetSpeakersUseCase
import org.koin.androidx.compose.getViewModel

class SpeakersViewModel :
    ReadViewModel<List<SpeakerDetail>>(
        load = { GetSpeakersUseCase().getSpeakers() },
        refresh = { GetSpeakersUseCase().refresh() },
        emptyCheck = { it.isEmpty() }
        )

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeakersView() {
    val viewModel: SpeakersViewModel = getViewModel()
    val navController = LocalNavController.current

    PageHeaderLayout(title = "Speakers",
        subtitle = "Discover our diverse speakers",
        onBackPress = { navController.popBackStack() }) {
        DefaultReadView(
            viewModel = viewModel,
            loader = { ListLoader() },
            emptyState = {
                GeneralError(
                    modifier = Modifier.padding(16.dp),
                    message = "No data found",
                    description = "Stay Tuned for more updates!",
                    onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
                )
            }) { speakerDetail ->
            val mapData = remember(speakerDetail) {
                derivedStateOf {
                    speakerDetail.sortedBy { it.nationality?.name }.groupBy { it.nationality }
                        .mapValues {
                            it.value.map { speaker -> speaker.toListDisplayItem() }
                        }.mapKeys {
                            ListHeader {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        it.key?.name ?: "",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            textAlign = TextAlign.Start
                                        ),
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(vertical = 16.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    it.key?.url?.let {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = it),
                                            contentDescription = "country",
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .size(16.dp)
                                                .background(MaterialTheme.colorScheme.surfaceVariant),
                                        )
                                    }
                                }
                            }
                        }
                }
            }

            ListDisplay(
                map = mapData.value,
                onItemClick = { navController.navigate(AppRoute.Speaker.generateExplicit(it)) },
                header = {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth(),
                        text = "Countries are sorted by alphabetical order",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                state = rememberPullRefreshState(refreshing = viewModel.state.isRefreshing,
                    onRefresh = { viewModel.on(ReadViewModel.Event.OnRefresh) }),
                isRefreshing = viewModel.state.isRefreshing,
                listState = rememberLazyListState(),
            )
        }
    }
}