package com.astrazeneca.competitor_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain Model - Pure Java (No framework dependencies)
 * Contains business logic for managing competitor data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competitor {
    private Long id;
    private String name;
    private List<String> therapeuticAreas = new ArrayList<>();
    private String headquarters;
    private List<ClinicalTrial> clinicalTrials = new ArrayList<>();

    /**
     * Business logic: Add a clinical trial to this competitor
     */
    public void addClinicalTrial(ClinicalTrial trial) {
        if (trial == null) {
            throw new IllegalArgumentException("Clinical trial cannot be null");
        }
        if (this.clinicalTrials == null) {
            this.clinicalTrials = new ArrayList<>();
        }
        this.clinicalTrials.add(trial);
    }
}
