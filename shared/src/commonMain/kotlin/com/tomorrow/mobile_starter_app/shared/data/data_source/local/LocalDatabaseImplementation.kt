package com.tomorrow.mobile_starter_app.shared.data.data_source.local

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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class LocalDatabaseImplementation(
    private val json: Json,
) : LocalDatabase {
    private val configuration: RealmConfiguration by lazy {
        RealmConfiguration.Builder(
            schema = setOf(
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

    //Your getters and setters goes here

    override suspend fun clearData() = realm.write {
    //Don't forget to delete everything
    }

    //realm classes goes here
}