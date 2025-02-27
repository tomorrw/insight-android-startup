package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.ActionDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.mobile_starter_app.shared.domain.model.Action

class ActionMapper: EntityMapper<Action, ActionDTO> {
    override fun mapFromEntity(entity: ActionDTO) = Action(
        url = entity.actionUrl ?: "",
        type = when (entity.actionType) {
            ActionDTO.ActionType.OpenLink -> Action.ActionType.OpenLink
            ActionDTO.ActionType.ApiHit -> Action.ActionType.ApiHit
            null -> Action.ActionType.OpenLink
        },
        label = entity.actionLabel ?: "Claim Offer",
        isPrimary = entity.isPrimary == 1,
        isDisabled = entity.isDisabled == 1
    )
    override fun mapToEntity(domainModel: Action): ActionDTO {
        TODO("Not yet implemented")
    }

}
