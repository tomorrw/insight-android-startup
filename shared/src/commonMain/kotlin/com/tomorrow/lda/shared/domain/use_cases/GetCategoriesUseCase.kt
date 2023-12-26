package com.tomorrow.lda.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.lda.shared.domain.model.Company
import com.tomorrow.lda.shared.domain.repositories.CompanyRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCategoriesUseCase : KoinComponent {
    private val repository: CompanyRepository by inject()

    @NativeCoroutines
    fun getCategories(): Flow<List<Company.Category>> = repository.getCategories()

    @NativeCoroutines
    fun refresh(): Flow<List<Company.Category>> = repository.refreshCategories()
}