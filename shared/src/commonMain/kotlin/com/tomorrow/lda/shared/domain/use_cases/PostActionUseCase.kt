package com.tomorrow.lda.shared.domain.use_cases

import com.tomorrow.lda.shared.domain.model.Action
import com.tomorrow.lda.shared.domain.model.ResultIOS
import com.tomorrow.lda.shared.domain.model.toResultIOS
import com.tomorrow.lda.shared.domain.repositories.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PostActionUseCase : KoinComponent {
    val repository: PostRepository by inject()

    suspend fun handle(action: Action): Result<String> =
        if (action.type == Action.ActionType.ApiHit) repository.hitPostUrl(action.url)
        else Result.failure(Exception("only an action of type Post.ActionType.ApiHit is supported by PostActionUseCase"))

    suspend fun handleIos(action: Action): ResultIOS<String, Throwable> =
        handle(action).toResultIOS()
}