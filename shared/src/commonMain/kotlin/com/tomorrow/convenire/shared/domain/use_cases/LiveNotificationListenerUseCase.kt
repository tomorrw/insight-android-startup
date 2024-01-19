package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.ResultIOS
import com.tomorrow.convenire.shared.domain.model.toResultIOS
import com.tomorrow.convenire.shared.domain.repositories.LiveNotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LiveNotificationListenerUseCase: KoinComponent {

    private val liveNotificationRepository: LiveNotificationRepository by inject()
    private var messages: Result<String> = Result.success("")
    private var callback: (() -> Unit)? = null
    fun startListening(  callback:() -> Unit) {
        liveNotificationRepository.startReceivingMessages { setMessage(it) }
        this.callback = callback
    }
    fun stopListening() {
        liveNotificationRepository.stopReceivingMessages()
    }
    fun getMessage(): Result<String> =  messages
    fun getMessageIOS(): ResultIOS<String, Throwable> = messages.toResultIOS()
    private fun setMessage(message: Result<String>) {
        this.messages = message
        callback?.invoke()
    }
}