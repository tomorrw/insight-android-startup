package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.model.NotificationDataDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Notification
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationMapper : KoinComponent,
    EntityMapper<(Result<Notification>) -> Unit, (Result<NotificationDTO>) -> Unit> {
    val json: Json by inject()
    override fun mapFromEntity(entity: (Result<NotificationDTO>) -> Unit): (Result<Notification>) -> Unit {
        TODO()
    }

    override fun mapToEntity(domainModel: (Result<Notification>) -> Unit): (Result<NotificationDTO>) -> Unit =
        { result ->
            domainModel(
                if (result.isSuccess && result.getOrNull()?.event == NotificationDTO.EventType.CHECK_IN.value) {
                    val response = result.getOrNull()?.data
                        ?: "{ \"result\": false, \"message\": \"Error\" } }"
                    val model = json.decodeFromString<NotificationDataDTO>(response)
                    Result.success(
                        Notification(
                            model.result ?: false,
                            model.message ?: "Error"
                        )
                    )
                } else {
                    Result.failure(
                        result.exceptionOrNull() ?: Exception("Unknown error or event type")
                    )
                }

            )
        }
}