package com.tomorrow.mobile_starter_app.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tomorrow.components.headers.SecondaryEntityDetailHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.Loader
import com.tomorrow.components.others.PullToRefreshLayout
import com.tomorrow.components.others.TagText
import com.tomorrow.mobile_starter_app.common.SectionDisplay
import com.tomorrow.mobile_starter_app.R
import com.tomorrow.mobile_starter_app.common.SectionDisplay
import com.tomorrow.mobile_starter_app.common.buttons.BookmarkEventButton
import com.tomorrow.mobile_starter_app.common.mappers.toPageUi
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.mobile_starter_app.shared.domain.model.Session
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.GetSessionByIdUseCase
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.format.DateTimeFormatter

class EventDetailsViewModel(id: String) : ReadViewModel<Session>(
    load = { GetSessionByIdUseCase().getSession(id) },
    refresh = { GetSessionByIdUseCase().getSession(id) }
)

private val formatter = DateTimeFormatter.ofPattern("HH:mm")
private val dayFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun EventDetailsView(id: String) {
    val viewModel: EventDetailsViewModel = getViewModel { parametersOf(id) }
    val navController = LocalNavController.current

    DefaultReadView(viewModel = viewModel, error = {
        GeneralError(
            modifier = Modifier.padding(16.dp),
            message = it,
            description = "Please check your internet connection and try again.",
            onButtonClick = { viewModel.on(ReadViewModel.Event.OnRefresh) },
            onBackClick = { navController.popBackStack() }
        )
    }) {
        PullToRefreshLayout(
            state = rememberPullRefreshState(
                refreshing = viewModel.state.isRefreshing,
                onRefresh = { viewModel.on(ReadViewModel.Event.OnRefresh) }
            ),
            isRefreshing = viewModel.state.isRefreshing
        ) {
            SecondaryEntityDetailHeaderLayout(
                title = "",
                image = it.image,
                socialLinks = null,
                zoomEnabled = false,
                actions = { BookmarkEventButton(id = id) },
                onBack = { navController.popBackStack() },
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Spacer(Modifier.height(8.dp))

                        it.getTag()?.let { tag ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TagText(
                                    text = tag.text,
                                    textStyle = MaterialTheme.typography.titleSmall.copy(color = Color(android.graphics.Color.parseColor(tag.color))),
                                    backgroundColor = Color(android.graphics.Color.parseColor(tag.background))
                                )

                                if (it.minutesAttended != null) androidx.compose.material3.Text(
                                    "${it.minutesAttended}m Attended",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = Color(android.graphics.Color.parseColor(tag.color))
                                    )
                                )
                            }
                        }


                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = it.title,
                            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                        )

                        Spacer(Modifier.height(22.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = "location",
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterVertically),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = it.location,
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        FlowRow(
                            verticalArrangement = Arrangement.Center,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.DateRange,
                                    contentDescription = "date",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterVertically),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = it.startTime.toJavaLocalDateTime()
                                        .format(dayFormatter),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    modifier = Modifier
                                )
                            }

                            Row(
                                modifier = Modifier.padding(bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.baseline_access_time_24),
                                    contentDescription = "date",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterVertically),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = "${
                                        it.startTime.toJavaLocalDateTime().format(formatter)
                                    } - ${it.endTime.toJavaLocalDateTime().format(formatter)}",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    modifier = Modifier
                                )
                            }
                        }

                        if (it.numberOfAttendees.isLoaded || it.numberOfSeats.isLoaded) {
                            Row(
                                modifier = Modifier.padding(bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Person,
                                    contentDescription = "date",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterVertically),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                it.getAttendeesCount()?.let { attendees ->
                                    Text(
                                        text = attendees,
                                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        if (it.canAskQuestions) {
                            Button(
                                onClick = {
                                    navController.navigate(
                                        AppRoute.AskLectureQuestion.generateExplicit(
                                            it.id
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .heightIn(min = 50.dp)
                                    .fillMaxWidth(),
                                enabled = !viewModel.state.isLoading,
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Text(
                                    "Ask a Question",
                                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                                )
                            }
                            Spacer(Modifier.height(26.dp))
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outline)

                    Spacer(Modifier.height(26.dp))
                }

                it.detailPage.getDataIfLoaded()?.toPageUi()?.sections?.let { sections ->
                    items(sections) { section ->

                        SectionDisplay(section = section)
                        Spacer(Modifier.height(32.dp))
                    }
                } ?: item { Loader() }
            }
        }
    }
}