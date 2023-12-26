package com.tomorrow.lda.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.lda.shared.domain.model.Company
import com.tomorrow.lda.shared.domain.repositories.CompanyRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCompaniesUseCase : KoinComponent {
    private val repository: CompanyRepository by inject()

    @NativeCoroutines
    fun getCompanies(): Flow<List<Company>> = repository.getCompanies()

    @NativeCoroutines
    fun refresh(): Flow<List<Company>> = repository.refreshCompanies()
}