package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.tomorrow.mobile_starter_app.shared.domain.model.ColorTheme
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ColorThemeUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()

    fun getColorTheme() = repository.getColorTheme()

    fun setColorTheme(colorTheme: ColorTheme) {
        repository.setColorTheme(colorTheme)
    }
}