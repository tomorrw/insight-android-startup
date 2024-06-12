package com.tomorrow.mobile_starter_app.shared.data.data_source.remote

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.*
import com.tomorrow.kmmProjectStartup.domain.utils.UUID
import io.ktor.client.plugins.auth.providers.*

interface ApiService {
    suspend fun getUpdate(appPlatform: AppPlatformDTO): Result<UpdateInfoDTO>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String?,
        phoneNumber: String
    ): Result<UUID>

    suspend fun verifyOTP(
        email: String?,
        phoneNumber: String?,
        otp: String
    ): Result<Pair<BearerTokens, UserDTO>>

    suspend fun login(phoneNumber: String?, email: String?): Result<UUID>
    suspend fun logout(): Result<Unit>
    suspend fun getUser(): Result<UserDTO>
    suspend fun saveFCMToken(fcmToken: String): Result<Unit>
    suspend fun deleteFCMToken(fcmToken: String): Result<Unit>
    suspend fun addUnAuthenticatedInterceptor(intercept: suspend () -> Unit)
    suspend fun sendAuthWebsocket(socket: String, userId: String): Result<ApiServiceImplementation.AuthWebsocketResponse>
}