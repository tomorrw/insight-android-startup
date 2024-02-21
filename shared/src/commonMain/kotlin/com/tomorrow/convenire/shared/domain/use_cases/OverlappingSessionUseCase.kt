package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.Session
import com.tomorrow.convenire.shared.domain.repositories.SessionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OverlappingSessionUseCase : KoinComponent {
    private val repository: SessionRepository by inject()

    fun getOverlapping(id: String): Session? {
        val sessionToCheckWith = GetSessionByIdUseCase().getCachedSession(id) ?: return null

        repository
            .eventsBookmarks()
            .filter { it.value }
            .forEach {
                GetSessionByIdUseCase().getCachedSession(it.key)
                    ?.let { s -> if (sessionToCheckWith.overlapsWithOtherSession(s)) return s }
            }

        return null
    }
}