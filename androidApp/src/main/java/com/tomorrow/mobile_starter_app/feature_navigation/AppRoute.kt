package com.tomorrow.mobile_starter_app.feature_navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tomorrow.components.others.Loader
import com.tomorrow.mobile_starter_app.shared.di.Constants
import com.tomorrow.mobile_starter_app.views.AskLectureQuestionView
import com.tomorrow.mobile_starter_app.views.CategoryView
import com.tomorrow.mobile_starter_app.views.ClaimedOffers
import com.tomorrow.mobile_starter_app.views.CompaniesByMap
import com.tomorrow.mobile_starter_app.views.CompaniesView
import com.tomorrow.mobile_starter_app.views.CompanyView
import com.tomorrow.mobile_starter_app.views.DailyLecturesView
import com.tomorrow.mobile_starter_app.views.EventDetailsView
import com.tomorrow.mobile_starter_app.views.ExhibitionsView
import com.tomorrow.mobile_starter_app.views.HomeView
import com.tomorrow.mobile_starter_app.views.LecturesView
import com.tomorrow.mobile_starter_app.views.LoginView
import com.tomorrow.mobile_starter_app.views.MyLecturesView
import com.tomorrow.mobile_starter_app.views.MyProgressView
import com.tomorrow.mobile_starter_app.views.MyQrView
import com.tomorrow.mobile_starter_app.views.OTPView
import com.tomorrow.mobile_starter_app.views.OffersAndDeals
import com.tomorrow.mobile_starter_app.views.OnBoardingView
import com.tomorrow.mobile_starter_app.views.PostView
import com.tomorrow.mobile_starter_app.views.ProductCategoriesView
import com.tomorrow.mobile_starter_app.views.ProfileView
import com.tomorrow.mobile_starter_app.views.RegisterView
import com.tomorrow.mobile_starter_app.views.SettingsView
import com.tomorrow.mobile_starter_app.views.SpeakerDetailView
import com.tomorrow.mobile_starter_app.views.SpeakersView
import com.tomorrow.navigation.Route
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDate


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

    object Lectures : AppRoute(
        path = "lectures",
        component = { LecturesView() },
        shouldDisplayBottomBar = true
    )

    object AskLectureQuestion : AppRoute(
        path = "lectures",
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
        component = { it.arguments?.getString("id")?.let { id -> AskLectureQuestionView(id) } }
    ) {
        fun generateExplicit(id: String) = generate(id)
    }

    object DailyLectures :
        AppRoute(
            path = "daily-lectures",
            arguments = listOf(navArgument("date") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }),
            component = {
                val date = it.arguments?.getString("date")?.toLocalDate()?.toJavaLocalDate()
                DailyLecturesView(date)
            },
            shouldDisplayBottomBar = true
        ) {
        fun generateExplicit(date: String) =
            generate(nullableParams = listOf(Route.NullableParam("date", date)))
    }

    object MyLectures : AppRoute(
        path = "my-lectures",
        component = { MyLecturesView() },
        shouldDisplayBottomBar = true
    )

    object Exhibitions : AppRoute(
        path = "exhibitions",
        component = { ExhibitionsView() },
        shouldDisplayBottomBar = true
    )

    object CompaniesByMap :
        AppRoute(
            path = "exhibitions/symposium",
            component = { CompaniesByMap() },
        )

    object ProductCategories :
        AppRoute(
            path = "exhibitions/categories",
            component = { ProductCategoriesView() },
            shouldDisplayBottomBar = true
        )

    object OffersAndDeals :
        AppRoute(
            path = "exhibitions/offers",
            component = { OffersAndDeals() },
            shouldDisplayBottomBar = true
        )

    object ClaimedOffers :
        AppRoute(
            path = "exhibitions/claimed-offers",
            component = { ClaimedOffers() },
            shouldDisplayBottomBar = true
        )

    object ProductCategory : AppRoute(
        path = "exhibitions/category",
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
        component = { it.arguments?.getString("id")?.let { id -> CategoryView(id) } },
        shouldDisplayBottomBar = true
    ) {
        fun generateExplicit(id: String) = generate(id)
    }

    object Companies : AppRoute(
        path = "exhibitions/companies",
        component = { CompaniesView() },
        shouldDisplayBottomBar = true
    )

    object Company : AppRoute(
        path = "company",
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
        component = { it.arguments?.getString("id")?.let { id -> CompanyView(id) } }
    ) {
        fun generateExplicit(id: String) = generate(id)
    }

    object Speaker : AppRoute(
        path = "speaker",
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
        component = { it.arguments?.getString("id")?.let { id -> SpeakerDetailView(id) } }
    ) {
        fun generateExplicit(id: String) = generate(id)
    }

    object EventDetail : AppRoute(
        path = "event",
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
        component = { it.arguments?.getString("id")?.let { id -> EventDetailsView(id) } }
    ) {
        fun generateExplicit(id: String) = generate(id)
    }

    object Post : AppRoute(
        path = "post",
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
        component = { it.arguments?.getString("id")?.let { id -> PostView(id) } }
    ) {
        fun generateExplicit(id: String) = generate(id)
    }


    object MyQr : AppRoute(
        path = "my-qr",
        component = { MyQrView() },
        shouldDisplayBottomBar = true
    )

    object MyProgress : AppRoute(
        path = "progress",
        component = { MyProgressView() },
        shouldDisplayBottomBar = true
    )

    object Profile : AppRoute(
        path = "profile",
        component = { ProfileView() },
        shouldDisplayBottomBar = true
    )

    object Settings : AppRoute(
        path = "profile/settings",
        component = { SettingsView() },
        shouldDisplayBottomBar = true
    )

    object Speakers : AppRoute(
        path = "lectures/speakers",
        component = { SpeakersView() },
        shouldDisplayBottomBar = true
    )

    object LoaderPage : AppRoute(
        path = "loader",
        component = { Loader() },
        shouldBeAuthenticated = false
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
