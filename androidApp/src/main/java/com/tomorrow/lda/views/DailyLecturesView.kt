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
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.mappers.toEvent
import com.tomorrow.lda.shared.domain.model.Session
import com.tomorrow.lda.shared.domain.use_cases.GetAppropriateDisplayedDayForEvent
import com.tomorrow.lda.shared.domain.use_cases.GetSessionsUseCase
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
            eventsByDay = sessions.map { it.toEvent() }.groupBy { it.startDate.toLocalDate() },
            displayedDay = GetAppropriateDisplayedDayForEvent().getDay(sessions)?.toJavaLocalDate()
        )
    }
}

class DailyLecturesViewModel : ReadViewModel<DailyLecturesState>(
    load = { GetSessionsUseCase().getSessions().map { DailyLecturesState.fromSessions(it) } },
    refresh = { GetSessionsUseCase().refresh().map { DailyLecturesState.fromSessions(it) } }
) {
    override fun onDataReception(d: DailyLecturesState) {
        val oldSelectedDay = state.viewData?.displayedDay

        state = state.copy(
            viewData = d.copy(
                displayedDay = if (d.eventsByDay.keys.contains(oldSelectedDay)) oldSelectedDay else d.displayedDay
            )
        )
    }

    fun changeSelectedDay(day: LocalDate) {
        state = state.copy(viewData = state.viewData?.copy(displayedDay = day))
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
                onDaySelected = { day -> viewModel.changeSelectedDay(day) }
            )
        }
    }
}