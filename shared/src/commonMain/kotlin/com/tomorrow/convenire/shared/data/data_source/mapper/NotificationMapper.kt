package com.tomorrow.convenire.shared.data.data_source.mapper

import com.tomorrow.convenire.shared.data.data_source.model.NotificationDTO
import com.tomorrow.convenire.shared.data.data_source.utils.EntityMapper
import com.tomorrow.convenire.shared.domain.model.Notification

class NotificationMapper: EntityMapper<(Result<Notification>) -> Unit, (Result<NotificationDTO>) -> Unit> {
    override fun mapFromEntity(entity: (Result<NotificationDTO>) -> Unit): (Result<Notification>) -> Unit = {result ->
        entity(
            if(result.isSuccess) {
                Result.success(
                    NotificationDTO(
                        result.getOrNull()?.result ?: false,
                        result.getOrNull()?.message ?: ""
                    )
                )
            } else{
                Result.failure(result.exceptionOrNull()!!)
            }
        )
    }
    override fun mapToEntity(domainModel: (Result<Notification>) -> Unit): (Result<NotificationDTO>) -> Unit = { result ->
        domainModel(
            if(result.isSuccess) {
                Result.success(
                    Notification(
                        result.getOrNull()?.result ?: false,
                        result.getOrNull()?.message ?: ""
                    )
                )
            } else{
                Result.failure(result.exceptionOrNull()!!)
            }
        )

    }
}