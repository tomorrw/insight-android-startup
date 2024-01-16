package com.tomorrow.convenire.feature_events

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import com.tomorrow.convenire.common.GeneralError
import com.tomorrow.convenire.common.PullToRefreshLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private val getMonthFormatter = DateTimeFormatter.ofPattern("MMM")

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class
)
@Composable
fun EventsList(
    events: List<Event>,
    state: PullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { }),
    isRefreshing: Boolean = false,
    lazyListState: LazyListState = rememberLazyListState(),
    days: List<LocalDate>,
    selectedDay: LocalDate?,
    emptyView: @Composable () -> Unit = {
        GeneralError(message = "Nothing to see here yet.", description = "")
    },
    onDaySelected: (LocalDate) -> Unit,
) {
    //  ========= DAYS =========
    val eventsByLocation: Map<String, List<Event>> by remember(events) {
        derivedStateOf {
            mapOf("All Locations" to events).plus(
                events.groupBy { it.location }.toSortedMap(Comparator.reverseOrder())
            )
        }
    }

    var selectedLocation by remember {
        mutableStateOf(eventsByLocation.keys.firstOrNull() ?: "")
    }

    LaunchedEffect(key1 = eventsByLocation) {
        if (eventsByLocation.keys.contains(selectedLocation).not()) {
            selectedLocation = eventsByLocation.keys.firstOrNull() ?: ""
        }
    }

    val filteredEvents by remember(eventsByLocation) {
        derivedStateOf { eventsByLocation.getOrDefault(selectedLocation, events) }
    }

    PullToRefreshLayout(state = state, isRefreshing = isRefreshing) {
        LaunchedEffect(key1 = selectedDay) {
            getCurrentEventIndex(events).let {
                if (it != -1) lazyListState.animateScrollToItem(it)
            }
        }

        LazyColumn(
            Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                .animateContentSize(),
            state = lazyListState
        ) {
            if (events.isEmpty()) item {
                Column(modifier = Modifier.fillParentMaxHeight()) {
                    Header(
                        lazyListState,
                        eventsByLocation,
                        days,
                        selectedDay,
                        onDaySelected,
                        selectedLocation
                    ) { selectedLocation = it }

                    emptyView()
                }
            } else {
                stickyHeader {
                    Header(
                        lazyListState,
                        eventsByLocation,
                        days,
                        selectedDay,
                        onDaySelected,
                        selectedLocation
                    ) { selectedLocation = it }
                }

                item { Spacer(Modifier.height(16.dp)) }

                items(filteredEvents, key = { it.hashCode() }) { e ->
                    Column(Modifier.animateItemPlacement()) {
                        EventCard(event = e)
                        Spacer(Modifier.size(16.dp))
                    }

                }

                item { Spacer(modifier = Modifier.size(32.dp)) }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    lazyListState: LazyListState,
    eventsByLocation: Map<String, List<Event>>,
    days: List<LocalDate>,
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit,
    selectedLocation: String,
    onSelectedLocation: (String) -> Unit,
) {
    CompositionLocalProvider(
        LocalDensity provides Density(LocalDensity.current.density, 1f)
    ) {
        val headerHeight = 130.dp
        val density = LocalDensity.current

        var oldFirstVisibleItemOffset by remember { mutableStateOf(lazyListState.firstVisibleItemScrollOffset) }
        var oldFirstVisibleItemIndex by remember { mutableStateOf(lazyListState.firstVisibleItemIndex) }
        val newFirstVisibleItemScrollOffset by remember(lazyListState) { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }
        val newFirstVisibleItemScrollIndex by remember(lazyListState) { derivedStateOf { lazyListState.firstVisibleItemIndex } }

        var headerOffset by remember { mutableStateOf(0.dp) }

        LaunchedEffect(newFirstVisibleItemScrollOffset) {
            if (oldFirstVisibleItemIndex == newFirstVisibleItemScrollIndex) {
                val changeOfOffset =
                    newFirstVisibleItemScrollOffset - oldFirstVisibleItemOffset

                with(density) {
                    headerOffset =
                        (headerOffset + changeOfOffset.toDp()).coerceIn(
                            0.dp,
                            headerHeight
                        )
                }
            }

            oldFirstVisibleItemOffset = newFirstVisibleItemScrollOffset
            oldFirstVisibleItemIndex = newFirstVisibleItemScrollIndex
        }

        Column(
            Modifier
                .height(headerHeight)
                .offset(y = -headerOffset)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                days.forEach {
                    Column(Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (it == selectedDay) MaterialTheme.colorScheme.background else androidx.compose.material.MaterialTheme.colors.surface
                        )
                        .clickable { onDaySelected(it) }
                        .padding(8.dp)
                        .width(40.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val color =
                            if (selectedDay == it) MaterialTheme.colorScheme.primary else androidx.compose.material.MaterialTheme.colors.primaryVariant

                        Text(
                            it.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(color)
                        )
                        Text(
                            getMonthFormatter.format(it),
                            style = LocalTextStyle.current.copy(color)
                        )
                    }
                }
            }

            var isLocationFilterExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                expanded = isLocationFilterExpanded,
                onExpandedChange = {
                    isLocationFilterExpanded = !isLocationFilterExpanded
                }
            ) {
                OutlinedTextField(
                    value = selectedLocation,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLocationFilterExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )

                DropdownMenu(
                    modifier = Modifier
                        .exposedDropdownSize()
                        .background(MaterialTheme.colorScheme.background),
                    expanded = isLocationFilterExpanded,
                    onDismissRequest = { isLocationFilterExpanded = false }
                ) {
                    eventsByLocation.forEach { item ->
                        Text(
                            text = item.key,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    onSelectedLocation(item.key)
                                    isLocationFilterExpanded = false
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

private fun getCurrentEventIndex(list: List<Event>): Int = list.indexOfFirst { it.isNow() }