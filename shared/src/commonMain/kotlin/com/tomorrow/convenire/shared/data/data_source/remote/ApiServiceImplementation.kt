package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.model.*
import com.tomorrow.convenire.shared.data.data_source.utils.BaseApiService
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
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

    override suspend fun hitPostUrl(url: String): Result<String> = post<HitPostResponse>(url).map { it.message }

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
        setBody(RegisterRequest(firstName, lastName,  "", phoneNumber.replace(" ", "")))
    }.map { it.uuid }

    // this is the actual login once the otp is verified
    override suspend fun verifyOTP(email: String?,phoneNumber: String?,  otp: String): Result<Pair<BearerTokens, UserDTO>> =
        post<VerifyOTPResponse>("$baseUrl/api/login") {
            phoneNumber?.let {

                setBody(VerifyOTPRequest(it.replace(" ", "").replace("+", ""), otp))
            }
            email?.let {
                setBody(VerifyOTPRequestViaEmail(it, otp))
            }
        }.map { Pair(BearerTokens(accessToken = it.token, refreshToken = it.token), it.user) }

    // this is used to not make old users go through register again
    override suspend fun login(phoneNumber: String?, email: String?): Result<String> = post<LoginResponse>("$baseUrl/api/request-otp") {
        phoneNumber?.let {
            setBody(LoginRequest(it.replace(" ", "").replace("+", "")))
        }
        email?.let {
            setBody(LoginRequestViaEmail(it))
        }
    }.map { it.uuid }

    override suspend fun logout(): Result<Unit> = post("$baseUrl/api/logout")
    override suspend fun getUser(): Result<UserDTO> = get("$baseUrl/api/users")
    override suspend fun getSpinners(): Result<List<SpinnerDTO>> = get("$baseUrl/api/spinners")
    override suspend fun getOffers(): Result<List<OfferDTO>> = get("$baseUrl/api/offers")
    override suspend fun getClaimedOffers(): Result<List<OfferDTO>> = get("$baseUrl/api/offers/claimed")

    override suspend fun getTicket(id: Int): Result<QrCodeDataDTO> {
        return Result.success(
            QrCodeDataDTO(
                user = UserDTO(
                    id = "1",
                    uuid = "1",
                    name = "Any Name",
                    email = "any@gmail.com",
                    phoneNumber = "1234567890",
                    hasPaid = true,
            ),
                conventionName = "Title",
                conventionStartDate = "2021-09-01 08:30:00",
                conventionEndDate = "2021-09-02 08:30:00",
                ticketDescription = "Scan this QR code at the event and food court entrance",
            )
        )
    }
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
