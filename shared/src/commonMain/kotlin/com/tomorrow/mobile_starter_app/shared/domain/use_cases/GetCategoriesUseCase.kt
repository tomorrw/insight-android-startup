package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.model.Company
import com.tomorrow.mobile_starter_app.shared.domain.repositories.CompanyRepository
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