package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.repositories.SessionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ToggleShouldNotifyEventUseCase : KoinComponent {
    private val repository: SessionRepository by inject()

    @NativeCoroutines
    suspend fun toggleShouldNotify(id: String) = repository.toggleShouldNotify(id)
}