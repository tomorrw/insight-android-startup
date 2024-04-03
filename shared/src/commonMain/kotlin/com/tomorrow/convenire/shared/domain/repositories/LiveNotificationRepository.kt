package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.domain.model.Notification

interface LiveNotificationRepository {
    fun startReceivingMessages(setMessage: (Result<Notification>) -> Unit)
    fun stopReceivingMessages()
}