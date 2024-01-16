package com.tomorrow.convenire.shared.domain.repositories


import com.tomorrow.convenire.shared.domain.model.AppPlatform
import com.tomorrow.convenire.shared.domain.model.UpdateInfo

interface AppSettingsRepository {
    suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo>
}

