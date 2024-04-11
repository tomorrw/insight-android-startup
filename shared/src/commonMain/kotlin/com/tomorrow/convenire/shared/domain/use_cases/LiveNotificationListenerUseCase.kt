package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.Notification
import com.tomorrow.convenire.shared.domain.model.ResultIOS
import com.tomorrow.convenire.shared.domain.model.toResultIOS
import com.tomorrow.convenire.shared.domain.repositories.LiveNotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LiveNotificationListenerUseCase : KoinComponent {

    private val liveNotificationRepository: LiveNotificationRepository by inject()
    fun startListening(id: String, callback: (Result<Notification>) -> Unit) {
        liveNotificationRepository.startReceivingMessages(id) {
            callback(it)
        }
    }
    fun stopListening() {
        liveNotificationRepository.stopReceivingMessages()
    }

    fun startListeningIOS(id: String, callback: (ResultIOS<Notification, Throwable>) -> Unit) {
        liveNotificationRepository.startReceivingMessages(id) {
            callback(it.toResultIOS())
        }
    }
}