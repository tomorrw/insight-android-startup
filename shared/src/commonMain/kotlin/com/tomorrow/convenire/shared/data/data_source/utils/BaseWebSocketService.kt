package com.tomorrow.convenire.shared.data.data_source.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseWebSocketService(
    val clientProvider: () -> HttpClient,
) : KoinComponent {
    val scope: CoroutineScope by inject()
    val json: Json by inject()
    var webSocketSession: HashMap<String, DefaultClientWebSocketSession> = HashMap()
    inline fun <reified Model> startListening(
        crossinline setMessage: (Result<Model>) -> Unit,
        baseUrl: String,
        path: String
    ) {
        scope.launch {
            try {
                clientProvider().wss(
                    host = baseUrl,
                    path = path,
                ) {
                    val incomingMessages = launch { onReceive(setMessage) }
                    webSocketSession[path] = this
                    incomingMessages.join()
                }
            } catch (e: Throwable) {
            }
        }
    }

    open fun stopListening(path: String) {
        scope.launch {
            webSocketSession[path]?.close()
        }
    }

    suspend inline fun <reified Model> DefaultClientWebSocketSession.onReceive(setMessage: (Result<Model>) -> Unit) {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val response = message.readText()
                val model = json.decodeFromString<Model>(response)
                setMessage(Result.success(model))
            }
        } catch (e: Throwable) {
            setMessage(Result.failure(e))

        }
    }
}