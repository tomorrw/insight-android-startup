package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.tomorrow.mobile_starter_app.shared.domain.model.Notification

interface LiveNotificationRepository {
    suspend fun startReceivingMessages(id: String, setMessage: (Result<Notification>) -> Unit)
    suspend fun stopReceivingMessages()
}