package com.astrazeneca.insights_service.infrastructure.web.dto;

import com.astrazeneca.insights_service.domain.model.ImpactLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsightResponse {

    private UUID id;
    private String description;
    private String therapeuticArea;
    private UUID competitorId;
    private ImpactLevel impactLevel;
    private Integer relevanceScore;
}

