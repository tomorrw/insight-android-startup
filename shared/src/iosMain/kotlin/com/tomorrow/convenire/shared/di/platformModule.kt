package com.tomorrow.convenire.shared.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.tomorrow.convenire.shared.domain.model.AppConfig
import com.tomorrow.convenire.shared.domain.model.AppPlatform
import com.tomorrow.convenire.shared.domain.utils.AppStoreHelper
import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.runBlocking
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUserDefaults

actual fun platformModule() = module {
    single { Darwin.create() }

    single(createdAtStart = false) {
        runBlocking {
            val iosAppConfig = AppStoreHelper().getAppNameAndUpdateUrl(
                bundleId = NSBundle.mainBundle.infoDictionary?.get("CFBundleIdentifier" as NSString)
                    .toString()
            )
            AppConfig(
                version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString" as NSString)
                    .toString(),
                platform = AppPlatform.IOS,
                name = iosAppConfig?.name ?: "App",
                updateUrl = iosAppConfig?.updateUrl
            )
        }
    }

    single<ObservableSettings>(named(com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.SETTING_NAME)) {
        NSUserDefaultsSettings(
            delegate = NSUserDefaults(suiteName = com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.ENCRYPTED_DATABASE_NAME)
        )
    }
}


