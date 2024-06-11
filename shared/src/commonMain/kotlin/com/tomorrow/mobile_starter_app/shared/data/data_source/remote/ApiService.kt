package com.tomorrow.mobile_starter_app.shared.data.data_source.remote

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.*
import com.tomorrow.kmmProjectStartup.domain.utils.UUID
import io.ktor.client.plugins.auth.providers.*

interface ApiService {
    suspend fun getSpeakers(): Result<List<SpeakerDTO>>
    suspend fun getCompanies(): Result<List<CompanyDTO>>
    suspend fun getSessions(): Result<List<SessionDTO>>
    suspend fun getHome(): Result<HomeDataDTO>
    suspend fun getProgressReport(): Result<ProgressReportDTO>
    suspend fun hitPostUrl(url: String): Result<String>
    suspend fun getPost(id: String): Result<PostDTO>
    suspend fun askQuestion(eventId: String, question: String, isAnonymous: Boolean): Result<Unit>
    suspend fun getSession(id: String): Result<SessionDTO>
    suspend fun getSpeaker(id: String): Result<SpeakerDTO>
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
    suspend fun getSpinners(): Result<List<SpinnerDTO>>
    suspend fun getOffers(): Result<List<OfferDTO>>
    suspend fun getClaimedOffers(): Result<List<OfferDTO>>
    suspend fun getConfig(): Result<ConfigurationDTO>
    suspend fun saveFCMToken(fcmToken: String): Result<Unit>
    suspend fun deleteFCMToken(fcmToken: String): Result<Unit>
    suspend fun addUnAuthenticatedInterceptor(intercept: suspend () -> Unit)
    suspend fun sendAuthWebsocket(socket: String, userId: String): Result<ApiServiceImplementation.AuthWebsocketResponse>
}