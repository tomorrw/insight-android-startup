package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.data.data_source.local.EncryptedStorageImplementation
import com.tomorrow.convenire.shared.domain.model.ColorTheme
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ColorThemeUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()

    fun getColorTheme(): ColorTheme {
    return repository.getColorTheme()
    }


    fun setColorTheme(colorTheme: ColorTheme) {
        repository.saveColorTheme(colorTheme.toString())
    }

}