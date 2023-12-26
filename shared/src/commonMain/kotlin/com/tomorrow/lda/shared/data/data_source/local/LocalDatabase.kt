package com.tomorrow.lda.shared.data.data_source.local

import com.tomorrow.lda.shared.data.data_source.model.CompanyDTO
import com.tomorrow.lda.shared.data.data_source.model.HomeDataDTO
import com.tomorrow.lda.shared.data.data_source.model.SessionDTO
import com.tomorrow.lda.shared.data.data_source.model.SpeakerDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

interface LocalDatabase {
    @Throws(SerializationException::class, CancellationException::class)
    suspend fun replaceSpeakers(apiResult: List<SpeakerDTO>)

    @Throws(SerializationException::class, CancellationException::class)
    suspend fun replaceSessions(apiResult: List<SessionDTO>)

    @Throws(SerializationException::class, CancellationException::class)
    suspend fun replaceSession(session: SessionDTO)

    @Throws(SerializationException::class, CancellationException::class)
    suspend fun replaceCompanies(apiResult: List<CompanyDTO>)

    @Throws(SerializationException::class, CancellationException::class)
    suspend fun replaceHomeResponse(apiResult: HomeDataDTO)

    fun getSpeakers(): List<SpeakerDTO>?
    fun getSessions(): List<SessionDTO>?
    fun getCompanies(): List<CompanyDTO>?
    fun getHomeResponse(): HomeDataDTO?

    fun isBookmarked(id: String): Boolean
    fun isBookmarkedFlow(id: String): Flow<Boolean>
    fun isBookmarkedFlow(listIds: List<String>): Flow<Map<String, Boolean>>
    fun eventsBookmarks(): Map<String, Boolean>
    suspend fun toggleShouldNotify(id: String)

    suspend fun lastUpdatedSpeakers(): Instant?
    suspend fun lastUpdatedSessions(): Instant?
    suspend fun lastUpdatedCompanies(): Instant?
    suspend fun lastUpdatedHomeResponse(): Instant?

    suspend fun clearData()
}