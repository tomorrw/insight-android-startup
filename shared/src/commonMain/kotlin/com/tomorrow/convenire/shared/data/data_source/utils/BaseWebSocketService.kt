package com.tomorrow.convenire.shared.data.data_source.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.ws
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
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
    inline fun <reified Model, reified Message> startListening(
        crossinline setMessage: (Result<Model>) -> Unit,
        baseUrl: String,
        path: String,
        port: Int,
        message: Message?,
    ) {
        scope.launch {
            try {
                clientProvider().wss(
                    host = baseUrl,
                    path = path,
//                    port = port
                ) {
                    val incomingMessages = launch {
                        message?.let {
                            sendMessage(
                                message,
                                baseUrl,
                                path,
                                port
                            )
                        }
                        onReceive(setMessage)
                    }
                    webSocketSession[path] = this
                    incomingMessages.join()
                }
            } catch (e: Throwable) {
                setMessage(Result.failure(e))
            }
        }
    }

    open fun stopListening(path: String) {
        scope.launch {
            webSocketSession[path]?.close()
        }
    }

    inline fun <reified Model> sendMessage(
        message: Model,
        baseUrl: String,
        path: String,
        port: Int,
    ) {
        scope.launch {
            try {
                if (webSocketSession[path]?.isActive == true) {
                    webSocketSession[path]?.sendSerialized(message)
                } else
                    clientProvider().wss(
                        host = baseUrl,
                        path = path,
//                        port = port
                    ) {
                        launch {
                            sendSerialized(message)
                        }
                    }

            } catch (e: Throwable) {
                throw e
            }
        }
    }

    suspend inline fun <reified Model> DefaultClientWebSocketSession.onReceive(setMessage: (Result<Model>) -> Unit) {
        try {
            for (message in incoming) {
                val s = this.receiveDeserialized<Model>()
                setMessage(Result.success(s))
            }
        } catch (e: Throwable) {
            setMessage(Result.failure(e))
        }
    }
}