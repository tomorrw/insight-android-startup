package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.domain.model.Notification

interface LiveNotificationRepository {
    suspend fun startReceivingMessages(id: String, setMessage: (Result<Notification>) -> Unit)
    suspend fun stopReceivingMessages()
}