package com.tomorrow.convenire.shared.domain.utils.validator.models

import com.tomorrow.convenire.shared.domain.model.MultipleFieldValidationError
import com.tomorrow.convenire.shared.domain.utils.validator.models.rules.RequiredRule
import com.tomorrow.convenire.shared.domain.utils.validator.models.rules.Rule
import com.tomorrow.convenire.shared.domain.utils.validator.models.rules.TransformativeRule
import com.tomorrow.convenire.shared.domain.utils.validator.values.lang.ValidationStrings

/**
 * by default all fields are not required
 * **/
open class Field<Original>(
    val value: Original?,
    val name: String,
    val rules: List<Rule>,
    private val language: ValidationStrings.Language = ValidationStrings.Language.English
) {
    constructor(
        value: Original?,
        name: String,
        rule: Rule,
        language: ValidationStrings.Language = ValidationStrings.Language.English
    ) : this(
        value,
        name,
        listOf(rule),
        language
    )


    val isValid: Boolean = value.validate(rules)

    fun getErrors(): List<String>? = if (isValid) null else rules
        .mapNotNull { it.getErrorMessageIfNotValid(value, language) }
        .map { error -> error(name) }
        .let { it.ifEmpty { null } }

    fun getFirstError(): String? =
        if (isValid) null else rules
            .firstOrNull { it.isNotValid(value) }
            ?.getErrorMessage(language)
            ?.invoke(name)


    private fun Original?.validate(rules: List<Rule>) =
        if (!rules.contains(RequiredRule) && RequiredRule.isNotValid(value)) true
        else rules.all { it.isValid(this) }
}


@Throws(MultipleFieldValidationError::class)
fun List<Field<*>>.getErrors(): MultipleFieldValidationError? {
    val errors = this.mapNotNull {
        it.getErrors()?.let { errors -> it.name to errors }
    }.toMap()

    if (errors.isNotEmpty()) return MultipleFieldValidationError(errors)
    return null
}

@Throws(Exception::class)
inline fun <reified Transformed> Field<*>.getTransformed(): Transformed =
    rules.filterIsInstance<TransformativeRule<*>>().firstOrNull()
        ?.getInstanceIfValid(value) as? Transformed
        ?: throw Exception("cannot transform this field to ${Transformed::class}")