package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.tomorrow.mobile_starter_app.shared.domain.model.Action
import com.tomorrow.kmmProjectStartup.domain.model.ResultIOS
import com.tomorrow.kmmProjectStartup.domain.model.toResultIOS
import com.tomorrow.mobile_starter_app.shared.domain.repositories.PostRepository
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