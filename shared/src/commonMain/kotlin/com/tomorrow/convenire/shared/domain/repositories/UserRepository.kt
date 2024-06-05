package com.tomorrow.convenire.shared.domain.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.ProgressReport
import com.tomorrow.convenire.shared.domain.model.SpeakerDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    @NativeCoroutines
    fun getProgressReport(): Flow<ProgressReport>

    @NativeCoroutines
    fun refreshProgressReport(): Flow<ProgressReport>
}