package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.mobile_starter_app.shared.domain.model.User
import com.tomorrow.kmmProjectStartup.domain.model.toResultIOS
import com.tomorrow.mobile_starter_app.shared.domain.repositories.AuthenticationRepository
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.Field
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.getErrors
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.getTransformed
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.EmailRule
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.OTPRule
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.PhoneNumberRule
import com.tomorrow.kmmProjectStartup.domain.utils.validator.models.rules.RequiredRule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VerifyOTPUseCase : KoinComponent {
    val repository: AuthenticationRepository by inject()

    suspend fun verify(phoneNumber: String?, email: String?, otp: String): Result<User> {
        val otpField = Field(otp, "OTP", listOf(RequiredRule, OTPRule()))
        val emailField: Field<String> =
            Field(email, "Email", EmailRule())
        val phoneNumberField: Field<String> =
            Field(phoneNumber, "Phone Number", PhoneNumberRule())

        listOf(otpField, emailField, phoneNumberField).getErrors()
            ?.let { return Result.failure(it) }

        phoneNumber?.let {
            return repository.verify(
                phoneNumber = phoneNumberField.getTransformed(),
                otpField.getTransformed()
            )
        }
        return repository.verify(email = emailField.getTransformed(), otpField.getTransformed())
    }

    suspend fun verifyIOS(
        phoneNumber: String?,
        email: String?,
        otp: String
    ): ResultIOS<User, Throwable> = verify(phoneNumber, email, otp).toResultIOS()
}

