package com.tomorrow.mobile_starter_app.launch

import android.app.Application
import android.content.Context
import com.tomorrow.kmmProjectStartup.domain.model.AppConfig
import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform
import com.tomorrow.mobile_starter_app.BuildConfig
import com.tomorrow.mobile_starter_app.shared.di.initKoin
import com.tomorrow.mobile_starter_app.views.HomeViewModel
import com.tomorrow.mobile_starter_app.views.RegisterViewModel
import com.tomorrow.videoplayer.FullScreenViewModel
import com.tomorrow.videoplayer.VideoPlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.logger.Level
import org.koin.dsl.module

class MobileStarterApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MobileStarterApp)
            modules(appModule)
        }
    }

    companion object {
        val appModule = module {
            viewModel { VideoPlayerViewModel(androidApplication()) }
            single {
                AppConfig(
                    version = BuildConfig.VERSION_NAME,
                    name = androidContext().getAppName(),
                    platform = AppPlatform.Android,
                    updateUrl = androidContext().getPlayStoreLink()
                )
            }
            single { FullScreenViewModel() }
            single { RegisterViewModel() }
            viewModel { HomeViewModel() }
        }

        private fun Context.getPlayStoreLink() =
            "http://play.google.com/store/apps/details?id=${packageName}"

        private fun Context.getAppName(): String =
            this.applicationInfo.loadLabel(this.packageManager).toString()
    }
}