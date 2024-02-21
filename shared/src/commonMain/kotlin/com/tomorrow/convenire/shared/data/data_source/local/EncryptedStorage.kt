package com.tomorrow.convenire.shared.data.data_source.local

import com.tomorrow.convenire.shared.data.data_source.model.UserDTO
import com.tomorrow.convenire.shared.domain.model.ColorTheme
import io.ktor.client.plugins.auth.providers.*

interface EncryptedStorage {
    var bearerTokens: BearerTokens?
    var user: UserDTO?
    var colorTheme: ColorTheme?
    var fcmToken: String?
}