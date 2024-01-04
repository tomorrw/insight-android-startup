package com.tomorrow.convenire.launch

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tomorrow.convenire.common.BottomBar
import com.tomorrow.convenire.common.Loader
import com.tomorrow.convenire.feature_in_app_update.InAppUpdater
import com.tomorrow.convenire.feature_internet_connectivity.ConnectivityStatus
import com.tomorrow.convenire.feature_navigation.AppRoute
import com.tomorrow.convenire.feature_navigation.setUp
import com.tomorrow.convenire.shared.domain.model.ColorTheme
import com.tomorrow.convenire.shared.domain.use_cases.ColorThemeUseCase
import com.tomorrow.convenire.shared.domain.use_cases.IsAuthenticatedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    val uiTheme = ColorThemeUseCase().getColorTheme()

    CompositionLocalProvider(
        LocalLayoutDirection provides currentLayoutDirection,
        LocalNavController provides navController,
    ) {
        JetpackComposeDarkThemeTheme(
            darkTheme = when(uiTheme) {
                ColorTheme.Light -> false
                ColorTheme.Dark -> true
                else -> isSystemInDarkTheme()
            }
        ) {
            Scaffold(
                bottomBar = { BottomBar(isVisible = AppRoute.shouldDisplayBottomBar(currentRoute)) },
                containerColor = MaterialTheme.colors.surface
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

            InAppUpdater()

            Box(Modifier.fillMaxSize()) { fullScreenViewModel.content() }
        }
//        androidx.compose.material3.MaterialTheme(
//            appColorsMaterial3,
//            typography = typographyMaterial3
//        ) {
//            MaterialTheme(appColors, typography = typography) {
//
//            }
//        }
    }
}

class FullScreenViewModel : ViewModel() {
    var content: @Composable () -> Unit by mutableStateOf({ })

    fun setFullScreen(content: @Composable () -> Unit) {
        this.content = content
    }

    fun clear() {
        this.content = {}
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun ConnectivityStatusWrapper(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope) -> Unit
) = Column(modifier) {
    val systemUiController = rememberSystemUiController()
    val surfaceColor = androidx.compose.material3.MaterialTheme.colorScheme.surface

    LaunchedEffect(key1 = "") {
        systemUiController.setNavigationBarColor(surfaceColor)
        systemUiController.setSystemBarsColor(surfaceColor)
    }

    ConnectivityStatus()

    content(this)
}
