package com.tomorrow.convenire.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.convenire.shared.domain.model.Company
import com.tomorrow.convenire.shared.domain.repositories.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCompaniesByCategoryUseCase : KoinComponent {
    private val repository: CompanyRepository by inject()

    @NativeCoroutines
    fun getCompanies(categoryId: String): Flow<List<Company>> =
        repository
            .getCompanies()
            .map { companies -> companies.filter { it.categories.any { cat -> cat.id == categoryId } } }

    @NativeCoroutines
    fun refresh(categoryId: String): Flow<List<Company>> =
        repository
            .refreshCompanies()
            .map { companies -> companies.filter { it.categories.any { cat -> cat.id == categoryId } } }
}