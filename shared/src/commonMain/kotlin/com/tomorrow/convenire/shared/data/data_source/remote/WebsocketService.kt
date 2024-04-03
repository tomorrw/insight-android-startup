package com.tomorrow.convenire.shared.data.data_source.remote


import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.domain.model.Notification

interface WebSocketService {
    fun startListeningToQr(setMessage: (Result<NotificationDTO>) -> Unit)
    fun stopListeningToQr()
}