package com.tomorrow.lda.shared.domain.use_cases

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.tomorrow.lda.shared.domain.model.Post
import com.tomorrow.lda.shared.domain.repositories.PostRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPostByIdUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    @NativeCoroutines
    fun getPost(id: String): Flow<Post> = repository.getPostById(id)
}