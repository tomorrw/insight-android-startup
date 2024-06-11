package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.kmmProjectStartup.domain.model.toResultIOS
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import com.tomorrow.kmmProjectStartup.domain.utils.UUID
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.Field
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.getErrors
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.getTransformed
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.EmailRule
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.PhoneNumberRule
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.RequiredRule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class LoginUseCase : KoinComponent {
    private val repository: AuthenticationRepository by inject()
    suspend fun login(phoneNumber: String?, email: String?): Result<UUID>? {
        phoneNumber?.let {
            return loginViaPhoneNumber(it)
        }
        email?.let {
            return loginViaEmail(it)
        }
        return null
    }

    private suspend fun loginViaPhoneNumber(phoneNumber: String): Result<UUID> {

        val phoneNumberField =
            Field(phoneNumber, "Phone Number", listOf(PhoneNumberRule(), RequiredRule))

        listOf(phoneNumberField).getErrors()?.let {
            return Result.failure(it)
        }
        return repository.login(phoneNumber = phoneNumberField.getTransformed())

    }

    private suspend fun loginViaEmail(email: String): Result<UUID> {

        val emailField =
            Field(email, "Email", listOf(EmailRule(), RequiredRule))

        listOf(emailField).getErrors()?.let {
            return Result.failure(it)
        }

        return repository.login(email = emailField.getTransformed())

    }

    suspend fun loginIOS(phoneNumber: String?, email: String?): ResultIOS<UUID, Throwable>? =
        login(phoneNumber, email)?.toResultIOS()

}

