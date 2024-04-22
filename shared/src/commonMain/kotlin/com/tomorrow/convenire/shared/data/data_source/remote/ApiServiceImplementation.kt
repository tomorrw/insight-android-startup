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
import com.tomorrow.convenire.shared.data.data_source.utils.BaseApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent


class ApiServiceImplementation(
    clientProvider: () -> HttpClient,
    private val baseUrl: String,
) : KoinComponent, ApiService, BaseApiService(clientProvider) {
    override suspend fun getSpeakers(): Result<List<SpeakerDTO>> = get("$baseUrl/api/speakers")
    override suspend fun getCompanies(): Result<List<CompanyDTO>> = get("$baseUrl/api/companies")
    override suspend fun getSessions(): Result<List<SessionDTO>> = get("$baseUrl/api/events")
    override suspend fun getHome(): Result<HomeDataDTO> = get("$baseUrl/api/home")
    override suspend fun getProgressReport(): Result<ProgressReportDTO> = get("$baseUrl/api/progress-report")

    override suspend fun hitPostUrl(url: String): Result<String> =
        post<HitPostResponse>(url).map { it.message }

    override suspend fun getPost(id: String): Result<PostDTO> = get("$baseUrl/api/posts/$id")

    override suspend fun askQuestion(
        eventId: String,
        question: String,
        isAnonymous: Boolean
    ): Result<Unit> = post("$baseUrl/api/events/$eventId/questions") {
        setBody(LectureQuestionBody(content = question, isAnonymous = isAnonymous))
    }

    override suspend fun getSession(id: String): Result<SessionDTO> = get("$baseUrl/api/events/$id")
    override suspend fun getUpdate(appPlatform: AppPlatformDTO): Result<UpdateInfoDTO> =
        get("$baseUrl/api/versions/${appPlatform.name}")

    override suspend fun register(
        firstName: String, lastName: String, email: String?, phoneNumber: String
    ): Result<String> = post<RegisterResponse>("$baseUrl/api/register") {
        setBody(RegisterRequest(firstName, lastName, "", phoneNumber.replace(" ", "")))
    }.map { it.uuid }

    // this is the actual login once the otp is verified
    override suspend fun verifyOTP(
        email: String?,
        phoneNumber: String?,
        otp: String
    ): Result<Pair<BearerTokens, UserDTO>> =
        post<VerifyOTPResponse>("$baseUrl/api/login") {
            phoneNumber?.let {

                setBody(VerifyOTPRequest(it.replace(" ", "").replace("+", ""), otp))
            }
            email?.let {
                setBody(VerifyOTPRequestViaEmail(it, otp))
            }
        }.map { Pair(BearerTokens(accessToken = it.token, refreshToken = it.token), it.user) }

    // this is used to not make old users go through register again
    override suspend fun login(phoneNumber: String?, email: String?): Result<String> =
        post<LoginResponse>("$baseUrl/api/request-otp") {
            phoneNumber?.let {
                setBody(LoginRequest(it.replace(" ", "").replace("+", "")))
            }
            email?.let {
                setBody(LoginRequestViaEmail(it))
            }
        }.map { it.uuid }

    val logoutUrl = "$baseUrl/api/logout"
    override suspend fun logout(): Result<Unit> = post(logoutUrl)
    override suspend fun getUser(): Result<UserDTO> = get("$baseUrl/api/users")
    override suspend fun getSpinners(): Result<List<SpinnerDTO>> = get("$baseUrl/api/spinners")
    override suspend fun getOffers(): Result<List<OfferDTO>> = get("$baseUrl/api/offers")
    override suspend fun getClaimedOffers(): Result<List<OfferDTO>> =
        get("$baseUrl/api/offers/claimed")

    override suspend fun getConfig(): Result<ConfigurationDTO> = get("$baseUrl/api/configuration")
    override suspend fun saveFCMToken(fcmToken: String): Result<Unit> =
        post("$baseUrl/api/fcm-tokens") {
            setBody(FCMTokensRequest(fcmToken))
        }

    override suspend fun deleteFCMToken(fcmToken: String): Result<Unit> =
        post("$baseUrl/api/fcm-tokens/unlink") {
            setBody(FCMTokensRequest(fcmToken))
        }

    override suspend fun addUnAuthenticatedInterceptor(intercept: suspend () -> Unit) {
        clientProvider().receivePipeline.intercept(HttpReceivePipeline.After) {
            if (it.status == HttpStatusCode.Unauthorized
                && !it.request.url.toString().contains(logoutUrl)
            )
                intercept()
        }
    }

    override suspend fun sendAuthWebsocket(socketId: String, userId: String) = post<AuthWebsocketResponse>(
        "$baseUrl/broadcasting/auth"
    ) {
        setBody(
            WebsocketAuthRequest(
                socketId = socketId,
                channelName = "private-App.Models.User.$userId"
            )
        )
    }


    @Serializable
    private data class WebsocketAuthRequest(
        @SerialName("socket_id")
        val socketId: String,
        @SerialName("channel_name")
        val channelName: String,
    )
    @Serializable
    data class AuthWebsocketResponse(
        @SerialName("auth")
        val authToken: String
    )

    @Serializable
    private data class FCMTokensRequest(
        val token: String
    )

    @Serializable
    private data class VerifyOTPResponse(
        val token: String, val user: UserDTO
    )

    @Serializable
    private data class ResendOTPRequest(
        val uuid: String,
    )

    @Serializable
    private data class LoginRequest(
        @SerialName("phone_number")
        val phoneNumber: String,
    )

    @Serializable
    private data class LoginRequestViaEmail(
        val email: String,
    )

    @Serializable
    private data class LoginResponse(
        val uuid: String
    )

    @Serializable
    private data class VerifyOTPRequest(
        @SerialName("phone_number") val phoneNumber: String,
        val otp: String,
    )

    @Serializable
    private data class VerifyOTPRequestViaEmail(
        val email: String,
        val otp: String,
    )

    @Serializable
    private data class RegisterRequest(
        @SerialName("first_name") val firstName: String,
        @SerialName("last_name") val lastName: String,
        val email: String?,
        @SerialName("phone_number") val phoneNumber: String,
    )

    @Serializable
    data class LectureQuestionBody(
        @SerialName("content")
        val content: String,
        @SerialName("is_anonymous")
        val isAnonymous: Boolean
    )

    @Serializable
    data class RegisterResponse(
        val uuid: String,
    )

    @Serializable
    data class HitPostResponse(
        val message: String,
    )
}
