package com.tomorrow.convenire.shared.domain.use_cases

import com.tomorrow.convenire.shared.domain.model.Email
import com.tomorrow.convenire.shared.domain.model.ResultIOS
import com.tomorrow.convenire.shared.domain.model.toResultIOS
import com.tomorrow.convenire.shared.domain.repositories.AuthenticationRepository
import com.tomorrow.convenire.shared.domain.utils.UUID
import com.tomorrow.convenire.shared.domain.utils.validator.models.Field
import com.tomorrow.convenire.shared.domain.utils.validator.models.getErrors
import com.tomorrow.convenire.shared.domain.utils.validator.models.getTransformed
import com.tomorrow.convenire.shared.domain.utils.validator.models.getTransformedOrNull
import com.tomorrow.convenire.shared.domain.utils.validator.models.rules.EmailRule
import com.tomorrow.convenire.shared.domain.utils.validator.models.rules.PhoneNumberRule
import com.tomorrow.convenire.shared.domain.utils.validator.models.rules.RequiredRule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterUseCase : KoinComponent {
    val repository: AuthenticationRepository by inject()

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ): Result<UUID> {
        val firstNameField = Field(firstName, "First Name", RequiredRule)
        val lastNameField = Field(lastName, "Last Name", RequiredRule)
        val phoneNumberField =
            Field(phoneNumber, "Phone Number", listOf(PhoneNumberRule(), RequiredRule))
        val emailField = Field(email, "Email", listOf(EmailRule()))

        listOf(firstNameField, lastNameField, phoneNumberField, emailField).getErrors()?.let {
            return Result.failure(it)
        }

        return repository.register(
            firstName = firstName,
            lastName = lastName,
            email = emailField.getTransformedOrNull(),
            phoneNumber = phoneNumberField.getTransformed()
        )
    }

    suspend fun registerIOS(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String
    ): ResultIOS<UUID, Throwable> = register(firstName, lastName, email, phoneNumber).toResultIOS()
}

