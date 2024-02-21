package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import kotlinx.coroutines.flow.Flow

interface SpeakerRepository {
    fun getSpeakerById(id: String): Flow<SpeakerDetail>

    fun getSpeakers(): Flow<List<SpeakerDetail>>

    fun refreshSpeakers(): Flow<List<SpeakerDetail>>
}