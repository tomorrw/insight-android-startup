package com.tomorrow.convenire.shared.data.data_source.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseWebSocketService(
    private val clientProvider: () -> HttpClient,
    private var counter: Int = 0

) : KoinComponent {
    val scope: CoroutineScope by inject()
    private var webSocketSession: HashMap<String, DefaultClientWebSocketSession> = HashMap()
    fun startListening(setMessage: (Result<String>) -> Unit, baseUrl: String, path: String) {
        scope.launch {
            try {
                clientProvider().wss(
                    host = baseUrl,
                    path = path
                ) {
                    val incomingMessages = launch { onReceive(setMessage) }
                    webSocketSession[path] = this
                    incomingMessages.join()
                }
            } catch (e: Throwable) {
                println("=============== Error while listening: ${e.message}")
//                throw e
            }
        }
    }

    open fun stopListening(path: String) {
        scope.launch {
            webSocketSession[path]?.close()
        }
    }

    private suspend fun DefaultClientWebSocketSession.onReceive(setMessage: (Result<String>) -> Unit) {
        try {
            for (message in incoming) {
                counter += 1
                if (counter == 5) return
                message as? Frame.Text ?: continue
                setMessage(Result.success(message.readText()))
            }
        } catch (e: Throwable) {
            println("=============== Error while receiving: ${e.message}")
        }
    }
}