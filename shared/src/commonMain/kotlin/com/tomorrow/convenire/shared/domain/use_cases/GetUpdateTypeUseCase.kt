package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.kmmProjectStartup.domain.model.AppConfig
import com.tomorrow.convenire.shared.domain.repositories.AppSettingsRepository
import com.tomorrow.kmmProjectStartup.domain.model.Version
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUpdateTypeUseCase : KoinComponent {
    private val appConfig: AppConfig by inject()
    private val repository: AppSettingsRepository by inject()

    suspend fun getUpdateType(): Result<UpdateType> =
        repository.getUpdateInfo(appConfig.platform).map {
            val currentVersion: Version = try {
                Version(appConfig.version)
            } catch (e: Exception) {
                return@map UpdateType.Forced
            }

            if (it.lastSupportedVersion <= currentVersion) return@map UpdateType.None

            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            //                 a                    b                    c
            //             No Update     [     Soft update     [     Hard Update
            //        -----------------------------------------------------------------> time

            // c
            if (now >= it.hardUpdateDate) return@map UpdateType.Forced

            // b
            if (now < it.hardUpdateDate && now >= it.softUpdateDate) return@map UpdateType.Flexible

            // a
            UpdateType.None
        }

    @Throws(Exception::class)
    suspend fun getType(): UpdateType = getUpdateType().getOrThrow()

    enum class UpdateType {
        Forced, Flexible, None,
    }
}

