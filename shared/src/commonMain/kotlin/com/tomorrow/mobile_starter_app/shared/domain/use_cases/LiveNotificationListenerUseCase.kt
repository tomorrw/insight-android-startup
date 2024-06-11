package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.tomorrow.mobile_starter_app.shared.domain.model.Notification
import com.tomorrow.mobile_starter_app.shared.domain.repositories.LiveNotificationRepository
import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.kmmProjectStartup.domain.model.toResultIOS
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LiveNotificationListenerUseCase : KoinComponent {

    private val liveNotificationRepository: LiveNotificationRepository by inject()
    suspend fun startListening(id: String, callback: (Result<Notification>) -> Unit) {
        liveNotificationRepository.startReceivingMessages(id) {
            callback(it)
        }
    }
    suspend fun stopListening() {
        liveNotificationRepository.stopReceivingMessages()
    }

    suspend fun startListeningIOS(id: String, callback: (ResultIOS<Notification, Throwable>) -> Unit) {
        liveNotificationRepository.startReceivingMessages(id) {
            callback(it.toResultIOS())
        }
    }
}