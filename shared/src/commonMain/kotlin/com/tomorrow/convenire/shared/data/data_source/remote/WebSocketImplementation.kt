package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.utils.BaseWebSocketService
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent

class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
) : KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
    counter = 0
) {
    override fun startListeningToQr(setMessage: (Result<String>) -> Unit) =
        startListening(setMessage, baseUrl,"wss/v2/1/demo/")

    override fun stopListeningToQr() = stopListening("wss/v2/1/demo/")

}
