package com.tomorrow.mobile_starter_app
.shared.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import com.tomorrow.kmmProjectStartup.domain.model.AppConfig
import com.tomorrow.kmmProjectStartup.domain.model.AppPlatform
import com.tomorrow.kmmProjectStartup.domain.utils.AppStoreHelper
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
            val iosAppConfig = AppStoreHelper(
                engine = get(),
                json = get()
            ).getAppNameAndUpdateUrl(
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

    single<ObservableSettings>(named(com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.SETTING_NAME)) {
        NSUserDefaultsSettings(
            delegate = NSUserDefaults(suiteName = com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.ENCRYPTED_DATABASE_NAME)
        )
    }
}


