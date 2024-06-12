package com.tomorrow.mobile_starter_app.feature_navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tomorrow.mobile_starter_app.shared.di.Constants
import com.tomorrow.mobile_starter_app.views.HomeView
import com.tomorrow.mobile_starter_app.views.LoginView
import com.tomorrow.mobile_starter_app.views.OTPView
import com.tomorrow.mobile_starter_app.views.OnBoardingView
import com.tomorrow.mobile_starter_app.views.RegisterView
import com.tomorrow.navigation.Route


sealed class AppRoute(
    override val path: String,
    override val arguments: List<NamedNavArgument>? = null,
    override val deepLinkPrefixes: List<String> = AppRoute.deepLinkPrefixes ,
    override val component: @Composable (params: NavBackStackEntry) -> Unit,
    val shouldBeAuthenticated: Boolean = true,
    val shouldDisplayBottomBar: Boolean = false,
) : Route {
    object OnBoarding : AppRoute(
        path = "on-boarding",
        component = { OnBoardingView() },
        shouldBeAuthenticated = false
    )

    object Register : AppRoute(
        path = "register",
        arguments = listOf(
            navArgument("focusPhoneNumber") {
                type = NavType.BoolType
                defaultValue = false
            }
        ),
        component = {
            val shouldFocusNumber = it.arguments?.getBoolean("focusPhoneNumber")
            RegisterView(shouldFocusNumber)
        },
        shouldBeAuthenticated = false
    ) {
        fun generateExplicit(focusPhoneNumber: Boolean) = generate(focusPhoneNumber)
    }

    object Login : AppRoute(
        path = "login",
        component = { LoginView() },
        shouldBeAuthenticated = false
    )

    object OTP : AppRoute(
        path = "otp",
        component = { OTPView() },
        shouldBeAuthenticated = false
    )

    object Home : AppRoute(
        path = "home",
        component = { HomeView() },
        shouldDisplayBottomBar = true
    )

    companion object {
        private val deepLinkPrefixes: List<String> = listOf(
            Constants.APP_DEEP_LINK_BASE_URL,
            "app://convenire"
        )

        fun getNormalRouteFromDeepLink(deepLink: String): String? {
            val prefix = deepLinkPrefixes.map { "$it/" }.firstOrNull { deepLink.contains(it) } ?: return null
            return deepLink.substringAfter(prefix)
        }

        val allRoutes: List<AppRoute> =
            AppRoute::class.sealedSubclasses.mapNotNull { it.objectInstance }

        private fun fromString(string: String?): AppRoute? =
            if (string.isNullOrBlank()) null
            else allRoutes.find { it.getFullPath() == string }

        fun shouldDisplayBottomBar(string: String?): Boolean =
            fromString(string)?.shouldDisplayBottomBar == true

        fun shouldBeAuthenticated(string: String?): Boolean =
            fromString(string)?.shouldBeAuthenticated == true
    }
}
