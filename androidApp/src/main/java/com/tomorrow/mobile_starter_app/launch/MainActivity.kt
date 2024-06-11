package com.tomorrow.mobile_starter_app.launch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.IsAuthenticatedUseCase
import com.tomorrow.mobile_starter_app.shared.domain.use_cases.SaveFCMToken

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition { IsAuthenticatedUseCase().isAuthenticated() == null }

        super.onCreate(savedInstanceState)

        try {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                val token = task.result
                SaveFCMToken().saveFCMToken(token).onFailure {
                    Log.e("FirebaseAnalytics", "failed saving fcm token $it")
                }
            }
        } catch (e: Throwable) {
            Log.e("FirebaseAnalytics", "failed initializing firebase analytics $e")
        }

        setContent {
            val navController = rememberNavController()
            DisposableEffect(Unit) {
                val listener = Consumer<Intent> {
                    try {
                        if (it.action != Intent.ACTION_VIEW) return@Consumer
                        else it.data?.let { link -> navController.navigate(link) }
                    } catch (_: Exception) {
                    }
                }
                val routeListener =
                    NavController.OnDestinationChangedListener { _, destination, _ ->
                        try {
                            val params = Bundle()
                            params.putString(
                                FirebaseAnalytics.Param.SCREEN_NAME,
                                destination.label as String?
                            )
                            params.putString(
                                FirebaseAnalytics.Param.SCREEN_CLASS,
                                destination.label as String?
                            )

                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
                        } catch (e: Throwable) {
                            Log.e("FirebaseAnalytics", "failed logging route change $e")
                        }
                    }

                navController.addOnDestinationChangedListener(routeListener)
                addOnNewIntentListener(listener)

                onDispose {
                    removeOnNewIntentListener(listener)
                    navController.removeOnDestinationChangedListener(routeListener)
                }
            }
            AppView(navController)
        }
    }
}

