package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Notification
import org.koin.core.component.KoinComponent

class NotificationMapper : KoinComponent,
    EntityMapper<(Result<Notification>) -> Unit, (Result<NotificationDTO>) -> Unit> {
    override fun mapFromEntity(entity: (Result<NotificationDTO>) -> Unit): (Result<Notification>) -> Unit {
        TODO()
    }

    override fun mapToEntity(domainModel: (Result<Notification>) -> Unit): (Result<NotificationDTO>) -> Unit =
        { result ->
            domainModel(
                if (result.isSuccess) {
                    when (result.getOrNull()?.event) {
                        NotificationDTO.EventType.CHECK_IN.value -> Result.success(
                            Notification(
                                result.getOrNull()?.data?.result ?: false,
                                result.getOrNull()?.data?.message ?: "Error"
                            )
                        )

                        NotificationDTO.EventType.ERROR.value -> Result.success(
                            Notification(
                                result.getOrNull()?.data?.result ?: false,
                                result.getOrNull()?.data?.message ?: "Error"
                            )
                        )

                        else -> Result.failure(
                            result.exceptionOrNull() ?: Exception("Unknown event type")
                        )

                    }
                } else Result.failure(
                    result.exceptionOrNull() ?: Exception("Unknown error or event type")
                )


            )
        }
}