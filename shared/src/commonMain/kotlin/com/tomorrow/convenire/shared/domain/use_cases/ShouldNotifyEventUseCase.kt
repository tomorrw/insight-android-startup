package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ShouldNotifyEventUseCase : KoinComponent {
    private val repository: SessionRepository by inject()

    fun shouldNotify(id: String): Boolean = repository.isBookmarked(id)

    fun shouldNotifyFlow(id: String): Flow<Boolean> = repository.isBookmarkedFlow(id)

    @NativeCoroutines
    fun shouldNotifyFlow(listIds: List<String>): Flow<Map<String, Boolean>> =
        repository.isBookmarkedFlow(listIds)
}