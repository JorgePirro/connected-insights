package com.astrazeneca.competitor_service.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO - Infrastructure/Web Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for adding a clinical trial to a competitor")
public class AddTrialRequest {

    @Schema(description = "Unique clinical trial identifier", example = "NCT12345678", required = true)
    private String trialId;

    @Schema(description = "Name of the clinical trial", example = "Phase III Lung Cancer Study", required = true)
    private String name;

    @Schema(description = "Clinical trial phase", example = "Phase III", required = true)
    private String phase;

    @Schema(description = "Current status of the trial", example = "Recruiting", required = true)
    private String status;

    @Schema(description = "Medical indication being studied", example = "Non-Small Cell Lung Cancer", required = true)
    private String indication;
}
