package com.tomorrow.mobile_starter_app.launch

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tomorrow.components.others.Loader
import com.tomorrow.mobile_starter_app.packageImplementation.InAppUpdaterImplementation
import com.tomorrow.mobile_starter_app.common.BottomBar
import com.tomorrow.mobile_starter_app.feature_navigation.AppRoute
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.appSettings.ColorThemeUseCase
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.authentication.IsAuthenticatedUseCase
import com.tomorrow.internetconnectivity.ConnectivityStatusWrapper
import com.tomorrow.navigation.setUp
import com.tomorrow.videoplayer.FullScreenViewModel
import org.koin.androidx.compose.koinViewModel

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppView(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { navBackStackEntry.value?.destination?.route } }
    val currentLayoutDirection by remember { mutableStateOf(LayoutDirection.Ltr) }
    val fullScreenViewModel: FullScreenViewModel = koinViewModel()
    val isAuthenticated by IsAuthenticatedUseCase().asFlow().collectAsState()
    val uiTheme = ColorThemeUseCase().getColorTheme().collectAsState()

    CompositionLocalProvider(
        LocalLayoutDirection provides currentLayoutDirection,
        LocalNavController provides navController,
    ) {
        JetpackComposeTheme(
            darkTheme = uiTheme.value.toBoolean() ?: isSystemInDarkTheme()
        ) {
            Scaffold(
                bottomBar = { BottomBar(isVisible = AppRoute.shouldDisplayBottomBar(currentRoute)) },
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
            ) { paddingValues ->
                ConnectivityStatusWrapper(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
                    isAuthenticated?.let {
                        // if you want to add a page to the nav graph
                        // create an object (not a class) that implements com.dentiflow.android.feature_navigation.AppRoute
                        // and it will be directly added here
                        NavHost(
                            navController,
                            startDestination = (if (it) AppRoute.Home else AppRoute.OnBoarding).generate()
                        ) { setUp(AppRoute.allRoutes) }
                    } ?: Loader()
                }
            }

            InAppUpdaterImplementation()

            Box(Modifier.fillMaxSize()) { fullScreenViewModel.content() }
        }
    }
}