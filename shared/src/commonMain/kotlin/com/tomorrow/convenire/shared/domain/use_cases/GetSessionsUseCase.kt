package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.Session
import com.tomorrow.convenire.shared.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSessionsUseCase : KoinComponent {
    private val repository: SessionRepository by inject()

    @NativeCoroutines
    @Throws(Throwable::class)
    fun getSessions(): Flow<List<Session>> = repository.getSessions()

    @NativeCoroutines
    fun refresh(): Flow<List<Session>> = repository.refreshSessions()
}