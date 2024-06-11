package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.rememberForeverLazyListState
import com.tomorrow.convenire.common.SpeakersTagList
import com.tomorrow.convenire.common.buttons.BookmarkEventButton
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.kmmProjectStartup.data.utils.Loadable
import com.tomorrow.kmmProjectStartup.data.utils.Loaded
import com.tomorrow.convenire.packageImplementation.mappers.toEvent
import com.tomorrow.convenire.packageImplementation.models.Event
import com.tomorrow.convenire.shared.domain.model.Session
import com.tomorrow.convenire.shared.domain.use_cases.GetAppropriateDisplayedDayForEvent
import com.tomorrow.convenire.shared.domain.use_cases.GetSessionsUseCase
import com.tomorrow.convenire.shared.domain.use_cases.ShouldNotifyEventUseCase
import com.tomorrow.eventlisting.EventCard
import com.tomorrow.eventlisting.EventsList
import com.tomorrow.eventlisting.EventsLoader
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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
                    it.value.filter { s -> ShouldNotifyEventUseCase().shouldNotify(s.id) }
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
), KoinComponent {
    override fun onDataReception(d: MyLecturesState) {
        val oldSelectedDay = state.viewData.getDataIfLoaded()?.displayedDay


        state = state.copy(
            viewData = d.copy(
                displayedDay = if (d.eventsByDay.keys.contains(oldSelectedDay)) oldSelectedDay else d.displayedDay
            )
        )
        listenForBookmarkChanges(d.eventsByDay.values.flatten().map { it.id })
    }

    private var listenForBookmarkChangesJob: Job? = null
    private val scope: CoroutineScope by inject()

    private fun listenForBookmarkChanges(ids: List<String>) {
        listenForBookmarkChangesJob?.cancel()

        try {
            listenForBookmarkChangesJob = scope.launch {
                if (ids.isEmpty()) return@launch
                ShouldNotifyEventUseCase()
                    .shouldNotifyFlow(ids)
                    .collect { isBookmarkedMap ->
                        val newViewData = state.viewData.getDataIfLoaded()?.let {
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
        } catch (e: Exception) {

        }
    }

    fun changeSelectedDay(day: LocalDate) {
        state.viewData.getDataIfLoaded()?.let {
            state = state.copy(viewData = it.copy(displayedDay = day))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyLecturesView() {
    val navController = LocalNavController.current
    val viewModel: MyLecturesViewModel = koinViewModel()

    LaunchedEffect(key1 = "hello") {
        viewModel.on(ReadViewModel.Event.LoadSilently)
    }

    PageHeaderLayout(
        title = "My Lectures",
        onBackPress = { navController.popBackStack() }
    ) {

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
                                AppRoute.DailyLectures.generateExplicit(it.displayedDay.toString())
                            )
                        }
                    )
                },
                onDaySelected = { day -> viewModel.changeSelectedDay(day) },
                eventCard = { event ->
                    EventCard(
                        event = event.toEvent().copy(onClick = { id ->
                            navController.navigate(
                                AppRoute.EventDetail.generateExplicit(id)
                            )
                        }),
                        cardFooter = {
                            if (event is Event) {
                                SpeakersTagList(speakers = event.speakers)
                            }
                        },
                        rightIcon = { id ->
                            BookmarkEventButton(id = id)
                        })
                }
            )
        }
    }
}

