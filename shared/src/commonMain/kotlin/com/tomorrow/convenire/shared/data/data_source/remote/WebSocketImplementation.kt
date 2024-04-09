package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.BaseWebSocketService
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
) : KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
) {

    override fun startListeningToQr(id: String, setMessage: (Result<NotificationDTO>) -> Unit) {
        return startListening(
            setMessage,
            "free.blr2.piesocket.com",
            "v3/1?api_key=h2mgwfuVp3BC1lEqEzs0P8dvnaf3TwPfq2rcNPzO&notify_self=1",
            6001,
            SubscriptionDTO("pusher:subscribe", Channel("ticket-$id"))
        )
    }

    override fun stopListeningToQr() = stopListening("v3/1?api_key=h2mgwfuVp3BC1lEqEzs0P8dvnaf3TwPfq2rcNPzO&notify_self=1")

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