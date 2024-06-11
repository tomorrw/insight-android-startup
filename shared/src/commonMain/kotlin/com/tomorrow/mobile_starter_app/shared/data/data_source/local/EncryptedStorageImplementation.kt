package com.tomorrow.mobile_starter_app.shared.data.data_source.local

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import com.tomorrow.mobile_starter_app.shared.data.data_source.model.UserDTO
import com.tomorrow.mobile_starter_app.shared.di.BearerTokensContainer
import com.tomorrow.mobile_starter_app.shared.domain.model.ColorTheme
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EncryptedStorageImplementation(
    private val encryptedSettings: ObservableSettings,
    private val json: Json,
) : com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorage, KoinComponent {
    private val tokensContainer: BearerTokensContainer by inject()
    override var bearerTokens: BearerTokens?
        get() = encryptedSettings.getStringOrNull(com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.TOKEN_NAME).toBearerTokens()
        set(value) {
            encryptedSettings[com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.TOKEN_NAME] = json.encodeToString(value?.toBearerTokenSerializable())
            tokensContainer.scope.close()
        }

    override var user: UserDTO?
        get() = encryptedSettings.getStringOrNull(com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.USER).toUserDTO()
        set(value) {
            encryptedSettings[com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.USER] = json.encodeToString(value)
        }

    override var colorTheme: ColorTheme?
        get() = encryptedSettings.getStringOrNull(com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.COLOR_THEME).toColorTheme()
        set(value) { encryptedSettings[com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.COLOR_THEME] = value.toString() }

    override var fcmToken: String?
        get() = encryptedSettings.getStringOrNull(com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.FCM_TOKEN)
        set(value) { encryptedSettings[com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.Companion.FCM_TOKEN] = value ?: "" }

    @Serializable
    private class BearerTokenSerializable(val token: String, val refreshToken: String) {
        fun toBearerTokens() = BearerTokens(accessToken = token, refreshToken = refreshToken)
    }

    private fun BearerTokens.toBearerTokenSerializable() =
        com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.BearerTokenSerializable(
            this.accessToken,
            this.refreshToken
        )

    private fun String?.toUserDTO(): UserDTO? = if (!this.isNullOrBlank()) try {
        json.decodeFromString<UserDTO>(this)
    } catch (e: Exception) {
        null
    } else null

    private fun String?.toColorTheme(): ColorTheme? = if (!this.isNullOrBlank()) try {
        json.decodeFromString<ColorTheme>(this)
    } catch (e: Exception) {
        null
    } else null

    private fun String?.toBearerTokens(): BearerTokens? =
        if (!this.isNullOrBlank()) try {
            json.decodeFromString<com.tomorrow.mobile_starter_app.shared.data.data_source.local.EncryptedStorageImplementation.BearerTokenSerializable>(this).toBearerTokens()
        } catch (e: Exception) {
            null
        } else null

    companion object {
        const val ENCRYPTED_DATABASE_NAME = "ENCRYPTED_DATABASE"
        const val SETTING_NAME = "ENCRYPTED_SETTING"
        const val TOKEN_NAME = "TOKEN"
        const val USER = "USER"
        const val COLOR_THEME = "COLOR_THEME"
        const val FCM_TOKEN = "FCM_TOKEN"
    }
}
