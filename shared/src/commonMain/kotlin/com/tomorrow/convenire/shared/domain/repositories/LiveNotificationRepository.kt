package com.tomorrow.convenire.shared.domain.repositories

interface LiveNotificationRepository {
    fun startReceivingMessages(setMessage: (Result<String>) -> Unit)
    fun stopReceivingMessages()
}