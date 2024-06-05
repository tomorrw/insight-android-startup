package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.model.NotificationDataDTO
import com.tomorrow.convenire.shared.data.data_source.utils.BaseWebSocketService
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
    private val port: Int,
) : KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
) {
    private val apiService: ApiService by inject()
    private var firstTime = true

    override suspend fun startListeningToQr(
        id: String,
        setMessage: (Result<NotificationDTO>) -> Unit
    ) {
        firstTime = true
        return this.startListening(
            interceptToAuthenticate(
                id,
                setMessage,
                onAuthentication = { authToken ->
                    sendMessage(
                        SubscriptionDTO(
                            "pusher:subscribe",
                            Channel(
                                "private-App.Models.User.$id",
                                authToken
                            )
                        ),
                        baseUrl,
                        "app/98a90d17c40bd9afc57a",
                        port
                    )
                }
            ),
            baseUrl,
            "app/98a90d17c40bd9afc57a",
            port
        )
    }

    private fun interceptToAuthenticate(
        userId: String,
        setMessage: (Result<NotificationDTO>) -> Unit,
        onAuthentication: suspend (authToken: String) -> Unit,
    ): (Result<NotificationDTO>) -> Unit =
        { message ->
            message.onSuccess { msg ->
                if (firstTime) {
                    val data = json.decodeFromString<NotificationDataDTO>(msg.data ?: "{}")
                    data.socketId?.let {
                        scope.launch {
                            apiService.sendAuthWebsocket(it, userId = userId).onSuccess { auth ->
                                onAuthentication(auth.authToken)
                                firstTime = false
                            }
                        }

                    }
                } else setMessage(message)
            }
        }

    override suspend fun stopListeningToQr() = stopListening("app/98a90d17c40bd9afc57a")

}

@Serializable
data class SubscriptionDTO(
    val event: String,
    val data: Channel
)

@Serializable
data class Channel(
    val channel: String,
    val auth: String = ""
)