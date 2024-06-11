package com.tomorrow.mobile_starter_app.shared.domain.repositories

import com.tomorrow.mobile_starter_app.shared.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostById(id: String): Flow<Post>
    suspend fun hitPostUrl(url: String): Result<String>
}

