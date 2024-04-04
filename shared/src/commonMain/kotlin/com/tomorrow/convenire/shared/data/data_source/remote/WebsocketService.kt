package com.tomorrow.convenire.shared.data.data_source.remote


import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO

interface WebSocketService {
    fun startListeningToQr(id: String, setMessage: (Result<NotificationDTO>) -> Unit)
    fun stopListeningToQr()
}