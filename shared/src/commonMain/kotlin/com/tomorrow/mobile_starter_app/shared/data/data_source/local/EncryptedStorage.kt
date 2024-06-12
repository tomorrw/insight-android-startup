package com.tomorrow.mobile_starter_app.shared.data.data_source.local

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.UserDTO
import com.tomorrow.mobile_starter_app.shared.domain.model.ColorTheme
import io.ktor.client.plugins.auth.providers.*

interface EncryptedStorage {
    var bearerTokens: BearerTokens?
    var user: UserDTO?
    var colorTheme: ColorTheme?
    var fcmToken: String?
}