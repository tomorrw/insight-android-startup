package com.tomorrow.lda.shared.domain.model

sealed class ResultIOS<out S, out F : Throwable> {
    data class Success<out S, out F : Throwable>(val value: S) : ResultIOS<S, F>()
    data class Failure<out S, out F : Throwable>(val failure: F) : ResultIOS<S, F>()
}

fun <S, F : Throwable> ResultIOS<S, F>.fold(onSuccess: (S) -> Unit, onFailure: (F) -> Unit) {
    when (this) {
        is ResultIOS.Success -> onSuccess(value)
        is ResultIOS.Failure -> onFailure(failure)
    }
}

fun <S, F : Throwable> ResultIOS<S, F>.onFailure(callback: (F) -> Unit) {
    if (this is ResultIOS.Failure) callback(failure)
}

fun <S, F : Throwable> ResultIOS<S, F>.onSuccess(callback: (S) -> Unit) {
    if (this is ResultIOS.Success) callback(value)
}

inline fun <reified M> Result<M>.toResultIOS(): ResultIOS<M, Throwable> =
    this.getOrElse { return ResultIOS.Failure(it) }.let { ResultIOS.Success(it) }
