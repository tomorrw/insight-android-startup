package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.ProgressReport
import com.tomorrow.convenire.shared.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProgressReportUseCase : KoinComponent {
    private val repository: UserRepository by inject()

    @NativeCoroutines
    @Throws(Throwable::class)
    fun getMyProgress(): Flow<ProgressReport> = repository.getProgressReport()

    @NativeCoroutines
    fun refresh(): Flow<ProgressReport> = repository.refreshProgressReport()
}