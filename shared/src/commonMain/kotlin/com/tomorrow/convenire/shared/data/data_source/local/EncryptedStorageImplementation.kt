package com.tomorrow.convenire.shared.data.data_source.local

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import com.tomorrow.convenire.shared.data.data_source.model.UserDTO
import com.tomorrow.convenire.shared.di.BearerTokensContainer
import io.ktor.client.plugins.auth.providers.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EncryptedStorageImplementation(
    private val encryptedSettings: ObservableSettings,
    private val json: Json,
) : com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorage, KoinComponent {
    private val tokensContainer: BearerTokensContainer by inject()
    override var bearerTokens: BearerTokens?
        get() = encryptedSettings.getStringOrNull(com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.Companion.TOKEN_NAME).toBearerTokens()
        set(value) {
            encryptedSettings[com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.Companion.TOKEN_NAME] = json.encodeToString(value?.toBearerTokenSerializable())
            tokensContainer.scope.close()
        }

    override var user: UserDTO?
        get() = encryptedSettings.getStringOrNull(com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.Companion.USER).toUserDTO()
        set(value) {
            encryptedSettings[com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.Companion.USER] = json.encodeToString(value)
        }

    @Serializable
    private class BearerTokenSerializable(val token: String, val refreshToken: String) {
        fun toBearerTokens() = BearerTokens(accessToken = token, refreshToken = refreshToken)
    }

    private fun BearerTokens.toBearerTokenSerializable() =
        com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.BearerTokenSerializable(
            this.accessToken,
            this.refreshToken
        )

    private fun String?.toUserDTO(): UserDTO? = if (this != null && this.isNotBlank()) try {
        json.decodeFromString<UserDTO>(this)
    } catch (e: Exception) {
        null
    } else null

    private fun String?.toBearerTokens(): BearerTokens? =
        if (this != null && this.isNotBlank()) try {
            json.decodeFromString<com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation.BearerTokenSerializable>(this).toBearerTokens()
        } catch (e: Exception) {
            null
        } else null

    companion object {
        const val ENCRYPTED_DATABASE_NAME = "ENCRYPTED_DATABASE"
        const val SETTING_NAME = "ENCRYPTED_SETTING"
        const val TOKEN_NAME = "TOKEN"
        const val USER = "USER"
    }
}
