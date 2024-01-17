package com.tomorrow.convenire.shared.domain.utils.validator.models.rules

abstract class TransformativeRule<T> : Rule() {
    abstract fun getInstanceIfValid(value: Any?): T?
}