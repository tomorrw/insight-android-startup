package com.tomorrow.mobile_starter_app.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.mobile_starter_app.shared.domain.model.Post
import com.tomorrow.mobile_starter_app.shared.domain.repositories.PostRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPostByIdUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    @NativeCoroutines
    fun getPost(id: String): Flow<Post> = repository.getPostById(id)
}