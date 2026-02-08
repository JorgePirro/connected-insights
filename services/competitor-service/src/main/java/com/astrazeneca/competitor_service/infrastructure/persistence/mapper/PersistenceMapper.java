package com.astrazeneca.competitor_service.infrastructure.persistence.mapper;

import com.astrazeneca.competitor_service.domain.model.ClinicalTrial;
import com.astrazeneca.competitor_service.domain.model.Competitor;
import com.astrazeneca.competitor_service.infrastructure.persistence.entity.ClinicalTrialEntity;
import com.astrazeneca.competitor_service.infrastructure.persistence.entity.CompetitorEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct Mapper - Maps between Domain Models and JPA Entities
 * Infrastructure Layer
 */
@Mapper(componentModel = "spring")
public interface PersistenceMapper {

    @Mapping(target = "competitor", ignore = true)
    ClinicalTrialEntity toEntity(ClinicalTrial clinicalTrial);

    ClinicalTrial toDomain(ClinicalTrialEntity clinicalTrialEntity);

    @Mapping(target = "clinicalTrials", source = "clinicalTrials")
    CompetitorEntity toEntity(Competitor competitor);

    @Mapping(target = "clinicalTrials", source = "clinicalTrials")
    Competitor toDomain(CompetitorEntity competitorEntity);

    List<Competitor> toDomainList(List<CompetitorEntity> competitorEntities);

    @AfterMapping
    default void linkTrialsToCompetitor(@MappingTarget CompetitorEntity competitorEntity) {
        if (competitorEntity.getClinicalTrials() != null) {
            competitorEntity.getClinicalTrials().forEach(trial -> trial.setCompetitor(competitorEntity));
        }
    }
}

