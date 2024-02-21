package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import com.tomorrow.convenire.shared.domain.repositories.SpeakerRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSpeakersUseCase : KoinComponent {
    private val repository: SpeakerRepository by inject()

    @NativeCoroutines
    fun getSpeakers(): Flow<List<SpeakerDetail>> = repository.getSpeakers()

    @NativeCoroutines
    fun refresh(): Flow<List<SpeakerDetail>> = repository.refreshSpeakers()
}