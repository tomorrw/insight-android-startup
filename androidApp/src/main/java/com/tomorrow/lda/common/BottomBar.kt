package com.tomorrow.lda.common

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tomorrow.lda.R
import com.tomorrow.lda.feature_navigation.AppRoute
import com.tomorrow.lda.launch.LocalNavController

private val screens: List<BottomBarItem> = listOf(
    BottomBarItem(
        title = "Home",
        icon = R.drawable.ic_home,
        path = AppRoute.Home.path
    ),
    BottomBarItem(
        title = "Lectures",
        icon = R.drawable.ic_library_books,
        path = AppRoute.Lectures.path
    ),
    BottomBarItem(
        title = "My QR",
        icon = R.drawable.ic_qr_code,
        path = AppRoute.MyQr.path
    ),
    BottomBarItem(
        title = "Exhibitions",
        icon = R.drawable.ic_menu_book,
        path = AppRoute.Exhibitions.path
    ),
    BottomBarItem(
        title = "Profile",
        icon = R.drawable.ic_person,
        path = AppRoute.Profile.path
    ),
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(isVisible: Boolean) {
    val navController = LocalNavController.current
    val navBack by navController.currentBackStackEntryAsState()
    val currentDestination = navBack?.destination?.route
    val isKeyboardVisible by keyboardVisibilityState()

    CompositionLocalProvider(
        LocalDensity provides Density(LocalDensity.current.density, 1f)
    ) {
        AnimatedContent(
            targetState = !isKeyboardVisible && isVisible,
            label = "BottomBarAnimation"
        ) {
            if (it)
                NavigationBar(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp)
                        .clip(MaterialTheme.shapes.medium),
                    containerColor = Color(0xFFEEF5FC)
                ) {
                    screens.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.contains(item.path) == true,
                            onClick = {
                                navController.navigate(item.path) {
                                    launchSingleTop = true
                                    navController.navigateUp()
                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(item.icon),
                                    contentDescription = "${item.title} Icon",
                                    modifier = Modifier.padding(bottom = 4.dp, top = 4.dp)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.secondary,
                                indicatorColor = Color(0xFFFFFFFF),
                                unselectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                            )
                        )
                    }
                }
        }
    }
}

private class BottomBarItem(val title: String, val icon: Int, val path: String)