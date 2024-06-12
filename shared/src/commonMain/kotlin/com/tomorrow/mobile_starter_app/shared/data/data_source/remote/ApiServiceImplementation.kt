package com.tomorrow.mobile_starter_app.shared.data.data_source.remote

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.*
import com.tomorrow.kmmProjectStartup.data.utils.BaseApiService
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
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
    data class RegisterResponse(
        val uuid: String,
    )
}
