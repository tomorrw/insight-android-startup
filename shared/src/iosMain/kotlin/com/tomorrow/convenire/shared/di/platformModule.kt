package com.tomorrow.convenire.shared.di

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.tomorrow.convenire.shared.data.data_source.model.AppstoreInfoDTO
import com.tomorrow.convenire.shared.domain.model.AppConfig
import com.tomorrow.convenire.shared.domain.model.AppPlatform
import com.tomorrow.convenire.shared.domain.repositories.AppSettingsRepository
import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUserDefaults

actual fun platformModule() = module {
    single { Darwin.create() }

    single {
        AppConfig(
            NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString" as NSString)
                .toString(),
            AppPlatform.IOS
        )
    }

    single<ObservableSettings>(named(com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.SETTING_NAME)) {
        NSUserDefaultsSettings(
            delegate = NSUserDefaults(suiteName = com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.ENCRYPTED_DATABASE_NAME)
        )
    }
}

class AppleInfo : KoinComponent {
    private val appSettings: AppSettingsRepository by inject()


    @Throws(Exception::class)
    suspend fun getAppleInfo(bundleId: String): AppstoreInfoDTO? {
        return appSettings.getAppleInfo(bundleId)
    }

}
