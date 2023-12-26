package com.tomorrow.lda.shared.data.data_source.local

import com.tomorrow.lda.shared.data.data_source.model.CompanyDTO
import com.tomorrow.lda.shared.data.data_source.model.HomeDataDTO
import com.tomorrow.lda.shared.data.data_source.model.SessionDTO
import com.tomorrow.lda.shared.data.data_source.model.SpeakerDTO
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmSingleQuery
import io.realm.kotlin.query.find
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class LocalDatabaseImplementation(
    private val json: Json,
) : LocalDatabase {
    private val configuration: RealmConfiguration by lazy {
        RealmConfiguration.Builder(
            schema = setOf(
                RealmListSpeaker::class,
                RealmListSession::class,
                RealmListCompany::class,
                RealmHomeResponse::class,
                ShouldNotify::class
            )
        ).build()
    }

    private val realm: Realm by lazy {
        try {
            Realm.open(configuration)
        } catch (e: IllegalStateException) {
            // if the schema has changed and migration failed.
            // did not implement a migration strategy because we use this as a caching layer
            // and it is not of high importance to keep changes across updates
            Realm.deleteRealm(configuration)
            Realm.open(configuration)
        }
    }

    override suspend fun replaceSpeakers(apiResult: List<SpeakerDTO>) {
        realm.write { copyToRealm(apiResult.toRealm(), UpdatePolicy.ALL) }
    }

    override suspend fun replaceSessions(apiResult: List<SessionDTO>) {
        realm.write { copyToRealm(apiResult.toRealm(), UpdatePolicy.ALL) }
    }

    override suspend fun replaceSession(session: SessionDTO) {
        realm.write {
            var updatedAt: RealmInstant? = null
            getRealmSessions().find { updatedAt = it?.updatedAt }

            copyToRealm(((getSessions()?.filter { it.id != session.id }
                ?: listOf()) + session).toRealm(updatedAt), UpdatePolicy.ALL)
        }
    }

    override suspend fun replaceCompanies(apiResult: List<CompanyDTO>) {
        realm.write { copyToRealm(apiResult.toRealm(), UpdatePolicy.ALL) }
    }

    override suspend fun replaceHomeResponse(apiResult: HomeDataDTO) {
        realm.write { copyToRealm(apiResult.toRealm(), UpdatePolicy.ALL) }
    }

    override fun getSpeakers(): List<SpeakerDTO>? = getRealmSpeakers().find()?.toDTO()


    override fun getSessions(): List<SessionDTO>? = getRealmSessions().find()?.toDTO()


    override fun getCompanies(): List<CompanyDTO>? = getRealmCompanies().find()?.toDTO()


    override fun getHomeResponse(): HomeDataDTO? = getRealmHomeResponse().find()?.toDTO()

    override fun isBookmarked(id: String): Boolean =
        realm.query<ShouldNotify>("id = $0", id).first().find()?.isEnabled ?: false

    override fun eventsBookmarks(): Map<String, Boolean> = realm
        .query<ShouldNotify>()
        .find { result -> result.associate { it.id to it.isEnabled } }


    override fun isBookmarkedFlow(id: String): Flow<Boolean> = realm
        .query<ShouldNotify>("id = $0", id)
        .first()
        .asFlow()
        .map { it.obj?.isEnabled ?: false }

    override fun isBookmarkedFlow(listIds: List<String>): Flow<Map<String, Boolean>> =
        if (listIds.isEmpty()) flow {} else realm
            .query<ShouldNotify>(listIds.joinToString(" OR ") { "id == '$it'" })
            .find()
            .asFlow()
            .map { result -> result.list.associate { it.id to it.isEnabled } }


    override suspend fun toggleShouldNotify(id: String) {
        realm.write {
            copyToRealm(ShouldNotify().apply {
                this.id = id
                this.isEnabled = !isBookmarked(id)
            }, UpdatePolicy.ALL)
        }
    }

    override suspend fun lastUpdatedSpeakers(): Instant? =
        getRealmSpeakers().find()?.updatedAt?.toInstant()

    override suspend fun lastUpdatedSessions(): Instant? =
        getRealmSessions().find()?.updatedAt?.toInstant()

    override suspend fun lastUpdatedCompanies(): Instant? =
        getRealmCompanies().find()?.updatedAt?.toInstant()

    override suspend fun lastUpdatedHomeResponse(): Instant? =
        getRealmHomeResponse().find()?.updatedAt?.toInstant()

    override suspend fun clearData() = realm.write {
        delete(this.query<RealmListSpeaker>().find())
        delete(this.query<RealmListSession>().find())
        delete(this.query<RealmListCompany>().find())
        delete(this.query<RealmHomeResponse>().find())
        delete(this.query<ShouldNotify>().find())
    }

    private fun RealmInstant.toInstant(): Instant = Instant.fromEpochSeconds(this.epochSeconds)

    private fun getRealmSpeakers(): RealmSingleQuery<RealmListSpeaker> =
        realm.query<RealmListSpeaker>("uuid = $0", "RealmListSpeaker").first()

    private fun getRealmSessions(): RealmSingleQuery<RealmListSession> =
        realm.query<RealmListSession>("uuid = $0", "RealmListSession").first()

    private fun getRealmCompanies(): RealmSingleQuery<RealmListCompany> =
        realm.query<RealmListCompany>("uuid = $0", "RealmListCompany").first()

    private fun getRealmHomeResponse(): RealmSingleQuery<RealmHomeResponse> =
        realm.query<RealmHomeResponse>("uuid = $0", "RealmHomeResponse").first()

    private fun RealmListSpeaker.toDTO(): List<SpeakerDTO>? =
        this.payload?.let { json.decodeFromString(it) }

    private fun RealmListSession.toDTO(): List<SessionDTO>? =
        this.payload?.let { json.decodeFromString(it) }

    private fun RealmListCompany.toDTO(): List<CompanyDTO>? =
        this.payload?.let { json.decodeFromString(it) }

    private fun RealmHomeResponse.toDTO(): HomeDataDTO? =
        this.payload?.let { json.decodeFromString(it) }

    private fun List<SpeakerDTO>.toRealm(): RealmListSpeaker {
        val o = this
        return RealmListSpeaker().apply {
            uuid = "RealmListSpeaker"
            payload = json.encodeToString(o)
        }
    }

    private fun List<SessionDTO>.toRealm(updatedAt: RealmInstant? = RealmInstant.now()): RealmListSession {
        val o = this
        return RealmListSession().apply {
            uuid = "RealmListSession"
            payload = json.encodeToString(o)
            this.updatedAt = updatedAt
        }
    }

    private fun List<CompanyDTO>.toRealm(): RealmListCompany {
        val o = this
        return RealmListCompany().apply {
            uuid = "RealmListCompany"
            payload = json.encodeToString(o)
        }
    }

    private fun HomeDataDTO.toRealm(): RealmHomeResponse {
        val o = this
        return RealmHomeResponse().apply {
            uuid = "RealmHomeResponse"
            payload = json.encodeToString(o)
        }
    }

    private class ShouldNotify : RealmObject {
        @PrimaryKey
        var id: String = "1234"
        var isEnabled: Boolean = false
    }

    private class RealmListSpeaker : RealmObject {
        @PrimaryKey
        var uuid: String = "RealmListSpeaker"
        var payload: String? = null
        var updatedAt: RealmInstant = RealmInstant.now()
    }

    private class RealmListSession : RealmObject {
        @PrimaryKey
        var uuid: String = "RealmListSession"
        var payload: String? = null
        var updatedAt: RealmInstant? = RealmInstant.now()
    }

    private class RealmListCompany : RealmObject {
        @PrimaryKey
        var uuid: String = "RealmListCompany"
        var payload: String? = null
        var updatedAt: RealmInstant = RealmInstant.now()
    }

    private class RealmHomeResponse : RealmObject {
        @PrimaryKey
        var uuid: String = "RealmHomeResponse"
        var payload: String? = null
        var updatedAt: RealmInstant = RealmInstant.now()
    }
}