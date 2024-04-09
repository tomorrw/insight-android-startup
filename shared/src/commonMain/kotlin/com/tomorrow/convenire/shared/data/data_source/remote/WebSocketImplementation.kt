package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.BaseWebSocketService
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
    private val port: Int,
) : KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
) {

    override fun startListeningToQr(id: String, setMessage: (Result<NotificationDTO>) -> Unit) {
        return startListening(
            setMessage,
            baseUrl,
            "app/98a90d17c40bd9afc57a",
            port,
            SubscriptionDTO("pusher:subscribe", Channel("ticket-$id"))
        )
    }

    override fun stopListeningToQr() = stopListening("app/98a90d17c40bd9afc57a")

}

@Serializable
data class SubscriptionDTO(
    val event: String,
    val data: Channel
)

@Serializable
data class Channel(
    val channel: String
)