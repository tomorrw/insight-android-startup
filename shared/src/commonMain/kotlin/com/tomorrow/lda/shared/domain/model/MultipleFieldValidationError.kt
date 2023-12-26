package com.tomorrow.lda.shared.domain.model

class MultipleFieldValidationError(
    val errors: Map<String, List<String>>,
    message: String = "fields are not valid"
) : Exception(message)

fun Throwable.toUserFriendlyError(): String = if (this is MultipleFieldValidationError) {
    this.errors.values.mapNotNull { it.firstOrNull() }.joinToString(", ")
} else this.message.let { if (!it.isNullOrBlank() && it.length < 50) it else "Something went wrong!" }