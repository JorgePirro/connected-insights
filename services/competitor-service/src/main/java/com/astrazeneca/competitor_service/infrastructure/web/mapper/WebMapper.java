package com.astrazeneca.competitor_service.infrastructure.web.mapper;

import com.astrazeneca.competitor_service.domain.model.ClinicalTrial;
import com.astrazeneca.competitor_service.domain.model.Competitor;
import com.astrazeneca.competitor_service.infrastructure.web.dto.AddTrialRequest;
import com.astrazeneca.competitor_service.infrastructure.web.dto.CompetitorResponse;
import com.astrazeneca.competitor_service.infrastructure.web.dto.CreateCompetitorRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * MapStruct Mapper - Maps between DTOs and Domain Models
 * Infrastructure/Web Layer
 */
@Mapper(componentModel = "spring")
public interface WebMapper {

    Competitor toDomain(CreateCompetitorRequest request);

    CompetitorResponse toResponse(Competitor competitor);

    List<CompetitorResponse> toResponseList(List<Competitor> competitors);

    ClinicalTrial toDomain(AddTrialRequest request);

    CompetitorResponse.ClinicalTrialDto toDto(ClinicalTrial clinicalTrial);
}

