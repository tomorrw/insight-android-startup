package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform
import com.tomorrow.kmmProjectStartup.domain.model.UpdateInfo
import com.tomorrow.mobile_starter_app.shared.domain.model.ColorTheme
import kotlinx.coroutines.flow.StateFlow

interface AppSettingsRepository {
    suspend fun getUpdateInfo(appPlatform: AppPlatform): Result<UpdateInfo>
    fun getColorTheme(): StateFlow<ColorTheme>
    fun setColorTheme(colorTheme: ColorTheme): Result<String>
    fun saveFCMToken(fcmToken: String?): Result<String>
}

