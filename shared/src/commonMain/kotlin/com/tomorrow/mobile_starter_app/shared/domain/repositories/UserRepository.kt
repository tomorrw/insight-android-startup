package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.model.ProgressReport
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    @NativeCoroutines
    fun getProgressReport(): Flow<ProgressReport>

    @NativeCoroutines
    fun refreshProgressReport(): Flow<ProgressReport>
}