package com.astrazeneca.competitor_service.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO - Infrastructure/Web Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Competitor response object with all details")
public class CompetitorResponse {

    @Schema(description = "Unique identifier of the competitor", example = "1")
    private Long id;

    @Schema(description = "Name of the pharmaceutical competitor", example = "Pfizer Inc.")
    private String name;

    @Schema(description = "List of therapeutic areas", example = "[\"Oncology\", \"Immunology\"]")
    private List<String> therapeuticAreas;

    @Schema(description = "Headquarters location", example = "New York, USA")
    private String headquarters;

    @Schema(description = "List of clinical trials associated with this competitor")
    private List<ClinicalTrialDto> clinicalTrials;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Clinical trial information")
    public static class ClinicalTrialDto {

        @Schema(description = "Clinical trial identifier", example = "NCT12345678")
        private String trialId;

        @Schema(description = "Trial name", example = "Phase III Lung Cancer Study")
        private String name;

        @Schema(description = "Trial phase", example = "Phase III")
        private String phase;

        @Schema(description = "Trial status", example = "Recruiting")
        private String status;

        @Schema(description = "Medical indication", example = "Non-Small Cell Lung Cancer")
        private String indication;
    }
}
