package com.tomorrow.mobile_starter_app
.shared.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.qualifier.named
import org.koin.dsl.module
import io.ktor.client.engine.okhttp.*

actual fun platformModule() = module {
    single { OkHttp.create() }

    single<ObservableSettings>(named(com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.SETTING_NAME)) {
        SharedPreferencesSettings(
            EncryptedSharedPreferences.create(
                get(),
                com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.ENCRYPTED_DATABASE_NAME,
                MasterKey.Builder(get())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ), false
        )
    }
}