package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.model.AppPlatformDTO
import com.tomorrow.convenire.shared.data.data_source.model.CompanyDTO
import com.tomorrow.convenire.shared.data.data_source.model.ConfigurationDTO
import com.tomorrow.convenire.shared.data.data_source.model.HomeDataDTO
import com.tomorrow.convenire.shared.data.data_source.model.OfferDTO
import com.tomorrow.convenire.shared.data.data_source.model.PostDTO
import com.tomorrow.convenire.shared.data.data_source.model.ProgressReportDTO
import com.tomorrow.convenire.shared.data.data_source.model.SessionDTO
import com.tomorrow.convenire.shared.data.data_source.model.SpeakerDTO
import com.tomorrow.convenire.shared.data.data_source.model.SpinnerDTO
import com.tomorrow.convenire.shared.data.data_source.model.UpdateInfoDTO
import com.tomorrow.convenire.shared.data.data_source.model.UserDTO
import com.tomorrow.convenire.shared.domain.utils.UUID
import io.ktor.client.plugins.auth.providers.BearerTokens

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