package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.kmmProjectStartup.domain.model.AppConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAppConfigUseCase: KoinComponent {
    private val appConfig: AppConfig by inject()

    fun get(): AppConfig = appConfig
}