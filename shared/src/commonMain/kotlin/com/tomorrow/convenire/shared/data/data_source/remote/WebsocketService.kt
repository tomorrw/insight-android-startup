package com.tomorrow.convenire.shared.data.data_source.remote


import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
interface WebSocketService {
    fun startListeningToQr(setMessage: (Result<String>) -> Unit)
    fun stopListeningToQr()
//    suspend fun DefaultClientWebSocketSession.onReceive(setMessage: (Result<String>) -> Unit)
}