package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.repositories.SessionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AskQuestionUseCase : KoinComponent {
    private val repository: SessionRepository by inject()

    @NativeCoroutines
    suspend fun askQuestion(eventId: String, question: String, isAnonymous: Boolean): Boolean =
        repository.askQuestion(eventId, question, isAnonymous)
}