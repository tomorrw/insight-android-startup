package com.tomorrow.mobile_starter_app.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.tomorrow.components.headers.PageHeaderLayout
import com.tomorrow.components.others.GeneralError
import com.tomorrow.components.others.PullToRefreshLayout
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.launch.LocalNavController
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.authentication.LogoutUseCase
import com.tomorrow.readviewmodel.DefaultReadView
import com.tomorrow.readviewmodel.ReadViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

class HomeViewModel : ReadViewModel<List<String>>(
    load = { flow { emit(listOf("HI")) }  },
    refresh = { flow { emit(listOf("HI")) }  },
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
    val scope = rememberCoroutineScope()
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
                Button(onClick = {
                    scope.launch {
                        LogoutUseCase().logout()
                            .onSuccess {
                                navController.navigate(AppRoute.OnBoarding.generate())
                                Firebase.messaging.deleteToken()
                            }
                    }

                }) {
                    Text("Logout")
                }
            }
        }
    }
}