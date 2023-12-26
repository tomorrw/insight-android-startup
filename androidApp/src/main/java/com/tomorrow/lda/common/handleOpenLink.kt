package com.tomorrow.lda.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import com.tomorrow.lda.feature_navigation.AppRoute

fun handleLink(url: String, navController: NavController, context: Context) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url),
    )
    val appRoute = AppRoute.getNormalRouteFromDeepLink(url)

    if (appRoute != null) try {
        navController.navigate(appRoute)
    } catch (e: Throwable) {
        context.startActivity(intent)
    }
    else context.startActivity(intent)
}