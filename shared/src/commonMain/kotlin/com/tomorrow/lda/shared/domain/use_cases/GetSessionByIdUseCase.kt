package com.tomorrow.lda.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.lda.shared.domain.model.Session
import com.tomorrow.lda.shared.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSessionByIdUseCase : KoinComponent {
    private val repository: SessionRepository by inject()

    @NativeCoroutines
    fun getSession(id: String): Flow<Session> = repository.getSessionById(id)

    fun getCachedSession(id: String): Session? = repository.getCachedSessionById(id)
}