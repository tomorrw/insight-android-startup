package com.tomorrow.lda.shared.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import com.tomorrow.lda.shared.data.data_source.local.EncryptedStorageImplementation
import io.ktor.client.engine.android.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule() = module {
    single { Android.create() }
    single<ObservableSettings>(named(EncryptedStorageImplementation.SETTING_NAME)) {
        SharedPreferencesSettings(
            EncryptedSharedPreferences.create(
                get(),
                EncryptedStorageImplementation.ENCRYPTED_DATABASE_NAME,
                MasterKey.Builder(get())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ), false
        )
    }
}