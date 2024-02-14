package com.tomorrow.convenire.launch

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.tomorrow.convenire.BuildConfig
import com.tomorrow.convenire.feature_video.VideoPlayerViewModel
import com.tomorrow.convenire.shared.di.initKoin
import com.tomorrow.convenire.shared.domain.model.AppConfig
import com.tomorrow.convenire.shared.domain.model.AppPlatform
import com.tomorrow.convenire.views.AskLectureQuestionViewModel
import com.tomorrow.convenire.views.CategoryViewModel
import com.tomorrow.convenire.views.ClaimedOffersViewModel
import com.tomorrow.convenire.views.CompaniesByCategoryViewModel
import com.tomorrow.convenire.views.CompaniesByMapViewModel
import com.tomorrow.convenire.views.CompaniesViewModel
import com.tomorrow.convenire.views.CompanyViewModel
import com.tomorrow.convenire.views.DailyLecturesViewModel
import com.tomorrow.convenire.views.EventDetailsViewModel
import com.tomorrow.convenire.views.ExhibitionsViewModel
import com.tomorrow.convenire.views.HomeViewModel
import com.tomorrow.convenire.views.MyLecturesViewModel
import com.tomorrow.convenire.views.MyProfileViewModel
import com.tomorrow.convenire.views.MyQrViewModel
import com.tomorrow.convenire.views.NewsArticleViewModel
import com.tomorrow.convenire.views.OffersAndDealsViewModel
import com.tomorrow.convenire.views.ProductCategoriesViewModel
import com.tomorrow.convenire.views.RegisterViewModel
import com.tomorrow.convenire.views.SpeakerDetailViewModel
import com.tomorrow.convenire.views.SpeakersViewModel
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