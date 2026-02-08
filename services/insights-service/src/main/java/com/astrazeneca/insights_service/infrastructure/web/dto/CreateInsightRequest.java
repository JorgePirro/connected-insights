package com.astrazeneca.insights_service.infrastructure.web.dto;

import com.astrazeneca.insights_service.domain.model.ImpactLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInsightRequest {

    @NotBlank(message = "Description is required")
    private String description;

    private String therapeuticArea;

    private UUID competitorId;

    @NotNull(message = "Impact level is required")
    private ImpactLevel impactLevel;
}

