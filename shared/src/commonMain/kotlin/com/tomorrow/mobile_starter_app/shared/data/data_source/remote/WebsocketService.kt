package com.tomorrow.mobile_starter_app.shared.data.data_source.remote


import com.tomorrow.mobile_starter_app.shared.data.data_source.model.NotificationDTO

interface WebSocketService {
    suspend fun startListeningToQr(id: String, setMessage: (Result<NotificationDTO>) -> Unit)
    suspend fun stopListeningToQr()
}