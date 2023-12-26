package com.tomorrow.lda.shared.data.data_source.utils

interface EntityMapper<DomainModel, Entity> {
    /**
     * maps Entity to DomainModel
     *
     * @param entity to be mapped to DomainModel
     * @return the DomainModel created from the Entity
     */
    fun mapFromEntity(entity: Entity): DomainModel

    /**
     * maps DomainModel to Entity
     *
     * @param domainModel to be mapped to Entity
     * @return the Entity created from the domainModel
     */
    fun mapToEntity(domainModel: DomainModel): Entity

    /**
     * maps Entity to DomainModel if not null else it returns null
     *
     * @param entity to be mapped to DomainModel
     * @return DomainModel?
     */
    fun mapFromEntityIfNotNull(entity: Entity?): DomainModel? {
        return if (entity != null) mapFromEntity(entity) else null
    }

    /**
     * maps DomainModel to Entity if not null else it returns null
     *
     * @param domainModel to be mapped to entity
     * @return Entity?
     */
    fun mapToEntityIfNotNull(domainModel: DomainModel?): Entity? {
        return if (domainModel != null) mapToEntity(domainModel) else null
    }
}