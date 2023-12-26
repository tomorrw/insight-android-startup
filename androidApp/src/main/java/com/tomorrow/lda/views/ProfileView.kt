package com.tomorrow.lda.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomorrow.lda.common.GradientProgressIndicator
import com.tomorrow.lda.common.PullToRefreshLayout
import com.tomorrow.lda.common.buttons.ActionButton
import com.tomorrow.lda.common.cards.InlineHighlightedCard
import com.tomorrow.lda.common.headers.PageHeaderLayout
import com.tomorrow.lda.common.view_models.DefaultReadView
import com.tomorrow.lda.common.view_models.ReadViewModel
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController
import com.tomorrow.lda.shared.domain.model.User
import com.tomorrow.lda.shared.domain.use_cases.GetUserUseCase
import org.koin.androidx.compose.koinViewModel
import android.graphics.Color as GraphicsColor

class MyProfileViewModel : ReadViewModel<User>(load = { GetUserUseCase().getUser() },
    refresh = { GetUserUseCase().getUser() })

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileView() {
    val navController = LocalNavController.current
    val viewModel: MyProfileViewModel = koinViewModel()

    PageHeaderLayout(
        title = "Profile",
        subtitle = "Track your progress"
    ) {
        DefaultReadView(viewModel = viewModel) { user ->
            PullToRefreshLayout(
                state = rememberPullRefreshState(
                    refreshing = viewModel.state.isRefreshing,
                    onRefresh = { viewModel.on(ReadViewModel.Event.OnRefresh) }
                ),
                isRefreshing = viewModel.state.isRefreshing
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InlineHighlightedCard(name = user.getFullName(),
                        avatar = "",
                        detailText = user.phoneNumber.getFormattedNumberInOriginalFormat(),
                        onClick = { navController.navigate(AppRoute.Settings.path) })

                    Spacer(modifier = Modifier.height(16.dp))

                    val leagueColor = Color(GraphicsColor.parseColor("#${user.league.color}"))

                    CompositionLocalProvider(
                        LocalDensity provides Density(LocalDensity.current.density, 1f)
                    ) {
                        Box(
                            Modifier
                                .padding(horizontal = 32.dp)
                                .clip(CircleShape)
                                .background(Color.White), contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.aspectRatio(1f).padding(20.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "${user.league.lecturesAttendedCount}/${user.league.totalNumberOfLectures}",
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 60.sp)
                                )
                                Text(
                                    text = "LECTURES ATTENDED",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center
                                )
                            }

                            GradientProgressIndicator(
                                modifier = Modifier
                                    .matchParentSize()
                                    .padding(4.dp),
                                progress = user.league.percentage.let { if (it == 0f) 0.01f else it },
                                gradientStart = leagueColor.copy(0.4f),
                                gradientEnd = leagueColor,
                                strokeWidth = 12.dp,
                            )
                        }
                    }

                    if (user.league.name.isNotBlank()) Text(
                        text = user.league.name,
                        style = MaterialTheme.typography.headlineMedium.copy(leagueColor),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    user.actions.map {
                        ActionButton(action = it)
                    }
                }
            }

        }
    }
}