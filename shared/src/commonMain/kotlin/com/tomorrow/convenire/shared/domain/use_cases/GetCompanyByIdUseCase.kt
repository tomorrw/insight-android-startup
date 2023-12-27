package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.repositories.CompanyRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCompanyByIdUseCase : KoinComponent {
    private val repository: CompanyRepository by inject()

    @NativeCoroutines
    fun getCompany(id: String): Flow<Company> = repository.getCompanyById(id)
}