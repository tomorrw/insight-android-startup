package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.AppConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAppConfig: KoinComponent {
    val appConfig: AppConfig by inject()
}