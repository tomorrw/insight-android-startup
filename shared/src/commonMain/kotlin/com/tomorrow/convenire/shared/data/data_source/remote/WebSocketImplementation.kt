package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.BaseWebSocketService
import com.tomorrow.convenire.shared.domain.model.Notification
import io.ktor.client.HttpClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
    ) : KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
) {
    override fun startListeningToQr(setMessage: (Result<NotificationDTO>) -> Unit) {
        return startListening(setMessage, baseUrl, "wss/v2/1/demo/")
    }

    override fun stopListeningToQr() = stopListening("wss/v2/1/demo/")

}
