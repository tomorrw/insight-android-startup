package com.tomorrow.mobile_starter_app.shared.data.data_source.remote

import com.tomorrow.kmmProjectStartup.data.utils.BaseWebSocketService
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WebSocketServiceImplementation(
    private val httpClient: () -> HttpClient,
    private val baseUrl: String,
    private val port: Int,
    private val jsonConfig: Json,
) : KoinComponent, WebSocketService, BaseWebSocketService(
    clientProvider = httpClient,
    json = jsonConfig
) {

    val scope: CoroutineScope by inject()

    private val apiService: ApiService by inject()
}

@Serializable
data class Channel(
    val channel: String,
    val auth: String = ""
)