package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.model.NotificationDataDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Notification
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
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
                if (result.isSuccess) {
                    val data =
                        json.decodeFromString<NotificationDataDTO>(result.getOrNull()?.data ?: "{}")
                    when (data.type) {
                        NotificationDataDTO.EventType.CHECK_IN.value -> Result.success(
                            Notification(
                                data.result ?: false,
                                data.message ?: "Error"
                            )
                        )
                        NotificationDataDTO.EventType.CHECK_OUT.value -> Result.success(
                            Notification(
                                data.result ?: false,
                                data.message ?: "Error"
                            )
                        )

                        NotificationDataDTO.EventType.ERROR.value -> Result.success(
                            Notification(
                                false,
                                data.message ?: "Error"
                            )
                        )
                        else -> Result.failure(Exception("Unknown error or event type"))
                    }
                } else Result.failure(
                    result.exceptionOrNull() ?: Exception("Unknown error or event type")
                )
            )
        }
}