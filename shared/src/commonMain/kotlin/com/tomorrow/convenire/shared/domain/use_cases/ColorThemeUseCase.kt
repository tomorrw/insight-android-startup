package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.ColorTheme
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ColorThemeUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()

    fun getColorTheme() = repository.getColorTheme()

    fun setColorTheme(colorTheme: ColorTheme) {
        repository.setColorTheme(colorTheme)
    }
}