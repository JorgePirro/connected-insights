package com.astrazeneca.competitor_service.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO - Infrastructure/Web Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new competitor")
public class CreateCompetitorRequest {

    @Schema(description = "Name of the pharmaceutical competitor", example = "Pfizer Inc.", required = true)
    private String name;

    @Schema(description = "List of therapeutic areas the competitor focuses on",
            example = "[\"Oncology\", \"Immunology\", \"Cardiology\"]")
    private List<String> therapeuticAreas;

    @Schema(description = "Headquarters location", example = "New York, USA", required = true)
    private String headquarters;
}
