package com.tomorrow.mobile_starter_app.shared.domain.use_cases.appSettings

import com.tomorrow.kmmProjectStartup.domain.model.AppConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAppConfigUseCase: KoinComponent {
    private val appConfig: AppConfig by inject()

    fun get(): AppConfig = appConfig
}