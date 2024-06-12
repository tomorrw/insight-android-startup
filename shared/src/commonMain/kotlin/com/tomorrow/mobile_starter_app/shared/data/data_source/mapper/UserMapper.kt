package com.tomorrow.mobile_starter_app.shared.data.data_source.mapper

import com.tomorrow.mobile_starter_app.shared.data.data_source.model.UserDTO
import com.tomorrow.kmmProjectStartup.data.utils.EntityMapper
import com.tomorrow.kmmProjectStartup.domain.model.Email
import com.tomorrow.kmmProjectStartup.domain.model.FullName
import com.tomorrow.kmmProjectStartup.domain.model.Salutation
import com.tomorrow.mobile_starter_app.shared.domain.model.User
import com.tomorrow.kmmProjectStartup.domain.utils.PhoneNumber

class UserMapper : EntityMapper<User, UserDTO> {
    override fun mapFromEntity(entity: UserDTO) = User(
        id = entity.id,
        uuid = entity.uuid,
        fullName = FullName(
            salutation = SalutationMapper().mapFromEntityIfNotNull(entity.salutation) ?: Salutation.None,
            firstName = entity.firstName,
            lastName = entity.lastName
        ),
        email = entity.email?.let { Email(it) },
        phoneNumber = PhoneNumber(entity.phoneNumber),
        notificationTopics = entity.notificationTopics ?: listOf()
    )

    override fun mapToEntity(domainModel: User): UserDTO {
        TODO("Not yet implemented")
    }
}