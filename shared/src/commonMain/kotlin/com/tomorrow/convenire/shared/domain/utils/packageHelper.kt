package com.tomorrow.convenire.shared.domain.utils

import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.kmmProjectStartup.domain.model.fold
import com.tomorrow.kmmProjectStartup.domain.model.onFailure
import com.tomorrow.kmmProjectStartup.domain.model.onSuccess
import com.tomorrow.kmmProjectStartup.domain.model.toUserFriendlyError
import com.tomorrow.kmmProjectStartup.domain.use_cases.CompareStringsUseCase

var CompareStringsUseCase = CompareStringsUseCase

fun Throwable.toUserFriendlyError() = this.toUserFriendlyError()

fun <S, F : Throwable> ResultIOS<S, F>.fold(onSuccess: (S) -> Unit, onFailure: (F) -> Unit) = this.fold(onSuccess, onFailure)

fun <S, F : Throwable> ResultIOS<S, F>.onFailure(callback: (F) -> Unit) = this.onFailure(callback)

fun <S, F : Throwable> ResultIOS<S, F>.onSuccess(callback: (S) -> Unit) = this.onSuccess(callback)
