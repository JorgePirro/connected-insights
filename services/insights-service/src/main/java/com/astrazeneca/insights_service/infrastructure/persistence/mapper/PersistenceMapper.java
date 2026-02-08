package com.astrazeneca.insights_service.infrastructure.persistence.mapper;

import com.astrazeneca.insights_service.domain.model.ImpactLevel;
import com.astrazeneca.insights_service.domain.model.Insight;
import com.astrazeneca.insights_service.infrastructure.persistence.entity.InsightEntity;
import com.astrazeneca.insights_service.infrastructure.persistence.entity.InsightEntity.ImpactLevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersistenceMapper {

    @Mapping(target = "impactLevel", source = "impactLevel")
    InsightEntity toEntity(Insight insight);

    @Mapping(target = "impactLevel", source = "impactLevel")
    Insight toDomain(InsightEntity entity);

    default ImpactLevelEntity toEntityImpactLevel(ImpactLevel impactLevel) {
        if (impactLevel == null) {
            return null;
        }
        return switch (impactLevel) {
            case HIGH -> ImpactLevelEntity.HIGH;
            case MEDIUM -> ImpactLevelEntity.MEDIUM;
            case LOW -> ImpactLevelEntity.LOW;
        };
    }

    default ImpactLevel toDomainImpactLevel(ImpactLevelEntity impactLevelEntity) {
        if (impactLevelEntity == null) {
            return null;
        }
        return switch (impactLevelEntity) {
            case HIGH -> ImpactLevel.HIGH;
            case MEDIUM -> ImpactLevel.MEDIUM;
            case LOW -> ImpactLevel.LOW;
        };
    }
}

