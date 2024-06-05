package com.tomorrow.convenire.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.GeneralError
import com.tomorrow.convenire.common.PullToRefreshLayout
import com.tomorrow.convenire.common.Section
import com.tomorrow.convenire.common.SectionDisplay
import com.tomorrow.convenire.common.headers.PageHeaderLayout
import com.tomorrow.convenire.common.view_models.DefaultReadView
import com.tomorrow.convenire.common.view_models.ReadViewModel
import com.tomorrow.convenire.feature_events.Event
import com.tomorrow.convenire.feature_events.EventsLoader
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.mappers.toEvent
import com.tomorrow.convenire.shared.domain.model.ProgressReport
import com.tomorrow.convenire.shared.domain.use_cases.GetProgressReportUseCase
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import android.graphics.Color as GraphicsColor

data class MyProgressState(
    val events: List<Event>,
    val stats: List<Pair<String, String>>,
    val progress: Float,
    val progressColor: String,
) {
    companion object {
        fun fromProgressReport(report: ProgressReport) = MyProgressState(
            events = report.attendedSessions.map { it.toEvent().copy(isMinutesDisplayed = true) },
            stats = listOf(
                Pair(report.league.lecturesAttendedCount.toString(), "Lectures Attended"),
                Pair(
                    report.totalAttendedDuration.toDurationFormatString(),
                    "Time Spent in Session"
                )
            ),
            progress = report.league.percentage,
            progressColor = report.league.color
        )
    }
}

class MyProgressViewModel : ReadViewModel<MyProgressState>(
    load = {
        GetProgressReportUseCase().getMyProgress().map { MyProgressState.fromProgressReport(it) }
    },
    refresh = {
        GetProgressReportUseCase().refresh().map { MyProgressState.fromProgressReport(it) }
    },
    emptyCheck = { data -> data.events.isEmpty() }
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProgressView() {
    val navController = LocalNavController.current
    PageHeaderLayout(
        title = "My Progress",
        onBackPress = { navController.popBackStack() }
    ) {
        val viewModel: MyProgressViewModel = koinViewModel()

        PullToRefreshLayout(
            state = rememberPullRefreshState(
                refreshing = viewModel.state.isRefreshing,
                onRefresh = { viewModel.on(ReadViewModel.Event.OnRefresh) }
            ),
            isRefreshing = viewModel.state.isRefreshing
        ) {
            DefaultReadView(viewModel = viewModel, loader = { EventsLoader() },
                emptyState = {
                    GeneralError(
                        message = "No Progress Yet.",
                        description = "After attending lectures, you can assess your progress here.",
                        buttonText = "Reload",
                        onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) }
                    )
                }, error = {
                    GeneralError(
                        message = it,
                        description = "Please check your internet connection and try again.",
                        onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) }
                    )
                }) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            it.stats.forEachIndexed { index, data ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Text(data.first, style = MaterialTheme.typography.headlineSmall)
                                    Text(
                                        modifier = Modifier.widthIn(max = 70.dp),
                                        text = data.second,
                                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                }

                                if (index != it.stats.size - 1) Spacer(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
                                        .height(height = 34.dp)
                                        .width(width = 1.dp)
                                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Box(
                            Modifier
                                .clip(
                                    RoundedCornerShape(4.dp)
                                )
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                                .fillMaxWidth()
                                .height(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(it.progress)
                                    .fillMaxHeight()
                                    .align(Alignment.CenterStart)
                                    .background(Color(GraphicsColor.parseColor("#${it.progressColor}")))
                            ) {}
                        }

                    }


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                    ) {
                        item {
                            SectionDisplay(
                                contentPadding = PaddingValues(0.dp),
                                section = Section.EventList(
                                    title = "Attended Lectures",
                                    events = it.events,
                                    shouldDisplayTitle = false,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

