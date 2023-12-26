package com.tomorrow.lda.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.lda.common.GeneralError
import com.tomorrow.lda.common.headers.PageHeaderLayout
import com.tomorrow.lda.common.rememberForeverLazyListState
import com.tomorrow.lda.common.view_models.DefaultReadView
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_events.Event
import com.tomorrow.lda.feature_events.EventsList
import com.tomorrow.lda.feature_events.EventsLoader
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toEvent
import com.tomorrow.lda.shared.domain.model.Session
import com.tomorrow.lda.shared.domain.use_cases.GetAppropriateDisplayedDayForEvent
import com.tomorrow.lda.shared.domain.use_cases.GetSessionsUseCase
import com.tomorrow.lda.shared.domain.use_cases.ShouldNotifyEventUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import java.time.LocalDate

data class MyLecturesState(
    val eventsByDay: Map<LocalDate, List<Event>>,
    val displayedDay: LocalDate?
) {
    companion object : KoinComponent {
        fun fromSessions(sessions: List<Session>) = MyLecturesState(
            eventsByDay = sessions
                .groupBy { it.startTime.toJavaLocalDateTime().toLocalDate() }
                .mapValues {
                    it.value.filter { s -> ShouldNotifyEventUseCase().shouldNotify(s.id) || s.hasAttended }
                        .map { s -> s.toEvent() }
                },
            displayedDay = GetAppropriateDisplayedDayForEvent().getDay(sessions)
                ?.toJavaLocalDate()
        )
    }
}

class MyLecturesViewModel : ReadViewModel<MyLecturesState>(
    load = { GetSessionsUseCase().getSessions().map { MyLecturesState.fromSessions(it) } },
    refresh = { GetSessionsUseCase().refresh().map { MyLecturesState.fromSessions(it) } }
) {
    override fun onDataReception(d: MyLecturesState) {
        val oldSelectedDay = state.viewData?.displayedDay

        state = state.copy(
            viewData = d.copy(
                displayedDay = if (d.eventsByDay.keys.contains(oldSelectedDay)) oldSelectedDay else d.displayedDay
            )
        )
        listenForBookmarkChanges(d.eventsByDay.values.flatten().map { it.id })
    }

    private var listenForBookmarkChangesJob: Job? = null
    private fun listenForBookmarkChanges(ids: List<String>) {
        listenForBookmarkChangesJob?.cancel()

        listenForBookmarkChangesJob = scope.launch {
            ShouldNotifyEventUseCase()
                .shouldNotifyFlow(ids)
                .collect { isBookmarkedMap ->
                    val newViewData = state.viewData?.let {
                        it.copy(eventsByDay = it
                            .eventsByDay
                            .mapValues { entry ->
                                entry.value
                                    .filter { event -> isBookmarkedMap[event.id] == true || event.hasAttended }
                            }
                        )
                    }

                    state = state.copy(viewData = newViewData)
                }
        }
    }

    fun changeSelectedDay(day: LocalDate) {
        state = state.copy(viewData = state.viewData?.copy(displayedDay = day))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyLecturesView() {
    val navController = LocalNavController.current
    PageHeaderLayout(
        title = "My Schedule",
        onBackPress = { navController.popBackStack() }
    ) {
        val viewModel: MyLecturesViewModel = koinViewModel()

        DefaultReadView(viewModel = viewModel, loader = { EventsLoader() }, error = {
            GeneralError(
                message = it,
                description = "Please check your internet connection and try again.",
                onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) }
            )
        }) {
            Spacer(modifier = Modifier.height(20.dp))

            val listStates = it.eventsByDay.map { eventsByDay ->
                eventsByDay.key to rememberForeverLazyListState(key = eventsByDay.key.toString())
            }.toMap()

            EventsList(
                events = it.eventsByDay[it.displayedDay] ?: listOf(),
                state = rememberPullRefreshState(
                    refreshing = viewModel.state.isRefreshing,
                    onRefresh = {
                        viewModel.on(ReadViewModel.Event.OnRefresh)
                    }),
                isRefreshing = viewModel.state.isRefreshing,
                lazyListState = listStates[it.displayedDay] ?: rememberLazyListState(),
                days = it.eventsByDay.keys.toList(),
                selectedDay = it.displayedDay,
                emptyView = {
                    GeneralError(
                        message = "No Bookmarked Lectures",
                        description = "Bookmark interesting lectures. Check the schedule, click the bookmark icon to save. Happy exploring!",
                        buttonText = "Lectures Schedule",
                        onButtonClick = {
                            navController.navigate(
                                AppRoute.DailyLectures.generateExplicit(
                                    viewModel.state.viewData?.displayedDay.toString()
                                )
                            )
                        }
                    )
                },
                onDaySelected = { day -> viewModel.changeSelectedDay(day) }
            )
        }
    }
}

