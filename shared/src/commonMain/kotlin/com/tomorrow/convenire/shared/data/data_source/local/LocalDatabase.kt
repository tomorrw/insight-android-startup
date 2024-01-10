package com.tomorrow.convenire.shared.data.data_source.local

import com.tomorrow.convenire.shared.data.data_source.model.CompanyDTO
import com.tomorrow.convenire.shared.data.data_source.model.HomeDataDTO
import com.tomorrow.convenire.shared.data.data_source.model.SessionDTO
import com.tomorrow.convenire.shared.data.data_source.model.SpeakerDTO
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

    fun getSpeakers(): Result<List<SpeakerDTO>>
    fun getSessions(): Result<List<SessionDTO>>
    fun getCompanies(): Result<List<CompanyDTO>>
    fun getHomeResponse(): Result<HomeDataDTO>

    fun isBookmarked(id: String): Boolean
    fun isBookmarkedFlow(id: String): Flow<Boolean>
    fun isBookmarkedFlow(listIds: List<String>): Flow<Map<String, Boolean>>
    fun eventsBookmarks(): Map<String, Boolean>
    suspend fun toggleShouldNotify(id: String)

    fun lastUpdatedSpeakers(): Instant?
    fun lastUpdatedSessions(): Instant?
    fun lastUpdatedCompanies(): Instant?
    fun lastUpdatedHomeResponse(): Instant?

    suspend fun clearData()
}