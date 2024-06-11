package com.tomorrow.convenire.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.rememberForeverLazyListState
import com.tomorrow.convenire.common.SpeakersTagList
import com.tomorrow.convenire.packageImplementation.mappers.toEvent
import com.tomorrow.convenire.common.buttons.BookmarkEventButton
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.launch.LocalNavController
import com.tomorrow.convenire.packageImplementation.models.Event
import com.tomorrow.convenire.shared.domain.model.Session
import com.tomorrow.convenire.shared.domain.use_cases.GetAppropriateDisplayedDayForEvent
import com.tomorrow.convenire.shared.domain.use_cases.GetSessionsUseCase
import com.tomorrow.eventlisting.EventCard
import com.tomorrow.eventlisting.EventsList
import com.tomorrow.eventlisting.EventsLoader
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toJavaLocalDate
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

data class DailyLecturesState(
    val eventsByDay: Map<LocalDate, List<Event>>,
    val displayedDay: LocalDate?
) {
    companion object {
        fun fromSessions(sessions: List<Session>) = DailyLecturesState(
            eventsByDay = sessions.map { it.toEvent() }
                .groupBy { it.startDate.toLocalDate() },
            displayedDay = GetAppropriateDisplayedDayForEvent().getDay(sessions)?.toJavaLocalDate()
        )
    }
}

class DailyLecturesViewModel : ReadViewModel<DailyLecturesState>(
    load = { GetSessionsUseCase().getSessions().map { DailyLecturesState.fromSessions(it) } },
    refresh = { GetSessionsUseCase().refresh().map { DailyLecturesState.fromSessions(it) } }
) {
    override fun onDataReception(d: DailyLecturesState) {
        val oldSelectedDay = state.viewData.getDataIfLoaded()?.displayedDay

        state = state.copy(
            viewData = d.copy(displayedDay = if (d.eventsByDay.keys.contains(oldSelectedDay)) oldSelectedDay else d.displayedDay)
        )
    }

    fun changeSelectedDay(day: LocalDate) {
        state.viewData.getDataIfLoaded()
            ?.let { state = state.copy(viewData = it.copy(displayedDay = day)) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DailyLecturesView(
    preSelectedDate: LocalDate? = null
) {
    val navController = LocalNavController.current

    PageHeaderLayout(
        title = "Lectures Schedule",
        onBackPress = { navController.popBackStack() }
    ) {
        val viewModel: DailyLecturesViewModel = koinViewModel()

        LaunchedEffect(key1 = preSelectedDate) {
            if (preSelectedDate == null) return@LaunchedEffect
            viewModel.changeSelectedDay(preSelectedDate)
        }

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
                onDaySelected = { day -> viewModel.changeSelectedDay(day) },
                eventCard = { event ->
                    EventCard(
                        event = event.toEvent().copy(
                            onClick = { id ->
                                navController.navigate(
                                    AppRoute.EventDetail.generateExplicit(id)
                                )
                            }),
                        cardFooter = {
                            if (event is Event){
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