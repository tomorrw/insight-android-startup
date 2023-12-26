package com.tomorrow.lda.launch

import android.app.Application
import com.tomorrow.lda.BuildConfig
import com.tomorrow.lda.feature_video.VideoPlayerViewModel
import com.tomorrow.lda.shared.di.initKoin
import com.tomorrow.lda.shared.domain.model.AppConfig
import com.tomorrow.lda.shared.domain.model.AppPlatform
import com.tomorrow.lda.views.AskLectureQuestionViewModel
import com.tomorrow.lda.views.CategoryViewModel
import com.tomorrow.lda.views.ClaimedOffersViewModel
import com.tomorrow.lda.views.CompaniesByCategoryViewModel
import com.tomorrow.lda.views.CompaniesByMapViewModel
import com.tomorrow.lda.views.CompaniesViewModel
import com.tomorrow.lda.views.CompanyViewModel
import com.tomorrow.lda.views.DailyLecturesViewModel
import com.tomorrow.lda.views.EventDetailsViewModel
import com.tomorrow.lda.views.HomeViewModel
import com.tomorrow.lda.views.MyLecturesViewModel
import com.tomorrow.lda.views.MyProfileViewModel
import com.tomorrow.lda.views.MyQrViewModel
import com.tomorrow.lda.views.NewsArticleViewModel
import com.tomorrow.lda.views.OffersAndDealsViewModel
import com.tomorrow.lda.views.ProductCategoriesViewModel
import com.tomorrow.lda.views.RegisterViewModel
import com.tomorrow.lda.views.SpeakerDetailViewModel
import com.tomorrow.lda.views.SpeakersViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.logger.Level
import org.koin.dsl.module

class BIDMApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@BIDMApp)
            modules(appModule)
        }
    }

    companion object {
        val appModule = module {
            viewModel { VideoPlayerViewModel(androidApplication()) }
            single { AppConfig(BuildConfig.VERSION_NAME, AppPlatform.Android) }
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
    }
}