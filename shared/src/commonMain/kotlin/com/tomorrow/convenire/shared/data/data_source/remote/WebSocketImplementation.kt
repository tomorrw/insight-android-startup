package com.tomorrow.convenire.shared.data.data_source.remote

import com.tomorrow.convenire.shared.data.data_source.utils.BaseWebSocketService
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
    private var counter: Int = 0
): KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
    baseUrl = baseUrl,
    counter = counter
) {
    override fun startListeningToQr(setMessage: (Result<String>) -> Unit) {
        startListening(
            setMessage,
            ""
        )
    }
    override fun stopListeningToQr() = stopListening("")

}
