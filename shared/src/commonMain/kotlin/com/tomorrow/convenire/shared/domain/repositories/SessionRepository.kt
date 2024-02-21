package com.tomorrow.convenire.shared.domain.repositories

import com.tomorrow.convenire.shared.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessionById(id: String): Flow<Session>

    @Throws(Throwable::class)
    fun getSessions(): Flow<List<Session>>

    fun getCachedSessionById(id: String): Session?

    fun refreshSessions(): Flow<List<Session>>

    fun isBookmarked(id: String): Boolean

    fun isBookmarkedFlow(id: String): Flow<Boolean>

    fun isBookmarkedFlow(listIds: List<String>): Flow<Map<String, Boolean>>

    fun eventsBookmarks(): Map<String, Boolean>

    suspend fun toggleShouldNotify(id: String)

    suspend fun askQuestion(eventId: String, question: String, isAnonymous: Boolean): Boolean
}