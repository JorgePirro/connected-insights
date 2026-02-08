package com.astrazeneca.competitor_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain Model - Pure Java (No framework dependencies)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalTrial {
    private String trialId;
    private String name;
    private String phase;
    private String status;
    private String indication;
}
