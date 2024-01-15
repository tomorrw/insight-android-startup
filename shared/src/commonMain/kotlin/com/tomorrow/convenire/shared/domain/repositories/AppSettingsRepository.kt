package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.data.data_source.model.AppstoreInfoDTO
import com.tomorrow.convenire.shared.domain.model.AppInfo
import com.tomorrow.convenire.shared.domain.model.AppPlatform
import com.tomorrow.convenire.shared.domain.model.UpdateInfo
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo>
    suspend fun getAppInfo(): AppInfo
}

