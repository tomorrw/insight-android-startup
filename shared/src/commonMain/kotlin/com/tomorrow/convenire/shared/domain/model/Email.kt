package com.tomorrow.convenire.shared.domain.model

class Email private constructor(val value: String) {
    companion object {
        private val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")


        @Throws(IllegalArgumentException::class)
        operator fun invoke(emailString: String): Email =
            if (!emailString.matches(emailRegex)) throw IllegalArgumentException("email must be valid")
            else Email(emailString)
    }
}
