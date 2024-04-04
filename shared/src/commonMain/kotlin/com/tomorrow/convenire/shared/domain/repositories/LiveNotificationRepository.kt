package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.domain.model.Notification

interface LiveNotificationRepository {
    fun startReceivingMessages(id: String, setMessage: (Result<Notification>) -> Unit)
    fun stopReceivingMessages()
}