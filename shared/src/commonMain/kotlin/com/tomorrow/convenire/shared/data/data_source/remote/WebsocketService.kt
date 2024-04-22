package com.tomorrow.convenire.shared.data.data_source.remote


import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO

interface WebSocketService {
    suspend fun startListeningToQr(id: String, setMessage: (Result<NotificationDTO>) -> Unit)
    suspend fun stopListeningToQr()
}