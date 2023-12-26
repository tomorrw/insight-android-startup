package com.tomorrow.lda.shared.data.data_source.mapper

import com.tomorrow.lda.shared.data.data_source.model.UserDTO
import com.tomorrow.lda.shared.data.data_source.utils.EntityMapper
import com.tomorrow.lda.shared.domain.model.Action
import com.tomorrow.lda.shared.domain.model.Email
import com.tomorrow.lda.shared.domain.model.League
import com.tomorrow.lda.shared.domain.model.User
import com.tomorrow.lda.shared.domain.utils.PhoneNumber

class UserMapper : EntityMapper<User, UserDTO> {
    override fun mapFromEntity(entity: UserDTO) = User(
        id = entity.id,
        uuid = entity.uuid,
        name = entity.name,
        email = entity.email?.let { Email(it) },
        phoneNumber = PhoneNumber(entity.phoneNumber),
        hasPaid = entity.hasPaid == true,
        nextLeagueName = entity.nextLeagueName ?: "Silver",
        league = if (entity.league != null) LeagueMapper().mapFromEntity(entity.league) else League(
            name = "Bronze League",
            lecturesAttendedCount = 0,
            lecturesRemaining = 5,
            percentage = 0f,
            color = "BE8D69"
        ),
        actions = entity.actions?.map { ActionMapper().mapFromEntity(it) } ?: listOf()
    )

    override fun mapToEntity(domainModel: User): UserDTO {
        TODO("Not yet implemented")
    }
}