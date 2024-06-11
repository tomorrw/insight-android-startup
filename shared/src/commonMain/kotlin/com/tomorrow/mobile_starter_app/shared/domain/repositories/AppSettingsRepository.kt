package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform
import com.tomorrow.kmmProjectStartup.domain.model.UpdateInfo

interface AppSettingsRepository {
    suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo>
}

