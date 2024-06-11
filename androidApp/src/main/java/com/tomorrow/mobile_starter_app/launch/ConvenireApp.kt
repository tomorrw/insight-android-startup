package com.tomorrow.mobile_starter_app.launch

import android.app.Application
import android.content.Context
import com.tomorrow.mobile_starter_app.BuildConfig
import com.tomorrow.mobile_starter_app.shared.di.initKoin
import com.tomorrow.kmmProjectStartup.domain.model.AppConfig
import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform
import com.tomorrow.mobile_starter_app.views.AskLectureQuestionViewModel
import com.tomorrow.mobile_starter_app.views.CategoryViewModel
import com.tomorrow.mobile_starter_app.views.ClaimedOffersViewModel
import com.tomorrow.mobile_starter_app.views.CompaniesByCategoryViewModel
import com.tomorrow.mobile_starter_app.views.CompaniesByMapViewModel
import com.tomorrow.mobile_starter_app.views.CompaniesViewModel
import com.tomorrow.mobile_starter_app.views.CompanyViewModel
import com.tomorrow.mobile_starter_app.views.DailyLecturesViewModel
import com.tomorrow.mobile_starter_app.views.EventDetailsViewModel
import com.tomorrow.mobile_starter_app.views.ExhibitionsViewModel
import com.tomorrow.mobile_starter_app.views.HomeViewModel
import com.tomorrow.mobile_starter_app.views.MyLecturesViewModel
import com.tomorrow.mobile_starter_app.views.MyProfileViewModel
import com.tomorrow.mobile_starter_app.views.MyProgressViewModel
import com.tomorrow.mobile_starter_app.views.MyQrViewModel
import com.tomorrow.mobile_starter_app.views.NewsArticleViewModel
import com.tomorrow.mobile_starter_app.views.OffersAndDealsViewModel
import com.tomorrow.mobile_starter_app.views.ProductCategoriesViewModel
import com.tomorrow.mobile_starter_app.views.RegisterViewModel
import com.tomorrow.mobile_starter_app.views.SpeakerDetailViewModel
import com.tomorrow.mobile_starter_app.views.SpeakersViewModel
import com.tomorrow.videoplayer.FullScreenViewModel
import com.tomorrow.videoplayer.VideoPlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.logger.Level
import org.koin.dsl.module

class ConvenireApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ConvenireApp)
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
            viewModel { DailyLecturesViewModel() }
            viewModel { SpeakersViewModel() }
            viewModel { CompaniesByMapViewModel() }
            viewModel { CompaniesViewModel() }
            viewModel { MyLecturesViewModel() }
            viewModel { ProductCategoriesViewModel() }
            viewModel { MyQrViewModel() }
            viewModel { ExhibitionsViewModel() }
            viewModel { MyProgressViewModel() }
            viewModel { ClaimedOffersViewModel() }
            viewModel { OffersAndDealsViewModel() }
            viewModel { MyProfileViewModel() }
            viewModel { AskLectureQuestionViewModel(it[0]) }
            viewModel { CompanyViewModel(it[0]) }
            viewModel { CategoryViewModel(it[0]) }
            viewModel { NewsArticleViewModel(it[0]) }
            viewModel { EventDetailsViewModel(it[0]) }
            viewModel { SpeakerDetailViewModel(it[0]) }
            viewModel { CompaniesByCategoryViewModel(it[0]) }
        }

        private fun Context.getPlayStoreLink() =
            "http://play.google.com/store/apps/details?id=${packageName}"

        private fun Context.getAppName(): String =
            this.applicationInfo.loadLabel(this.packageManager).toString()
    }
}