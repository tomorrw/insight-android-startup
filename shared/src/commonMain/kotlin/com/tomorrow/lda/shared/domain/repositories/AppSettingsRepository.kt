package com.tomorrow.lda.shared.domain.repositories

import com.tomorrow.lda.shared.domain.model.AppPlatform
import com.tomorrow.lda.shared.domain.model.UpdateInfo

interface AppSettingsRepository {
    suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo>
}

